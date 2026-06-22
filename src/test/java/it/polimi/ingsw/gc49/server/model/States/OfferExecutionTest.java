package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link OfferExecution}.
 * <p>
 * {@code executeState()} can block when waiting for a player to consume their draw actions;
 * the tests here exercise the "no offers assigned" branch, where the while loop never enters
 * the wait and the transition is reached immediately.
 */
class OfferExecutionTest {

    private Game game;
    private Locks locks;

    @BeforeEach
    void setUp() {
        game = new Game(2, List.of("Peppe", "Wu"), "room");
        locks = new Locks();
    }

    @Test
    @DisplayName("currentStateType is OFFER_EXECUTION")
    void stateType() {
        OfferExecution state = new OfferExecution(game, locks);
        assertEquals(State.States.OFFER_EXECUTION, state.getCurrentStateType());
    }

    @Test
    @DisplayName("toString returns the localized state name")
    void toStringValue() {
        OfferExecution state = new OfferExecution(game, locks);
        assertEquals("Eseguimento delle offerte", state.toString());
    }

    @Test
    @DisplayName("executeState with no assigned offers transitions to RoundEnd when not last round")
    void executeNoOffersTransitionsToRoundEnd() {
        game.setLastRound(false);
        OfferExecution state = new OfferExecution(game, locks);

        State next = state.executeState();

        assertNotNull(next);
        assertEquals(State.States.OTHER, next.getCurrentStateType(), "RoundEnd reports OTHER as its state type");
        assertInstanceOf(RoundEnd.class, next);
    }

    @Test
    @DisplayName("executeState with no assigned offers transitions to GameEnd when last round")
    void executeNoOffersTransitionsToGameEndOnLastRound() {
        game.setLastRound(true);
        OfferExecution state = new OfferExecution(game, locks);

        State next = state.executeState();

        assertNotNull(next);
        assertInstanceOf(GameEnd.class, next);
    }
}
