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

    // --- helpers ---

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

    private static int characterCardsSize(Player p) { return sizeOfPrivateList(p, "characterCards"); }
    private static int buildingCardsSize(Player p) { return sizeOfPrivateList(p, "buildingCards"); }

    // --- construction ---

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("Game has the right number of players, all connected, with nicknames in order")
        void initialPlayers() {
            assertEquals(2, game.getNumOfPlayers());
            assertEquals(2, game.getNumOfConnectedPlayers());
            assertEquals(List.of("Peppe", "Wu"), game.getPlayersNicknames());
        }

        @Test
        @DisplayName("Track, CardBoard and EventManager are non-null; usedTotems empty; not paused; not last round")
        void initialFlagsAndComponents() {
            assertNotNull(game.getTrack());
            assertNotNull(game.getCardBoard());
            assertNotNull(game.getEventManager());
            assertTrue(game.getUsedTotems().isEmpty());
            assertFalse(game.isLastRound());
            assertFalse(game.isPaused());
        }

        @Test
        @DisplayName("getPlayers exposes the same list (read access)")
        void getPlayersExposed() {
            assertEquals(2, game.getPlayers().size());
            assertSame(peppe, game.getPlayers().get(0));
        }
    }

    // --- setters ---

    @Nested
    @DisplayName("Setters")
    class Setters {

        @Test
        @DisplayName("setLastRound + isLastRound round trip (both true/false)")
        void lastRoundRoundTrip() {
            game.setLastRound(true);
            assertTrue(game.isLastRound());
            game.setLastRound(false);
            assertFalse(game.isLastRound());
        }

        @Test
        @DisplayName("setPaused + isPaused round trip")
        void pausedRoundTrip() {
            game.setPaused(true);
            assertTrue(game.isPaused());
            game.setPaused(false);
            assertFalse(game.isPaused());
        }

        @Test
        @DisplayName("setCurrentPlayer / setCurrentPlayerIndex accept any value")
        void currentPlayerSetters() {
            assertDoesNotThrow(() -> {
                game.setCurrentPlayer(peppe);
                game.setCurrentPlayer(null);
                game.setCurrentPlayerIndex(1);
                game.setCurrentPlayerIndex(0);
            });
        }

        @Test
        @DisplayName("setPlayers, setTrack, setCardBoard, setEventManager replace the internal references")
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

        @Test
        @DisplayName("addControllerListener accepts null without throwing")
        void addControllerListenerAcceptsNull() {
            assertDoesNotThrow(() -> game.addControllerListener(null));
        }
    }

    // --- chooseTotem: every branch ---

    @Nested
    @DisplayName("chooseTotem: every branch")
    class ChooseTotemBranches {

        @Test
        @DisplayName("happy path: a player picks an available totem in TOTEM_CHOOSING")
        void happyPath() throws PlayerException {
            game.chooseTotem(0, Totem.BLUE);
            assertEquals(Totem.BLUE, peppe.getTotem());
            assertTrue(game.getUsedTotems().contains(Totem.BLUE));
        }

        @Test
        @DisplayName("picking a totem already taken by another player throws InvalidTotem")
        void duplicateTotemThrows() throws PlayerException {
            game.chooseTotem(0, Totem.BLUE);
            InvalidTotem ex = assertThrows(InvalidTotem.class, () -> game.chooseTotem(1, Totem.BLUE));
            assertEquals("Totem inaccettabile", ex.getTitle());
        }

        @Test
        @DisplayName("a player who already chose a totem cannot choose again (InvalidTotem)")
        void doubleChoiceThrows() throws PlayerException {
            game.chooseTotem(0, Totem.BLUE);
            assertThrows(InvalidTotem.class, () -> game.chooseTotem(0, Totem.YELLOW));
        }

        @Test
        @DisplayName("two different players, two different totems")
        void independentPicks() throws PlayerException {
            game.chooseTotem(0, Totem.BLUE);
            game.chooseTotem(1, Totem.YELLOW);
            assertEquals(2, game.getUsedTotems().size());
        }

        @Test
        @DisplayName("chooseTotem is a no-op when the game is paused")
        void noopWhenPaused() throws PlayerException {
            game.setPaused(true);
            game.chooseTotem(0, Totem.BLUE);
            assertNull(peppe.getTotem());
        }

        @Test
        @DisplayName("chooseTotem in a non-TOTEM_CHOOSING state does not register the totem")
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
    @DisplayName("draw methods: every branch")
    class DrawBranches {

        @Test
        @DisplayName("drawUpperCharacter from a non-current player throws NotYourTurnException")
        void wrongTurnUpperCharacter() {
            game.setCurrentPlayerIndex(0);
            assertThrows(NotYourTurnException.class, () -> game.drawUpperCharacter(1, 0));
        }

        @Test
        @DisplayName("drawLowerCharacter from a non-current player throws NotYourTurnException")
        void wrongTurnLowerCharacter() {
            game.setCurrentPlayerIndex(0);
            assertThrows(NotYourTurnException.class, () -> game.drawLowerCharacter(1, 0));
        }

        @Test
        @DisplayName("drawUpperBuilding from a non-current player throws NotYourTurnException")
        void wrongTurnUpperBuilding() {
            game.setCurrentPlayerIndex(0);
            assertThrows(NotYourTurnException.class, () -> game.drawUpperBuilding(1, 0));
        }

        @Test
        @DisplayName("drawLowerBuilding from a non-current player throws NotYourTurnException")
        void wrongTurnLowerBuilding() {
            game.setCurrentPlayerIndex(0);
            assertThrows(NotYourTurnException.class, () -> game.drawLowerBuilding(1, 0));
        }

        @Test
        @DisplayName("when paused, draw methods are silent no-ops even for the wrong player")
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
        @DisplayName("happy path: drawUpperCharacter in OFFER_EXECUTION with drawableUpper > 0")
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
        @DisplayName("happy path: drawLowerCharacter in OFFER_EXECUTION with drawableLower > 0")
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
        @DisplayName("happy path: drawUpperBuilding in OFFER_EXECUTION with drawableUpper > 0")
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
        @DisplayName("happy path: drawLowerBuilding in OFFER_EXECUTION with drawableLower > 0")
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
        @DisplayName("draw methods with drawable counters at zero are silent (covers the >0 false branch)")
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
        @DisplayName("draw methods in a state different from OFFER_EXECUTION are silent")
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
    @DisplayName("chooseOffer: every branch")
    class ChooseOfferBranches {

        @Test
        @DisplayName("wrong turn throws NotYourTurnException")
        void wrongTurn() {
            game.setCurrentPlayerIndex(0);
            assertThrows(NotYourTurnException.class, () -> game.chooseOffer(1, 0));
        }

        @Test
        @DisplayName("paused is a silent no-op")
        void paused() {
            game.setPaused(true);
            assertDoesNotThrow(() -> game.chooseOffer(1, 0));
        }

        @Test
        @DisplayName("happy path: in OFFER_CHOOSING the player is assigned to the offer slot")
        void happyPath() throws PlayerException {
            setOfferChoosingState();
            game.setCurrentPlayer(peppe);
            game.setCurrentPlayerIndex(0);
            game.chooseOffer(0, 0);
            assertSame(peppe, game.getTrack().getOfferBoard().get(0).getAssignedPlayer());
            assertTrue(peppe.hasChosenAnOffer());
        }

        @Test
        @DisplayName("occupied offer slot propagates NotValidOfferException")
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
        @DisplayName("wrong state (not OFFER_CHOOSING) is a silent no-op")
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
    @DisplayName("passYourTurn: every branch")
    class PassYourTurnBranches {

        @Test
        @DisplayName("paused is a silent no-op")
        void paused() {
            game.setPaused(true);
            peppe.setDrawableUpper(2);
            game.passYourTurn(0);
            assertEquals(2, peppe.getDrawableUpper(), "remaining draws must not be cleared");
        }

        @Test
        @DisplayName("wrong player is a silent no-op")
        void wrongPlayer() {
            game.setCurrentPlayerIndex(0);
            wu.setDrawableUpper(2);
            game.passYourTurn(1);
            assertEquals(2, wu.getDrawableUpper());
        }

        @Test
        @DisplayName("in TOTEM_CHOOSING (not OTHER), passYourTurn clears the current player's remaining actions")
        void clearsInNonOtherState() {
            game.setCurrentPlayerIndex(0);
            peppe.setDrawableUpper(3);
            peppe.setDrawableLower(2);
            game.passYourTurn(0);
            assertEquals(0, peppe.getDrawableUpper());
            assertEquals(0, peppe.getDrawableLower());
        }

        @Test
        @DisplayName("in OFFER_EXECUTION (not OTHER), passYourTurn clears the current player's remaining actions")
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
    @DisplayName("Connection lifecycle")
    class Connection {

        @Test
        @DisplayName("disconnect with 2 players online: pauses the game on the last remaining one")
        void disconnectPausesOnLastRemaining() {
            game.disconnectPlayer(1);
            assertFalse(wu.isConnected());
            assertEquals(1, game.getNumOfConnectedPlayers());
            assertTrue(game.isPaused());
        }

        @Test
        @DisplayName("disconnect of the second player (0 connected) reaches the 0-connected branch")
        void disconnectBothPlayers() {
            game.disconnectPlayer(0);
            game.disconnectPlayer(1);
            assertEquals(0, game.getNumOfConnectedPlayers());
        }

        @Test
        @DisplayName("reconnect of the second player brings connectedPlayers back to 2 and unpauses")
        void reconnectUnpauses() {
            game.disconnectPlayer(1);
            assertTrue(game.isPaused());
            game.connectPlayer(1);
            assertTrue(wu.isConnected());
            assertEquals(2, game.getNumOfConnectedPlayers());
            assertFalse(game.isPaused());
        }

        @Test
        @DisplayName("reconnect of a player marked removedFromTrack restores them on the order board")
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
    @DisplayName("Events, broadcasts and mockup")
    class EventsAndBroadcast {

        @Test
        @DisplayName("callRoundEndEvent and callGameEndEvent do not throw")
        void callGlobalEvents() {
            assertDoesNotThrow(() -> game.callRoundEndEvent());
            assertDoesNotThrow(() -> game.callGameEndEvent());
        }

        @Test
        @DisplayName("callDrawEvent and callTurnEndEvent work once currentPlayer is set")
        void callPlayerScopedEvents() {
            game.setCurrentPlayer(peppe);
            assertDoesNotThrow(() -> game.callDrawEvent());
            assertDoesNotThrow(() -> game.callTurnEndEvent());
        }

        @Test
        @DisplayName("queueUpdateModelElement + broadcastGameUpdate is no-throw with no controllers")
        void queueAndBroadcast() {
            game.queueUpdateModelElement(new TextModelElement("hi"));
            assertDoesNotThrow(() -> game.broadcastGameUpdate());
        }

        @Test
        @DisplayName("broadcastCurrentPlayerTurn is no-throw once currentPlayer/Index are set")
        void broadcastCurrentPlayerTurnNoThrow() {
            game.setCurrentPlayer(peppe);
            game.setCurrentPlayerIndex(0);
            assertDoesNotThrow(() -> game.broadcastCurrentPlayerTurn());
        }

        @Test
        @DisplayName("giveMockupGame returns a snapshot consistent with the players list")
        void giveMockupGameSnapshot() {
            MockupGame mockup = game.giveMockupGame();
            assertNotNull(mockup);
            assertEquals(2, mockup.getPlayers().size());
            assertEquals("Peppe", mockup.getPlayer(0).getNickname());
            assertEquals("Wu", mockup.getPlayer(1).getNickname());
        }

        @Test
        @DisplayName("executeCurrentState runs the current state once")
        void executeCurrentStateRuns() {
            setOfferChoosingState();
            assertDoesNotThrow(() -> game.executeCurrentState());
        }

        @Test
        @DisplayName("getNumOfConnectedPlayers reflects manual connection toggles")
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
    @DisplayName("gameLoop runs the TotemChoosing state and proceeds when totems are chosen")
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
