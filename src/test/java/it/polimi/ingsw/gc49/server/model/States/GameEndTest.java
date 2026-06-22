package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Artist;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Builder;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Invention;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Inventor;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.EventManager;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link GameEnd}.
 * <p>
 * {@code executeState()} calls {@code cardBoard.endGame()} (which resolves any leftover
 * event cards still on the board, like Sustenance / Ritual / Painting / Hunting — these
 * award or subtract points) and {@code callGameEndEvent()} (which fires every registered
 * building listener). Both depend on the random deck shuffle, so the tests would be
 * non-deterministic if the board were not cleared first.
 * <p>
 * Each test calls {@link #cleanGameForEndgame(Game)} to remove every residual event card
 * and listener before exercising the actual scoring logic of {@code GameEnd}.
 */
class GameEndTest {

    private Game game;
    private Locks locks;
    private Player peppe;
    private Player wu;

    @BeforeEach
    void setUp() {
        game = new Game(2, List.of("Peppe", "Wu"), "room");
        locks = new Locks();
        peppe = game.getPlayers().get(0);
        wu = game.getPlayers().get(1);
        cleanGameForEndgame(game);
    }

    /**
     * Removes every source of non-deterministic point changes that would otherwise fire
     * inside {@code GameEnd.executeState()}: the event cards still on the board (resolved
     * by {@code endGame()}) and the building listeners registered on the event manager
     * (fired by {@code callGameEndEvent()}).
     */
    private static void cleanGameForEndgame(Game game) {
        // 1) clear every row of the board: getters expose the internal lists directly
        game.getCardBoard().getLine().getUpperLine().clear();
        game.getCardBoard().getLine().getLowerLine().clear();
        game.getCardBoard().getLine().getUpperBuilding().clear();
        game.getCardBoard().getLine().getLowerBuilding().clear();
        // 2) replace the event manager with a fresh empty one (no listeners)
        game.setEventManager(new EventManager());
    }

    @Test
    @DisplayName("currentStateType is OTHER")
    void stateType() {
        GameEnd state = new GameEnd(game, locks);
        assertEquals(State.States.OTHER, state.getCurrentStateType());
    }

    @Test
    @DisplayName("toString returns the localized state name")
    void toStringValue() {
        GameEnd state = new GameEnd(game, locks);
        assertEquals("End of the match", state.toString());
    }

    @Test
    @DisplayName("executeState returns null to signal the end of the state machine")
    void executeReturnsNull() {
        GameEnd state = new GameEnd(game, locks);
        assertNull(state.executeState());
    }

    @Test
    @DisplayName("executeState adds the builder points to every player")
    void addsBuilderPoints() {
        // peppe gains 5 from a single builder
        peppe.addCharacterCard(new Builder(0, 5, Era.FIRST, 2, null));
        // wu gains 2+3 from two builders
        wu.addCharacterCard(new Builder(0, 2, Era.FIRST, 2, null));
        wu.addCharacterCard(new Builder(0, 3, Era.FIRST, 2, null));

        new GameEnd(game, locks).executeState();

        assertEquals(5, peppe.getPoints());
        assertEquals(5, wu.getPoints());
    }

    @Test
    @DisplayName("executeState adds inventors * distinct inventions to every player")
    void addsInventorPoints() {
        // peppe: 3 inventors with 2 distinct inventions -> 6 points
        peppe.addCharacterCard(new Inventor(Invention.CANOE, Era.FIRST, 2, null));
        peppe.addCharacterCard(new Inventor(Invention.CANOE, Era.FIRST, 2, null));
        peppe.addCharacterCard(new Inventor(Invention.BREAD, Era.FIRST, 2, null));

        new GameEnd(game, locks).executeState();

        assertEquals(6, peppe.getPoints());
    }

    @Test
    @DisplayName("executeState adds 10 points per pair of artists")
    void addsArtistPoints() {
        // peppe: 5 artists -> 2 pairs -> 20 points
        for (int i = 0; i < 5; i++) {
            peppe.addCharacterCard(new Artist(Era.FIRST, 2, null));
        }
        // wu: 1 artist -> 0 pairs -> 0 points
        wu.addCharacterCard(new Artist(Era.FIRST, 2, null));

        new GameEnd(game, locks).executeState();

        assertEquals(20, peppe.getPoints());
        assertEquals(0, wu.getPoints());
    }

    @Test
    @DisplayName("executeState adds the building points to every player")
    void addsBuildingPoints() {
        peppe.data.addNumBuildingPoints(7);
        wu.data.addNumBuildingPoints(3);

        new GameEnd(game, locks).executeState();

        assertEquals(7, peppe.getPoints());
        assertEquals(3, wu.getPoints());
    }

    @Test
    @DisplayName("executeState sums all endgame bonuses on top of the points already accumulated")
    void sumsAllBonusesOnTopOfExistingPoints() {
        // pre-existing points
        peppe.setPoints(10);
        // builder 5 + inventor (1 * 1) + 2 artists (10) + buildings 4 = 20 bonus
        peppe.addCharacterCard(new Builder(0, 5, Era.FIRST, 2, null));
        peppe.addCharacterCard(new Inventor(Invention.CANOE, Era.FIRST, 2, null));
        peppe.addCharacterCard(new Artist(Era.FIRST, 2, null));
        peppe.addCharacterCard(new Artist(Era.FIRST, 2, null));
        peppe.data.addNumBuildingPoints(4);

        new GameEnd(game, locks).executeState();

        assertEquals(30, peppe.getPoints());
    }

    @Test
    @DisplayName("with an empty tribe and cleared board, no bonus is awarded")
    void noBonusForEmptyTribe() {
        new GameEnd(game, locks).executeState();
        assertEquals(0, peppe.getPoints());
        assertEquals(0, wu.getPoints());
    }
}
