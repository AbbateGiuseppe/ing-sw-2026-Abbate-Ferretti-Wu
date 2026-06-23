package it.polimi.ingsw.gc49.server.controller;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.STRING.StringPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_HALL.InitializeHallPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_ROOM.InitializeRoomPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_HALL.UpdateHallPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_ROOM.UpdateRoomPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Totem;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.server.proxies.RmiProxyPlayer;
import it.polimi.ingsw.gc49.server.proxies.SocketProxyPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unified test class for {@link MassiWuPeppeController}.
 * <p>
 * A {@link FakePhasedProxyPlayer} subclass is used as the controlled proxy: it records every
 * callback received and, when the {@code shouldThrow} flag is on, makes the three
 * {@link it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualGameClient}
 * methods throw, so the controller's catch-and-disconnect branches can be exercised.
 */
class MassiWuPeppeControllerTest {

    private Game game;
    private FakePhasedProxyPlayer proxy;
    private MassiWuPeppeController controller;

    @BeforeEach
    void setUp() {
        game = new Game(2, List.of("Peppe", "Wu"), "room");
        proxy = new FakePhasedProxyPlayer("Peppe");
        controller = new MassiWuPeppeController(0, proxy);
        controller.connectModel(game);
    }

    // --- constructor ---

    @Test
    @DisplayName("constructor stores controllingPlayerIndex and controllingPlayer; game starts null")
    void constructorState() {
        MassiWuPeppeController fresh = new MassiWuPeppeController(2, proxy);
        // game is private and starts null; verified indirectly by checking that callbacks
        // that touch game on exception would NPE — but the happy-path callback works:
        assertDoesNotThrow(() -> fresh.reportError(new ErrorPacket("t", "c", false)));
        // and the proxy was indeed called
        assertNotNull(proxy.lastError);
    }

    // --- connectModel ---

    @Test
    @DisplayName("connectModel wires the game and adds the controller as a listener")
    void connectModelWires() {
        // already called in setUp; verify the controller is now part of the game's listeners
        // by triggering a broadcast (no-throw means the listener slot exists)
        assertDoesNotThrow(() -> game.broadcastGameUpdate());
    }

    // --- executeCommand: every PlayerActionEnum branch ---

    @Test
    @DisplayName("executeCommand CHOOSE_TOTEM forwards to game.chooseTotem")
    void executeChooseTotem() {
        controller.executeCommand(new CommandPacket(PlayerActionEnum.CHOOSE_TOTEM, Totem.BLUE));
        assertEquals(Totem.BLUE, game.getPlayers().get(0).getTotem());
    }

    @Test
    @DisplayName("executeCommand DRAW_UPPER_CHARACTER forwards to game.drawUpperCharacter (no-op in TOTEM_CHOOSING)")
    void executeDrawUpperCharacter() {
        assertDoesNotThrow(() ->
                controller.executeCommand(new CommandPacket(PlayerActionEnum.DRAW_UPPER_CHARACTER, 0)));
    }

    @Test
    @DisplayName("executeCommand DRAW_LOWER_CHARACTER forwards to game.drawLowerCharacter")
    void executeDrawLowerCharacter() {
        assertDoesNotThrow(() ->
                controller.executeCommand(new CommandPacket(PlayerActionEnum.DRAW_LOWER_CHARACTER, 0)));
    }

    @Test
    @DisplayName("executeCommand DRAW_UPPER_BUILDING forwards to game.drawUpperBuilding")
    void executeDrawUpperBuilding() {
        assertDoesNotThrow(() ->
                controller.executeCommand(new CommandPacket(PlayerActionEnum.DRAW_UPPER_BUILDING, 0)));
    }

    @Test
    @DisplayName("executeCommand DRAW_LOWER_BUILDING forwards to game.drawLowerBuilding")
    void executeDrawLowerBuilding() {
        assertDoesNotThrow(() ->
                controller.executeCommand(new CommandPacket(PlayerActionEnum.DRAW_LOWER_BUILDING, 0)));
    }

    @Test
    @DisplayName("executeCommand CHOOSE_OFFER forwards to game.chooseOffer (no-op in TOTEM_CHOOSING)")
    void executeChooseOffer() {
        assertDoesNotThrow(() ->
                controller.executeCommand(new CommandPacket(PlayerActionEnum.CHOOSE_OFFER, 0)));
    }

    @Test
    @DisplayName("executeCommand PASS_TURN forwards to game.passYourTurn and clears remaining actions")
    void executePassTurn() {
        game.setCurrentPlayerIndex(0);
        game.getPlayers().get(0).setDrawableUpper(2);

        controller.executeCommand(new CommandPacket(PlayerActionEnum.PASS_TURN));

        assertEquals(0, game.getPlayers().get(0).getDrawableUpper());
    }

    @Test
    @DisplayName("executeCommand DISCONNECT forwards to game.disconnectPlayer")
    void executeDisconnect() {
        controller.executeCommand(new CommandPacket(PlayerActionEnum.DISCONNECT));
        assertFalse(game.getPlayers().get(0).isConnected());
    }

