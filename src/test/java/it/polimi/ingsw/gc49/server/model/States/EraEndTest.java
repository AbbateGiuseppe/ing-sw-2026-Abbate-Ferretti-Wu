package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link EraEnd}.
 */
class EraEndTest {

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
        EraEnd state = new EraEnd(game, locks);
        assertEquals(State.States.OTHER, state.getCurrentStateType());
    }

    @Test
    @DisplayName("toString returns the localized state name")
    void toStringValue() {
        EraEnd state = new EraEnd(game, locks);
        assertEquals("Fine d'era", state.toString());
    }

    @Test
    @DisplayName("executeState transitions to OfferChoosing")
    void executeStateTransitionsToOfferChoosing() {
        EraEnd state = new EraEnd(game, locks);
        State next = state.executeState();

        assertNotNull(next);
        assertInstanceOf(OfferChoosing.class, next);
        assertEquals(State.States.OFFER_CHOOSING, next.getCurrentStateType());
    }
}
