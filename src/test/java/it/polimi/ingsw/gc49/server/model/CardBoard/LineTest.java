package it.polimi.ingsw.gc49.server.model.CardBoard;

import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.BonusHuntingCard;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Artist;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Hunter;
import it.polimi.ingsw.gc49.server.model.Card.EventCard.HuntingEvent;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.EventManager;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.States.EraEndedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Line}.
 * <p>
 * A real {@link Game} is constructed (its constructor only runs the {@code InitialSetup}
 * state once, without entering the game loop) and a separate, manually controlled
 * {@link Deck} is passed to {@link Line}'s constructor. This lets the tests deterministically
 * decide which cards land in each row.
 */
class LineTest {

    private Game game;
    private List<Player> players;
    private Deck deck;

    @BeforeEach
    void setUp() {
        // a real Game with 2 players: the constructor runs the InitialSetup state once
        game = new Game(2, List.of("Peppe", "Wu"), "testRoom");
        players = game.getPlayers();

        // a fresh, controlled deck for the tests below
        deck = new Deck();
    }

    @Test
    @DisplayName("Constructor with an empty deck builds a Line with empty rows")
    void constructorOnEmptyDeck() {
        Line line = new Line(game, deck);
        assertTrue(line.getUpperLine().isEmpty());
        assertTrue(line.getLowerLine().isEmpty());
        assertTrue(line.getUpperBuilding().isEmpty());
        assertTrue(line.getLowerBuilding().isEmpty());
    }

    @Test
    @DisplayName("Constructor with character cards fills the lower line first, then the upper line")
    void constructorFillsRowsCorrectly() {
        // 2 players -> lower needs 3 character cards (numPlayers+1), upper needs 6 (numPlayers+4)
        for (int i = 0; i < 9; i++) {
            deck.addCard(new Hunter(false, Era.FIRST, 2, null));
        }

        Line line = new Line(game, deck);

        assertEquals(3, line.getLowerLine().size(), "lower line should hold numPlayers+1 cards");
        assertEquals(6, line.getUpperLine().size(), "upper line should hold numPlayers+4 cards");
    }

    @Test
    @DisplayName("Event cards are placed on the upper line, not the lower one")
    void eventCardsGoToUpperLine() {
        EventManager mgr = new EventManager();
        // 2 events then 7 characters: events should land on the upper line
        deck.addCard(new HuntingEvent(2, mgr, Era.FIRST, 2, null));
        deck.addCard(new HuntingEvent(2, mgr, Era.FIRST, 2, null));
        for (int i = 0; i < 7; i++) {
            deck.addCard(new Hunter(false, Era.FIRST, 2, null));
        }

        Line line = new Line(game, deck);

        assertEquals(3, line.getLowerLine().size());
        // events are in the upper line; the lower line must contain only character cards
        line.getLowerLine().forEach(c -> assertFalse(c instanceof HuntingEvent));
    }

    @Test
    @DisplayName("getCurrentEra defaults to the first era; hasEraChanged is false initially")
    void initialEraState() {
        Line line = new Line(game, deck);
        assertEquals(Era.first(), line.getCurrentEra());
        assertFalse(line.hasEraChanged());
    }

    @Test
    @DisplayName("drawUpperCharacter returns null on an out-of-range index")
    void drawUpperOutOfRangeReturnsNull() {
        Line line = new Line(game, deck);
        assertNull(line.drawUpperCharacter(-1, players.get(0)));
        assertNull(line.drawUpperCharacter(0, players.get(0)));  // empty line
        assertNull(line.drawUpperCharacter(100, players.get(0)));
    }

    @Test
    @DisplayName("drawUpperCharacter removes the picked card from the upper line and returns it")
    void drawUpperCharacterRemovesAndReturns() {
        for (int i = 0; i < 9; i++) deck.addCard(new Hunter(false, Era.FIRST, 2, null));
        Line line = new Line(game, deck);
        int sizeBefore = line.getUpperLine().size();

        Card drawn = line.drawUpperCharacter(0, players.get(0));

        assertNotNull(drawn);
        assertEquals(sizeBefore - 1, line.getUpperLine().size());
    }

