package it.polimi.ingsw.gc49.server.rooms;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.STRING.StringPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_HALL.InitializeHallPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_ROOM.InitializeRoomPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_HALL.UpdateHallPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_ROOM.UpdateRoomPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Heartbeatable;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualGameServerAdapter;
import it.polimi.ingsw.gc49.server.Hall;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.server.proxies.RmiProxyPlayer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PlayingRoomRecoveryTest {

    @Test
    void restoreTransientGameLinksRebindsControllersAfterRecovery() throws Exception {
        RecordingClient client1 = new RecordingClient();
        RecordingClient client2 = new RecordingClient();
        RmiProxyPlayer player1 = proxy("lorenzo", client1);
        RmiProxyPlayer player2 = proxy("diego", client2);
        PlayingRoom room = new PlayingRoom(null, new Hall(), "Room", 2, List.of(player1, player2));
        room.createGame();

        clearControllerListeners(room.getGame());
        player1.setController(null);
        player2.setController(null);
        player1.setServerSideObject(null);
        player2.setServerSideObject(null);

        room.restoreTransientGameLinks();

        assertEquals(2, controllerListenerCount(room.getGame()));
        assertNotNull(controllerOf(player1));
        assertNotNull(controllerOf(player2));
        assertInstanceOf(VirtualGameServerAdapter.class, player1.getServerSide());
        assertInstanceOf(VirtualGameServerAdapter.class, player2.getServerSide());
    }

    @Test
    void gameSyncSendsAuthoritativeCurrentPlayerSnapshot() throws Exception {
        RecordingClient client1 = new RecordingClient();
        RecordingClient client2 = new RecordingClient();
        RmiProxyPlayer player1 = proxy("lorenzo", client1);
        RmiProxyPlayer player2 = proxy("diego", client2);
        PlayingRoom room = new PlayingRoom(null, new Hall(), "Room", 2, List.of(player1, player2));
        room.createGame();
        room.getGame().setPhaseStatus("Choose Offer");
        room.getGame().setCurrentPlayer(room.getGame().getPlayers().get(1));
        room.getGame().setCurrentPlayerIndex(1);

        player1.getServerSide().syncPlayer(player1);
        player2.getServerSide().syncPlayer(player2);

        assertEquals(1, client1.initializedGame.getCurrentPlayerIndex());
        assertEquals(1, client2.initializedGame.getCurrentPlayerIndex());
    }

    private RmiProxyPlayer proxy(String nickname, RecordingClient client) {
        return new RmiProxyPlayer(null, nickname, ApplicationPhase.ROOM, null, client);
    }

    private Object controllerOf(PhasedProxyPlayer proxy) throws Exception {
        Field field = PhasedProxyPlayer.class.getDeclaredField("controller");
        field.setAccessible(true);
        return field.get(proxy);
    }

    private void clearControllerListeners(Game game) throws Exception {
        controllerListeners(game).clear();
    }

    private int controllerListenerCount(Game game) throws Exception {
        return controllerListeners(game).size();
    }

    private List<?> controllerListeners(Game game) throws Exception {
        return (List<?>) listenersField().get(game);
    }

    private Field listenersField() throws Exception {
        Field field = Game.class.getDeclaredField("controllersListeners");
        field.setAccessible(true);
        return field;
    }

    private static final class RecordingClient implements VirtualClient, Heartbeatable {
        private MockupGame initializedGame;

        @Override
        public void changePhaseClient(ChangePhasePacket changePhasePacket) {
        }

        @Override
        public void sendString(StringPacket stringPacket) {
        }

        @Override
        public void initializeClientModel(InitializeModelPacket initializeModelPacket) {
            initializedGame = initializeModelPacket.mockupModel;
        }

        @Override
        public void updateClientModel(UpdateModelPacket updateModelPacket) {
        }

        @Override
        public void reportError(ErrorPacket errorPacket) {
        }

        @Override
        public void initializeClientHall(InitializeHallPacket initializeHallPacket) {
        }

        @Override
        public void updateClientHall(UpdateHallPacket updateHallPacket) {
        }

        @Override
        public void initializeClientRoom(InitializeRoomPacket initializeRoomPacket) {
        }

        @Override
        public void updateClientRoom(UpdateRoomPacket updateRoomPacket) {
        }

        @Override
        public void startHeartbeating() {
        }

        @Override
        public void sendHeartbeat() {
        }

        @Override
        public void receiveHeartbeat() {
        }
    }
}
