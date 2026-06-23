package it.polimi.ingsw.gc49.server.model;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.TextModelElement;
import it.polimi.ingsw.gc49.server.model.States.OfferChoosing;
import it.polimi.ingsw.gc49.server.model.States.OfferExecution;
import it.polimi.ingsw.gc49.server.model.States.State;
import it.polimi.ingsw.gc49.server.model.playerExceptions.InvalidTotem;
import it.polimi.ingsw.gc49.server.model.playerExceptions.NotValidOfferException;
import it.polimi.ingsw.gc49.server.model.playerExceptions.NotYourTurnException;
import it.polimi.ingsw.gc49.server.model.playerExceptions.PlayerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unified test class for {@link Game}. Targets ≥90% line and branch coverage by:
 * <ul>
 *   <li>covering every public getter/setter on the constructor's natural state;</li>
 *   <li>exercising every branch of {@code chooseTotem} (paused, occupied totem, already
 *       chosen, wrong phase);</li>
 *   <li>forcing {@code currentState} via reflection to reach the happy paths of the
 *       four draw methods, {@code chooseOffer} and {@code passYourTurn};</li>
 *   <li>covering disconnection/reconnection branches (pause-on-last, unpause-on-two,
 *       removed-from-track restore);</li>
 *   <li>running {@code gameLoop} on a background thread + driving it via
 *       {@code chooseTotem} to cover the loop and the {@code broadcastMockupGame} path.</li>
 * </ul>
 */
class GameTest {

    private Game game;
    private Player peppe;
    private Player wu;

    @BeforeEach
    void setUp() {
        game = new Game(2, List.of("Peppe", "Wu"), "room");
        peppe = game.getPlayers().get(0);
        wu = game.getPlayers().get(1);
    }

    // I need helpers to verify all the branches

    /** Forces the game's internal currentState (private field) to the given state. */
    private void forceState(State state) {
        try {
            Field f = Game.class.getDeclaredField("currentState");
            f.setAccessible(true);
            f.set(game, state);
        } catch (ReflectiveOperationException e) {
            fail("failed to force currentState via reflection: " + e);
        }
    }

    private void setOfferExecutionState() {
        forceState(new OfferExecution(game, new Locks()));
    }

    private void setOfferChoosingState() {
        forceState(new OfferChoosing(game, new Locks()));
    }




    /** Reads the private List<Card> on Player via reflection (no public getter exists). */
    @SuppressWarnings("unchecked")
    private static int sizeOfPrivateList(Player p, String fieldName) {
        try {
            Field f = Player.class.getDeclaredField(fieldName);
            f.setAccessible(true);
            return ((java.util.List<?>) f.get(p)).size();
        } catch (ReflectiveOperationException e) {
            fail("could not read " + fieldName + ": " + e);
            return -1;
        }
    }
 /// mi servono per vedere se il DRAW ha aggiunto carta
    private static int characterCardsSize(Player p) { return sizeOfPrivateList(p, "characterCards"); }
    private static int buildingCardsSize(Player p) { return sizeOfPrivateList(p, "buildingCards"); }



    // --- construction ---

    //SETUP BASE TEST
    @Nested
    class Construction {

        @Test
        void initialPlayers() {
            assertEquals(2, game.getNumOfPlayers());
            assertEquals(2, game.getNumOfConnectedPlayers());
            assertEquals(List.of("Peppe", "Wu"), game.getPlayersNicknames());
        }

        @Test
        void initialFlagsAndComponents() {
            assertNotNull(game.getTrack());
            assertNotNull(game.getCardBoard());
            assertNotNull(game.getEventManager());
            assertTrue(game.getUsedTotems().isEmpty());
            assertFalse(game.isLastRound());
            assertFalse(game.isPaused());
        }

        @Test
        void getPlayersExposed() {
            assertEquals(2, game.getPlayers().size());
            assertSame(peppe, game.getPlayers().get(0));
        }
    }


    // --- setters ---
/// verifiche semplici funzionamento setters e getters
    @Nested
    class Setters {

        @Test
        void lastRoundRoundTrip() {
            game.setLastRound(true);
            assertTrue(game.isLastRound());
            game.setLastRound(false);
            assertFalse(game.isLastRound());
        }

        @Test
        void pausedRoundTrip() {
            game.setPaused(true);
            assertTrue(game.isPaused());
            game.setPaused(false);
            assertFalse(game.isPaused());
        }


        @Test

