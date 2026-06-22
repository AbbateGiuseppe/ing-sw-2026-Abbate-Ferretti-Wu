package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link InitialSetup}.
 * <p>
 * The Game constructor automatically runs {@code InitialSetup.executeState()} once
 * (and only once), so building a real Game is enough to verify the setup side-effects.
 */
class InitialSetupTest {

    @Test
    @DisplayName("executeState creates a player per nickname with the correct index")
    void buildsPlayers() {
        Game game = new Game(3, List.of("Peppe", "Wu", "Massi"), "room");
        assertEquals(3, game.getPlayers().size());
        assertEquals("Peppe", game.getPlayers().get(0).getNickname());
        assertEquals("Wu", game.getPlayers().get(1).getNickname());
        assertEquals("Massi", game.getPlayers().get(2).getNickname());
        for (int i = 0; i < 3; i++) {
            assertEquals(i, game.getPlayers().get(i).getPlayerIndex());
        }
    }

    @Test
    @DisplayName("executeState builds a Track sized for the player count")
    void buildsTrack() {
        Game game = new Game(4, List.of("Peppe", "Wu", "Massi", "Peppe"), "room");
        assertNotNull(game.getTrack());
        assertEquals(6, game.getTrack().getOfferBoard().size());
        assertEquals(4, game.getTrack().getOrderBoard().size());
    }

    @Test
    @DisplayName("executeState builds an EventManager and a CardBoard")
    void buildsManagerAndBoard() {
        Game game = new Game(2, List.of("Peppe", "Wu"), "room");
        assertNotNull(game.getEventManager());
        assertNotNull(game.getCardBoard());
    }

    @Test
    @DisplayName("executeState sets lastRound to false")
    void lastRoundInitiallyFalse() {
        Game game = new Game(2, List.of("Peppe", "Wu"), "room");
        assertFalse(game.isLastRound());
    }

    @Test
    @DisplayName("executeState returns a non-null next state (TotemChoosing)")
    void returnsNextState() {
        Game game = new Game(2, List.of("Peppe", "Wu"), "room");
        Locks locks = new Locks();
        // re-running InitialSetup directly: it must produce a next state
        InitialSetup setup = new InitialSetup(game, locks, 2, List.of("Peppe", "Wu"));
        State next = setup.executeState();
        assertNotNull(next);
        assertEquals(State.States.TOTEM_CHOOSING, next.getCurrentStateType());
    }

    @Test
    @DisplayName("setup works for the 5-player case too")
    void fivePlayerSetup() {
        Game game = new Game(5, List.of("Peppe", "Wu", "Massi", "Peppe", "Wu"), "room");
        assertEquals(5, game.getPlayers().size());
        assertEquals(7, game.getTrack().getOfferBoard().size());
        assertEquals(5, game.getTrack().getOrderBoard().size());
    }

    @Test
    @DisplayName("toString returns the localized state name")
    void toStringValue() {
        InitialSetup setup = new InitialSetup(
                new Game(2, List.of("Peppe", "Wu"), "room"),
                new Locks(),
                2,
                List.of("Peppe", "Wu"));
        assertEquals("Preparazione", setup.toString());
    }

    @Test
    @DisplayName("currentStateType is OTHER for InitialSetup")
    void stateTypeIsOther() {
        InitialSetup setup = new InitialSetup(
                new Game(2, List.of("Peppe", "Wu"), "room"),
                new Locks(),
                2,
                List.of("Peppe", "Wu"));
        assertEquals(State.States.OTHER, setup.getCurrentStateType());
    }
}
