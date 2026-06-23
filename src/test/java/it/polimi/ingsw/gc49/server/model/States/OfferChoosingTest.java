package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link OfferChoosing}.
 * <p>
 * {@code executeState()} blocks on {@code locks.playerInput.wait()} whenever a player on an
 * order slot has not chosen an offer yet. The "fast-path" tested below relies on the fact
 * that, right after Game construction (i.e. InitialSetup has run but TotemChoosing has not),
 * the order board is still empty, so {@code getNextPlayerOrderSlot()} returns null right
 * away and the while loop never enters the wait. This lets us reach the transition into
 * OfferExecution without ever blocking.
 */
class OfferChoosingTest {

    private Game game;
    private Locks locks;

    @BeforeEach
    void setUp() {
        game = new Game(2, List.of("Peppe", "Wu"), "room");
        locks = new Locks();
    }

    @Test
    void stateType() {
        OfferChoosing state = new OfferChoosing(game, locks);
        assertEquals(State.States.OFFER_CHOOSING, state.getCurrentStateType());
    }

    @Test
    void toStringValue() {
        OfferChoosing state = new OfferChoosing(game, locks);
        assertEquals("Scelta delle offerte", state.toString());
    }

    @Test
    void executeWithEmptyOrderSlotsTransitions() {
        // order board is empty after Game construction (randomizeStartingOrder happens in TotemChoosing).
        OfferChoosing state = new OfferChoosing(game, locks);

        State next = state.executeState();

        assertNotNull(next);
        assertEquals(State.States.OFFER_EXECUTION, next.getCurrentStateType());
    }
}