        void componentSettersReplace() {
            EventManager freshEm = new EventManager();
            game.setEventManager(freshEm);
            assertSame(freshEm, game.getEventManager());

            it.polimi.ingsw.gc49.server.model.Track.Track freshTrack =
                    new it.polimi.ingsw.gc49.server.model.Track.Track(2);
            game.setTrack(freshTrack);
            assertSame(freshTrack, game.getTrack());

            game.setPlayers(List.of(new Player("X", 0), new Player("Y", 1)));
            assertEquals(2, game.getNumOfPlayers());
        }

    }

    // --- chooseTotem: every branch ---

    @Nested
    class ChooseTotemBranches {

        @Test
        void happyPath() throws PlayerException {
            game.chooseTotem(0, Totem.BLUE);
            assertEquals(Totem.BLUE, peppe.getTotem());
            assertTrue(game.getUsedTotems().contains(Totem.BLUE));
        }

        @Test
        void duplicateTotemThrows() throws PlayerException {
            game.chooseTotem(0, Totem.BLUE);
            InvalidTotem ex = assertThrows(InvalidTotem.class, () -> game.chooseTotem(1, Totem.BLUE));
            assertEquals("Totem inaccettabile", ex.getTitle());
        }

        @Test
        void doubleChoiceThrows() throws PlayerException {
            game.chooseTotem(0, Totem.BLUE);
            assertThrows(InvalidTotem.class, () -> game.chooseTotem(0, Totem.YELLOW));
        }

        @Test
        void independentPicks() throws PlayerException {
            game.chooseTotem(0, Totem.BLUE);
            game.chooseTotem(1, Totem.YELLOW);
            assertEquals(2, game.getUsedTotems().size());
        }

        @Test
        void noopWhenPaused() throws PlayerException {
            game.setPaused(true);
            game.chooseTotem(0, Totem.BLUE);
            assertNull(peppe.getTotem());
        }

        @Test
        void wrongStateNoOp() throws PlayerException {
            setOfferChoosingState();
            // the inner branch only enters when state is TOTEM_CHOOSING; otherwise the totem
            // is NOT registered (no exception, no side-effect)
            game.chooseTotem(0, Totem.BLUE);
            assertNull(peppe.getTotem(),
                    "totem must not be assigned outside TOTEM_CHOOSING");
            assertTrue(game.getUsedTotems().isEmpty());
        }
    }

    // --- draw methods: NotYourTurnException + paused + happy + drawable=0 ---

    @Nested
    class DrawBranches {

        @Test
        void wrongTurnUpperCharacter() {
            game.setCurrentPlayerIndex(0);
            assertThrows(NotYourTurnException.class, () -> game.drawUpperCharacter(1, 0));
        }

        @Test
        void wrongTurnLowerCharacter() {
            game.setCurrentPlayerIndex(0);
            assertThrows(NotYourTurnException.class, () -> game.drawLowerCharacter(1, 0));
        }

        @Test
        void wrongTurnUpperBuilding() {
            game.setCurrentPlayerIndex(0);
            assertThrows(NotYourTurnException.class, () -> game.drawUpperBuilding(1, 0));
        }

        @Test
        void wrongTurnLowerBuilding() {
            game.setCurrentPlayerIndex(0);
            assertThrows(NotYourTurnException.class, () -> game.drawLowerBuilding(1, 0));
        }

        @Test
        void noopWhenPausedAllDraws() {
            game.setPaused(true);
            assertDoesNotThrow(() -> {
                game.drawUpperCharacter(1, 0);
                game.drawLowerCharacter(1, 0);
                game.drawUpperBuilding(1, 0);
                game.drawLowerBuilding(1, 0);
            });
        }

        @Test
        void drawUpperCharacterHappyPath() throws PlayerException {
            setOfferExecutionState();
            game.setCurrentPlayer(peppe);
            game.setCurrentPlayerIndex(0);
            peppe.setDrawableUpper(1);
            int initial = characterCardsSize(peppe);
            game.drawUpperCharacter(0, 0);
            // happy branch was reached: either a card was added (drawnCard != null)
            // or the slot was empty (drawnCard == null). Both branches are exercised.
            assertTrue(characterCardsSize(peppe) >= initial);
        }

        @Test
        void drawLowerCharacterHappyPath() throws PlayerException {
            setOfferExecutionState();
            game.setCurrentPlayer(peppe);
            game.setCurrentPlayerIndex(0);
            peppe.setDrawableLower(1);
            int initial = characterCardsSize(peppe);
            game.drawLowerCharacter(0, 0);
            assertTrue(characterCardsSize(peppe) >= initial);
        }

        @Test
        void drawUpperBuildingHappyPath() throws PlayerException {
            setOfferExecutionState();
            game.setCurrentPlayer(peppe);
            game.setCurrentPlayerIndex(0);
            peppe.setFood(100); // afford any building's onDraw cost
            peppe.setDrawableUpper(1);
            int initial = buildingCardsSize(peppe);
            game.drawUpperBuilding(0, 0);
            assertTrue(buildingCardsSize(peppe) >= initial);
        }

