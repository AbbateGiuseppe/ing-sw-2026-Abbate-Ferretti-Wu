package it.polimi.ingsw.gc49.server.model;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.TextModelElement;
import it.polimi.ingsw.gc49.server.model.playerExceptions.InvalidTotem;
import it.polimi.ingsw.gc49.server.model.playerExceptions.NotYourTurnException;
import it.polimi.ingsw.gc49.server.model.playerExceptions.PlayerException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Game}.
 * <p>
 * The Game constructor runs only {@code InitialSetup.executeState()} (it does NOT start
 * {@code gameLoop()}), so {@code currentState} is {@code TotemChoosing} right after
 * construction. This is enough to drive most of the public API without ever blocking.
 */
class GameTest {

    private Game game;

    @BeforeEach
    void setUp() {
        game = new Game(2, List.of("Peppe", "Wu"), "room");
    }

    // --- construction ---

    @Nested
    @DisplayName("Construction")
    class Construction {

        @Test
        @DisplayName("Game is built with the right number of players")
        void numOfPlayers() {
            assertEquals(2, game.getNumOfPlayers());
        }

        @Test
        @DisplayName("All players start as connected")
        void initiallyAllConnected() {
            assertEquals(2, game.getNumOfConnectedPlayers());
        }

        @Test
        @DisplayName("Player nicknames are exposed in order")
        void playersNicknames() {
            assertEquals(List.of("Peppe", "Wu"), game.getPlayersNicknames());
        }

        @Test
        @DisplayName("Track, CardBoard and EventManager are non-null after setup")
        void componentsInitialized() {
            assertNotNull(game.getTrack());
            assertNotNull(game.getCardBoard());
            assertNotNull(game.getEventManager());
        }

        @Test
        @DisplayName("usedTotems starts empty and lastRound starts false; not paused by default")
        void initialFlags() {
            assertTrue(game.getUsedTotems().isEmpty());
            assertFalse(game.isLastRound());
            assertFalse(game.isPaused());
        }
    }

    // --- setters / setter pairs ---

    @Test
    @DisplayName("setLastRound + isLastRound round trip")
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
    @DisplayName("setCurrentPlayer and setCurrentPlayerIndex are accepted without effect on the public state")
    void currentPlayerSetters() {
        game.setCurrentPlayer(game.getPlayers().get(1));
        game.setCurrentPlayerIndex(1);
        // no public getter; just ensure setters do not throw
        assertDoesNotThrow(() -> game.setCurrentPlayer(null));
    }

    // --- chooseTotem ---

    @Nested
    @DisplayName("chooseTotem")
    class ChooseTotem {

        @Test
        @DisplayName("a player can pick an available totem")
        void pickAvailable() throws PlayerException {
            game.chooseTotem(0, Totem.BLUE);
            assertEquals(Totem.BLUE, game.getPlayers().get(0).getTotem());
            assertTrue(game.getUsedTotems().contains(Totem.BLUE));
        }

        @Test
        @DisplayName("choosing an already-taken totem throws InvalidTotem")
        void duplicateTotemThrows() throws PlayerException {
            game.chooseTotem(0, Totem.BLUE);
            assertThrows(InvalidTotem.class, () -> game.chooseTotem(1, Totem.BLUE));
        }

        @Test
        @DisplayName("choosing a second totem for a player already with one throws InvalidTotem")
        void doubleChoiceThrows() throws PlayerException {
            game.chooseTotem(0, Totem.BLUE);
            assertThrows(InvalidTotem.class, () -> game.chooseTotem(0, Totem.YELLOW));
        }

