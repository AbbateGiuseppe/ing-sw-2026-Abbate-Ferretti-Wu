package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.Totem;
import it.polimi.ingsw.gc49.server.model.playerExceptions.PlayerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link TotemChoosing}.
 * <p>
 * {@code executeState()} blocks on {@code locks.playerInput.wait()} until every player has
 * chosen a totem, then runs {@code randomizeStartingOrder}, which (a) seats every player on
 * an order slot and (b) hands out the starting food (2 / 3 / 3 / 4 / 4 by slot position).
 * <p>
 * Important: right after {@code TotemChoosing} ends, the game moves into {@code OfferChoosing},
 * whose {@code getNextPlayerOrderSlot()} immediately empties the order slots. So the order
 * slots cannot be observed reliably from the test thread. The persistent, observable effect
 * of {@code randomizeStartingOrder} is the <b>food granted to the Player objects</b>, which
 * is never cleared — that is what these tests assert on.
 */
class TotemChoosingTest {

    private Game game;
    private Locks locks;

    @BeforeEach
    void setUp() {
        game = new Game(2, List.of("Peppe", "Wu"), "room");
        locks = new Locks();
    }

    // --- construction-time contract ---

    @Test
    void stateType() {
        TotemChoosing state = new TotemChoosing(game, locks);
        assertEquals(State.States.TOTEM_CHOOSING, state.getCurrentStateType());
    }

    @Test
    void toStringValue() {
        TotemChoosing state = new TotemChoosing(game, locks);
        assertEquals("Scelta del totem", state.toString());
    }

    // --- realistic concurrent execution ---

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void totemChoiceUnblocksAndDealsFood() throws InterruptedException {
        Thread loop = new Thread(game::gameLoop, "game-loop");
        loop.setDaemon(true);
        loop.start();

        // both players pick a totem; each chooseTotem() notifies the monitor the state waits on
        chooseTotem(0, Totem.BLUE);
        chooseTotem(1, Totem.YELLOW);

        // wait until randomizeStartingOrder has handed out food to every player
        waitUntilFoodDealt();

        // every player must have received starting food (>0)
        for (Player p : game.getPlayers()) {
            assertTrue(p.getFood() > 0, p.getNickname() + " must have received starting food");
        }

        // rulebook: for a two-player game the totals are 2 and 3 (in some order after the shuffle)
        int foodPeppe = game.getPlayers().get(0).getFood();
        int foodWu = game.getPlayers().get(1).getFood();
        assertEquals(5, foodPeppe + foodWu, "the two players share 2 + 3 starting food");
        assertTrue((foodPeppe == 2 && foodWu == 3) || (foodPeppe == 3 && foodWu == 2),
                "each player gets either 2 or 3, depending on the random order");

        // both totems are registered
        assertEquals(2, game.getUsedTotems().size());
        assertTrue(game.getUsedTotems().contains(Totem.BLUE));
        assertTrue(game.getUsedTotems().contains(Totem.YELLOW));

        loop.interrupt();
    }

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void staysBlockedUntilAllChoose() throws InterruptedException {
        Thread loop = new Thread(game::gameLoop, "game-loop");
        loop.setDaemon(true);
        loop.start();

        // give the loop time to reach the wait inside TotemChoosing
        Thread.sleep(300);

        // only one player chooses: the state must remain blocked, so no food is dealt yet
        chooseTotem(0, Totem.BLUE);
        Thread.sleep(300);
        assertEquals(0, game.getPlayers().get(0).getFood(),
                "no food must be dealt while the second player has not chosen");
        assertEquals(0, game.getPlayers().get(1).getFood());

        // the second choice unblocks the state and food is finally dealt
        chooseTotem(1, Totem.YELLOW);
        waitUntilFoodDealt();
        assertEquals(5,
                game.getPlayers().get(0).getFood() + game.getPlayers().get(1).getFood());

        loop.interrupt();
    }

    // --- helpers ---

    /** chooseTotem ignores choices made outside TOTEM_CHOOSING (no exception), so just call it. */
    private void chooseTotem(int playerIndex, Totem totem) {
        try {
            game.chooseTotem(playerIndex, totem);
        } catch (PlayerException e) {
            fail("unexpected PlayerException while choosing a totem: " + e.getMessage());
        }
    }

    /** Spins until at least one player has been given starting food (randomizeStartingOrder ran). */
    private void waitUntilFoodDealt() throws InterruptedException {
        long deadline = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < deadline) {
            int total = game.getPlayers().stream().mapToInt(Player::getFood).sum();
            if (total > 0) {
                return;
            }
            Thread.sleep(20);
        }
        fail("randomizeStartingOrder did not deal food within the time limit");
    }
}