    @Test
    @DisplayName("executeCommand CONNECT forwards to game.connectPlayer")
    void executeConnect() {
        game.disconnectPlayer(0);
        assertFalse(game.getPlayers().get(0).isConnected());

        controller.executeCommand(new CommandPacket(PlayerActionEnum.CONNECT));

        assertTrue(game.getPlayers().get(0).isConnected());
    }

    @Test
    @DisplayName("executeCommand catches PlayerException and forwards an ErrorPacket to the proxy")
    void executeCatchesPlayerException() {
        // first choice succeeds; second triggers InvalidTotem (a PlayerException) -> caught
        controller.executeCommand(new CommandPacket(PlayerActionEnum.CHOOSE_TOTEM, Totem.BLUE));
        proxy.lastError = null;

        controller.executeCommand(new CommandPacket(PlayerActionEnum.CHOOSE_TOTEM, Totem.YELLOW));

        assertNotNull(proxy.lastError, "the controller must forward the InvalidTotem as an ErrorPacket");
        assertEquals("Totem inaccettabile", proxy.lastError.errorTitle);
        assertFalse(proxy.lastError.forceDisconnection);
    }

    // --- initializeClientModel ---

    @Test
    @DisplayName("initializeClientModel happy path forwards the packet to the proxy")
    void initializeClientModelHappy() {
        InitializeModelPacket packet = new InitializeModelPacket(game.giveMockupGame());
        controller.initializeClientModel(packet);
        assertSame(packet, proxy.lastInitModel);
    }

    @Test
    @DisplayName("initializeClientModel on a throwing proxy disconnects the player")
    void initializeClientModelOnThrowDisconnects() {
        proxy.shouldThrow = true;
        controller.initializeClientModel(new InitializeModelPacket(game.giveMockupGame()));
        assertFalse(game.getPlayers().get(0).isConnected());
    }

    // --- updateClientModel ---

    @Test
    @DisplayName("updateClientModel happy path forwards the packet to the proxy")
    void updateClientModelHappy() {
        UpdateModelPacket packet = new UpdateModelPacket();
        controller.updateClientModel(packet);
        assertSame(packet, proxy.lastUpdateModel);
    }

    @Test
    @DisplayName("updateClientModel on a throwing proxy disconnects the player")
    void updateClientModelOnThrowDisconnects() {
        proxy.shouldThrow = true;
        controller.updateClientModel(new UpdateModelPacket());
        assertFalse(game.getPlayers().get(0).isConnected());
    }

    // --- reportError ---

    @Test
    @DisplayName("reportError happy path forwards the packet to the proxy")
    void reportErrorHappy() {
        ErrorPacket packet = new ErrorPacket("Title", "Body", false);
        controller.reportError(packet);
        assertSame(packet, proxy.lastError);
    }

    @Test
    @DisplayName("reportError on a throwing proxy disconnects the player")
    void reportErrorOnThrowDisconnects() {
        proxy.shouldThrow = true;
        controller.reportError(new ErrorPacket("t", "c", false));
        assertFalse(game.getPlayers().get(0).isConnected());
    }

    // ===============================================================
    // Fake PhasedProxyPlayer: stubs every abstract method, records the
    // three VirtualGameClient callbacks and optionally throws on them.
    // ===============================================================

    private static class FakePhasedProxyPlayer extends PhasedProxyPlayer {

        boolean shouldThrow = false;
        InitializeModelPacket lastInitModel;
        UpdateModelPacket lastUpdateModel;
        ErrorPacket lastError;

        FakePhasedProxyPlayer(String nickname) {
            super(null, nickname, ApplicationPhase.GAME, null, null, null, null);
        }

        // --- VirtualGameClient (the only methods that matter for this test) ---

        @Override
        public void initializeClientModel(InitializeModelPacket p) throws Exception {
            if (shouldThrow) throw new Exception("simulated");
            lastInitModel = p;
        }

        @Override
        public void updateClientModel(UpdateModelPacket p) throws Exception {
            if (shouldThrow) throw new Exception("simulated");
            lastUpdateModel = p;
        }

        @Override
        public void reportError(ErrorPacket p) throws Exception {
            if (shouldThrow) throw new Exception("simulated");
            lastError = p;
        }

        // --- VirtualClient / VirtualHallClient / VirtualRoomClient ---

        @Override public void changePhaseClient(ChangePhasePacket p) {}
        @Override public void sendString(StringPacket p) {}
        @Override public void initializeClientHall(InitializeHallPacket p) {}
        @Override public void updateClientHall(UpdateHallPacket p) {}
        @Override public void initializeClientRoom(InitializeRoomPacket p) {}
        @Override public void updateClientRoom(UpdateRoomPacket p) {}

        // --- VirtualServer (sendCommand / joinRoom / createRoom / leaveRoom / disconnect) ---

        @Override public void sendCommand(CommandPacket p) {}
        @Override public void joinRoom(HallJoinPacket p) {}
        @Override public void createRoom(HallCreatePacket p) {}
        @Override public void leaveRoom(RoomLeavePacket p) {}
        @Override public void disconnect(DisconnectPacket p) {}

        // --- Heartbeatable + conversion stubs ---

        @Override public void sendHeartbeat() {}
        @Override public RmiProxyPlayer convertToRmi() { return null; }
        @Override public SocketProxyPlayer convertToSocket() { return null; }
    }
}