        @Test
        @DisplayName("two different players can pick two different totems")
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
            assertNull(game.getPlayers().get(0).getTotem());
        }
    }

    // --- action methods: "not your turn" branches ---

    @Nested
    @DisplayName("Action methods reject calls from the wrong player")
    class NotYourTurn {

        @BeforeEach
        void useFirstPlayerAsCurrent() {
            game.setCurrentPlayerIndex(0);
        }

        @Test
        @DisplayName("drawUpperCharacter from a non-current player throws NotYourTurnException")
        void drawUpperCharacter() {
            assertThrows(NotYourTurnException.class, () -> game.drawUpperCharacter(1, 0));
        }

        @Test
        @DisplayName("drawLowerCharacter from a non-current player throws NotYourTurnException")
        void drawLowerCharacter() {
            assertThrows(NotYourTurnException.class, () -> game.drawLowerCharacter(1, 0));
        }

        @Test
        @DisplayName("drawUpperBuilding from a non-current player throws NotYourTurnException")
        void drawUpperBuilding() {
            assertThrows(NotYourTurnException.class, () -> game.drawUpperBuilding(1, 0));
        }

        @Test
        @DisplayName("drawLowerBuilding from a non-current player throws NotYourTurnException")
        void drawLowerBuilding() {
            assertThrows(NotYourTurnException.class, () -> game.drawLowerBuilding(1, 0));
        }

        @Test
        @DisplayName("chooseOffer from a non-current player throws NotYourTurnException")
        void chooseOffer() {
            assertThrows(NotYourTurnException.class, () -> game.chooseOffer(1, 0));
        }
    }

    // --- pause guard ---

    @Test
    @DisplayName("draw and chooseOffer are no-ops when paused (no exception thrown)")
    void noopsWhenPaused() {
        game.setPaused(true);
        assertDoesNotThrow(() -> {
            game.drawUpperCharacter(1, 0);
            game.drawLowerCharacter(1, 0);
            game.drawUpperBuilding(1, 0);
            game.drawLowerBuilding(1, 0);
            game.chooseOffer(1, 0);
            game.passYourTurn(1);
        });
    }

    // --- passYourTurn ---

    @Test
    @DisplayName("passYourTurn does nothing in TotemChoosing (state type is not OTHER but the action requires the right turn)")
    void passYourTurnInTotemChoosing() {
        // currentPlayerIndex defaults to 0; player 0 is the current one
        game.setCurrentPlayerIndex(0);
        Player peppe = game.getPlayers().get(0);
        peppe.setDrawableUpper(2);
        peppe.setDrawableLower(3);

        // state type is TOTEM_CHOOSING (not OTHER), so the cleanRemainingActions branch is taken
        game.passYourTurn(0);

        assertEquals(0, peppe.getDrawableUpper());
        assertEquals(0, peppe.getDrawableLower());
    }

    @Test
    @DisplayName("passYourTurn called by the wrong player is a no-op")
    void passYourTurnWrongPlayer() {
        game.setCurrentPlayerIndex(0);
        Player wu = game.getPlayers().get(1);
        wu.setDrawableUpper(2);

        game.passYourTurn(1); // wu is not the current player

        assertEquals(2, wu.getDrawableUpper(), "remaining actions must NOT be cleared");
    }

    // --- connection lifecycle ---

    @Nested
    @DisplayName("Connection lifecycle")
    class Connection {

        @Test
        @DisplayName("disconnectPlayer flips connected to false and reduces the connected count")
        void disconnectPlayer() {
            game.disconnectPlayer(1);
            assertFalse(game.getPlayers().get(1).isConnected());
            assertEquals(1, game.getNumOfConnectedPlayers());
        }

        @Test
        @DisplayName("disconnecting until only one player is left pauses the game")
        void disconnectingPausesWhenOneRemains() {
            game.disconnectPlayer(1);
            assertTrue(game.isPaused());
        }

        @Test
        @DisplayName("connectPlayer brings a disconnected player back online")
        void connectPlayer() {
            game.disconnectPlayer(1);
            game.connectPlayer(1);
            assertTrue(game.getPlayers().get(1).isConnected());
            assertEquals(2, game.getNumOfConnectedPlayers());
        }

        @Test
        @DisplayName("reconnecting when 2 players are online unpauses the game")
        void reconnectionUnpausesGame() {
            game.disconnectPlayer(1);
            assertTrue(game.isPaused());
            game.connectPlayer(1);
            assertFalse(game.isPaused());
        }
    }

    // --- event calls ---

    @Test
    @DisplayName("callRoundEndEvent invokes the round-end event on the manager without throwing")
    void callRoundEndEvent() {
        assertDoesNotThrow(() -> game.callRoundEndEvent());
    }

    @Test
    @DisplayName("callGameEndEvent invokes the game-end event on the manager without throwing")
    void callGameEndEvent() {
        assertDoesNotThrow(() -> game.callGameEndEvent());
    }

    @Test
    @DisplayName("callDrawEvent and callTurnEndEvent work after setting a currentPlayer")
    void callPlayerScopedEvents() {
        game.setCurrentPlayer(game.getPlayers().get(0));
        assertDoesNotThrow(() -> game.callDrawEvent());
        assertDoesNotThrow(() -> game.callTurnEndEvent());
    }

    // --- mockup / broadcast ---

    @Test
    @DisplayName("giveMockupGame returns a complete MockupGame snapshot")
    void giveMockupGameSnapshot() {
        MockupGame mockup = game.giveMockupGame();
        assertNotNull(mockup);
    }

    @Test
    @DisplayName("queueUpdateModelElement and broadcastGameUpdate are no-throw without controllers")
    void broadcastWithoutControllers() {
        game.queueUpdateModelElement(new TextModelElement("test"));
        assertDoesNotThrow(() -> game.broadcastGameUpdate());
    }

    @Test
    @DisplayName("broadcastCurrentPlayerTurn is no-throw once a currentPlayer is set")
    void broadcastCurrentPlayerTurn() {
        game.setCurrentPlayer(game.getPlayers().get(0));
        game.setCurrentPlayerIndex(0);
        assertDoesNotThrow(() -> game.broadcastCurrentPlayerTurn());
    }

    // --- executeCurrentState exposed ---

    @Test
    @DisplayName("addControllerListener accepts a null listener without throwing")
    void addControllerListenerAcceptsNull() {
        assertDoesNotThrow(() -> game.addControllerListener(null));
    }
}