    @Test
    @DisplayName("drawLowerCharacter returns null on an out-of-range index")
    void drawLowerOutOfRangeReturnsNull() {
        Line line = new Line(game, deck);
        assertNull(line.drawLowerCharacter(0, players.get(0)));
    }

    @Test
    @DisplayName("drawLowerCharacter removes the picked card from the lower line and returns it")
    void drawLowerCharacterRemovesAndReturns() {
        for (int i = 0; i < 9; i++) deck.addCard(new Hunter(false, Era.FIRST, 2, null));
        Line line = new Line(game, deck);
        int sizeBefore = line.getLowerLine().size();

        Card drawn = line.drawLowerCharacter(0, players.get(0));

        assertNotNull(drawn);
        assertEquals(sizeBefore - 1, line.getLowerLine().size());
    }

    @Test
    @DisplayName("drawLowerBuilding returns null when the building card is unaffordable")
    void drawUnaffordableBuildingReturnsNullAndLeavesIt() {
        // build a Line with no characters and one building manually placed in the lower row
        Line line = new Line(game, deck);
        BonusHuntingCard expensive = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 3, 100, Era.FIRST, 2, null);
        line.getLowerBuilding().add(expensive);

        Player peppe = players.get(0);
        peppe.setFood(0);

        Card result = line.drawLowerBuilding(0, peppe);

        assertNull(result);
        assertEquals(1, line.getLowerBuilding().size(), "card must remain in the row when unaffordable");
    }

    @Test
    @DisplayName("endRound discards the lower line, shifts the upper line down, and refills the upper line")
    void endRoundShiftsAndRefills() throws EraEndedException {
        // start: 9 cards Era I -> lower 3, upper 6
        for (int i = 0; i < 9; i++) deck.addCard(new Hunter(false, Era.FIRST, 2, null));
        // refill source: 6 more Era I cards available
        for (int i = 0; i < 6; i++) deck.addCard(new Hunter(false, Era.FIRST, 2, null));

        Line line = new Line(game, deck);
        line.endRound(2);

        assertEquals(6, line.getLowerLine().size(), "the old upper line moves down");
        assertEquals(6, line.getUpperLine().size(), "the upper line is refilled with numPlayers+4 new cards");
        assertFalse(line.hasEraChanged());
    }

    @Test
    @DisplayName("endRound throws EraEndedException when refilled cards belong to a new era")
    void endRoundDetectsEraChange() {
        // setup with Era I, then refill with Era II
        for (int i = 0; i < 9; i++) deck.addCard(new Hunter(false, Era.FIRST, 2, null));
        for (int i = 0; i < 6; i++) deck.addCard(new Hunter(false, Era.SECOND, 2, null));

        Line line = new Line(game, deck);
        assertThrows(EraEndedException.class, () -> line.endRound(2));
        assertTrue(line.hasEraChanged());
        assertEquals(Era.SECOND, line.getNewEra());
    }

    @Test
    @DisplayName("clearEraChange resets the era-change flags")
    void clearEraChangeResets() {
        for (int i = 0; i < 9; i++) deck.addCard(new Hunter(false, Era.FIRST, 2, null));
        for (int i = 0; i < 6; i++) deck.addCard(new Hunter(false, Era.SECOND, 2, null));
        Line line = new Line(game, deck);
        assertThrows(EraEndedException.class, () -> line.endRound(2));

        line.clearEraChange();

        assertFalse(line.hasEraChanged());
        assertNull(line.getNewEra());
    }

    @Test
    @DisplayName("endGame clears all rows")
    void endGameClearsAllRows() {
        for (int i = 0; i < 9; i++) deck.addCard(new Hunter(false, Era.FIRST, 2, null));
        Line line = new Line(game, deck);

        line.endGame();

        assertTrue(line.getUpperLine().isEmpty());
        assertTrue(line.getLowerLine().isEmpty());
        assertTrue(line.getUpperBuilding().isEmpty());
        assertTrue(line.getLowerBuilding().isEmpty());
    }
}
