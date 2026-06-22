package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link RoundEnd}.
 */
class RoundEndTest {

    private Game game;
    private Locks locks;

    @BeforeEach
    void setUp() {
        game = new Game(2, List.of("Peppe", "Wu"), "room");
        locks = new Locks();
    }

    @Test
    @DisplayName("currentStateType is OTHER")
    void stateType() {
        RoundEnd state = new RoundEnd(game, locks);
        assertEquals(State.States.OTHER, state.getCurrentStateType());
    }

    @Test
    @DisplayName("toString returns the localized state name")
    void toStringValue() {
        RoundEnd state = new RoundEnd(game, locks);
        assertEquals("Fine del round", state.toString());
    }

    @Test
    @DisplayName("executeState transitions back to OfferChoosing or forward to EraEnd depending on the deck")
    void executeStateTransitions() {
        RoundEnd state = new RoundEnd(game, locks);
        State next = state.executeState();

        assertNotNull(next);
        // Either the round continues (OfferChoosing) or the era changes (EraEnd).
        assertTrue(next instanceof OfferChoosing || next instanceof EraEnd,
                "next state must be either OfferChoosing or EraEnd, got: " + next.getClass());
    }
}