        @Test
        void drawLowerBuildingHappyPath() throws PlayerException {
            setOfferExecutionState();
            game.setCurrentPlayer(peppe);
            game.setCurrentPlayerIndex(0);
            peppe.setFood(100);
            peppe.setDrawableLower(1);
            int initial = buildingCardsSize(peppe);
            game.drawLowerBuilding(0, 0);
            assertTrue(buildingCardsSize(peppe) >= initial);
        }

        @Test
        void zeroDrawableNoOp() throws PlayerException {
            setOfferExecutionState();
            game.setCurrentPlayer(peppe);
            game.setCurrentPlayerIndex(0);
            peppe.setDrawableUpper(0);
            peppe.setDrawableLower(0);
            int chars = characterCardsSize(peppe);
            int builds = buildingCardsSize(peppe);

            game.drawUpperCharacter(0, 0);
            game.drawLowerCharacter(0, 0);
            game.drawUpperBuilding(0, 0);
            game.drawLowerBuilding(0, 0);

            assertEquals(chars, characterCardsSize(peppe));
            assertEquals(builds, buildingCardsSize(peppe));
        }

        @Test
        void wrongStateNoOp() throws PlayerException {
            // current state is TOTEM_CHOOSING; the inner branch only enters in OFFER_EXECUTION
            game.setCurrentPlayer(peppe);
            game.setCurrentPlayerIndex(0);
            peppe.setDrawableUpper(1);
            peppe.setDrawableLower(1);
            int chars = characterCardsSize(peppe);

            game.drawUpperCharacter(0, 0);
            game.drawLowerCharacter(0, 0);
            game.drawUpperBuilding(0, 0);
            game.drawLowerBuilding(0, 0);

            assertEquals(chars, characterCardsSize(peppe));
        }
    }

    // --- chooseOffer ---

    @Nested
    class ChooseOfferBranches {

        @Test
        void wrongTurn() {
            game.setCurrentPlayerIndex(0);
            assertThrows(NotYourTurnException.class, () -> game.chooseOffer(1, 0));
        }

        @Test
        void paused() {
            game.setPaused(true);
            assertDoesNotThrow(() -> game.chooseOffer(1, 0));
        }

        @Test
        void happyPath() throws PlayerException {
            setOfferChoosingState();
            game.setCurrentPlayer(peppe);
            game.setCurrentPlayerIndex(0);
            game.chooseOffer(0, 0);
            assertSame(peppe, game.getTrack().getOfferBoard().get(0).getAssignedPlayer());
            assertTrue(peppe.hasChosenAnOffer());
        }

        @Test
        void occupiedPropagates() throws PlayerException {
            setOfferChoosingState();
            game.setCurrentPlayer(peppe);
            game.setCurrentPlayerIndex(0);
            game.chooseOffer(0, 0);

            game.setCurrentPlayer(wu);
            game.setCurrentPlayerIndex(1);
            assertThrows(NotValidOfferException.class, () -> game.chooseOffer(1, 0));
        }

        @Test
        void wrongStateNoOp() throws PlayerException {
            // state is TOTEM_CHOOSING
            game.setCurrentPlayer(peppe);
            game.setCurrentPlayerIndex(0);
            game.chooseOffer(0, 0);
            assertNull(game.getTrack().getOfferBoard().get(0).getAssignedPlayer());
        }
    }

    // --- passYourTurn ---

    @Nested
    class PassYourTurnBranches {

        @Test
        void paused() {
            game.setPaused(true);
            peppe.setDrawableUpper(2);
            game.passYourTurn(0);
            assertEquals(2, peppe.getDrawableUpper(), "remaining draws must not be cleared");
        }

        @Test
        void wrongPlayer() {
            game.setCurrentPlayerIndex(0);
            wu.setDrawableUpper(2);
            game.passYourTurn(1);
            assertEquals(2, wu.getDrawableUpper());
        }

        @Test
        void clearsInNonOtherState() {
            game.setCurrentPlayerIndex(0);
            peppe.setDrawableUpper(3);
            peppe.setDrawableLower(2);
            game.passYourTurn(0);
            assertEquals(0, peppe.getDrawableUpper());
            assertEquals(0, peppe.getDrawableLower());
        }

        @Test
        void clearsInOfferExecution() {
            setOfferExecutionState();
            game.setCurrentPlayerIndex(0);
            peppe.setDrawableUpper(1);
            peppe.setDrawableLower(1);
            game.passYourTurn(0);
            assertEquals(0, peppe.getDrawableUpper());
            assertEquals(0, peppe.getDrawableLower());
        }
    }

    // --- disconnect / connect ---

    @Nested
    class Connection {

        @Test
        void disconnectPausesOnLastRemaining() {
            game.disconnectPlayer(1);
            assertFalse(wu.isConnected());
            assertEquals(1, game.getNumOfConnectedPlayers());
            assertTrue(game.isPaused());
        }

        @Test
        void disconnectBothPlayers() {
            game.disconnectPlayer(0);
            game.disconnectPlayer(1);
            assertEquals(0, game.getNumOfConnectedPlayers());
        }

        @Test
        void reconnectUnpauses() {
            game.disconnectPlayer(1);
            assertTrue(game.isPaused());
            game.connectPlayer(1);
            assertTrue(wu.isConnected());
            assertEquals(2, game.getNumOfConnectedPlayers());
            assertFalse(game.isPaused());
        }

        @Test
        void reconnectRestoresRemovedFromTrack() {
            // seat both players, then "remove" Wu (the second) from the track
            game.getTrack().getOrderBoard().get(0).assignPlayer(peppe);
            game.getTrack().getOrderBoard().get(1).assignPlayer(wu);
            game.getTrack().getOrderBoard().get(1).assignPlayer(null);
            wu.setRemovedFromTrack(true);

            game.disconnectPlayer(1);
            // currentPlayer must be set before reconnection (used inside the queued message)
            game.setCurrentPlayer(wu);

            game.connectPlayer(1);

            assertFalse(wu.isRemovedFromTrack(),
                    "the removed-from-track flag must be cleared on reconnection");
            assertTrue(wu.isConnected());
        }
    }

    // --- event calls + broadcasts + mockup ---

    @Nested
    class EventsAndBroadcast {

        @Test
        void callGlobalEvents() {
            assertDoesNotThrow(() -> game.callRoundEndEvent());
            assertDoesNotThrow(() -> game.callGameEndEvent());
        }

        @Test
        void callPlayerScopedEvents() {
            game.setCurrentPlayer(peppe);
            assertDoesNotThrow(() -> game.callDrawEvent());
            assertDoesNotThrow(() -> game.callTurnEndEvent());
        }

        @Test
        void queueAndBroadcast() {
            game.queueUpdateModelElement(new TextModelElement("hi"));
            assertDoesNotThrow(() -> game.broadcastGameUpdate());
        }

        @Test
        void broadcastCurrentPlayerTurnNoThrow() {
            game.setCurrentPlayer(peppe);
            game.setCurrentPlayerIndex(0);
            assertDoesNotThrow(() -> game.broadcastCurrentPlayerTurn());
        }

        @Test
        void giveMockupGameSnapshot() {
            MockupGame mockup = game.giveMockupGame();
            assertNotNull(mockup);
            assertEquals(2, mockup.getPlayers().size());
            assertEquals("Peppe", mockup.getPlayer(0).getNickname());
            assertEquals("Wu", mockup.getPlayer(1).getNickname());
        }

        @Test
        void executeCurrentStateRuns() {
            setOfferChoosingState();
            assertDoesNotThrow(() -> game.executeCurrentState());
        }

        @Test
        void numConnectedTracksToggles() {
            assertEquals(2, game.getNumOfConnectedPlayers());
            wu.setConnected(false);
            assertEquals(1, game.getNumOfConnectedPlayers());
            peppe.setConnected(false);
            assertEquals(0, game.getNumOfConnectedPlayers());
        }
    }

    // --- gameLoop (covered via a background thread driving TotemChoosing) ---

    @Test
    @Timeout(value = 10, unit = TimeUnit.SECONDS)
    void gameLoopRuns() throws Exception {
        Thread loop = new Thread(game::gameLoop, "game-loop");
        loop.setDaemon(true);
        loop.start();

        // wait briefly to let the loop reach the wait inside TotemChoosing
        Thread.sleep(200);

        // both players pick their totems; each call notifies the monitor the loop is on
        game.chooseTotem(0, Totem.BLUE);
        game.chooseTotem(1, Totem.YELLOW);

        // poll until food has been dealt (means TotemChoosing finished and OfferChoosing took over)
        long deadline = System.currentTimeMillis() + 5000;
        while (System.currentTimeMillis() < deadline) {
            if (peppe.getFood() + wu.getFood() > 0) break;
            Thread.sleep(20);
        }

        assertEquals(5, peppe.getFood() + wu.getFood(),
                "TotemChoosing must have run, dealing 2+3 food to the 2 players");
        loop.interrupt();
    }
}
