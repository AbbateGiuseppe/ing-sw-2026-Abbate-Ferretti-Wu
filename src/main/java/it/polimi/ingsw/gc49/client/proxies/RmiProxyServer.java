package it.polimi.ingsw.gc49.client.proxies;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.STRING.StringPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_HALL.InitializeHallPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_HALL.UpdateHallPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_ROOM.InitializeRoomPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_ROOM.UpdateRoomPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Heartbeatable;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


/**
 * The {@code RmiProxyServer} class is the RMI-specific implementation of the {@link PhasedProxyServer}.
 * It resides on the client side and acts as the immediate bridge to the remote server.
 * It forwards incoming remote method invocations from the server to the local {@link ClientApplication},
 * and takes local user actions (commands, room creation, etc.) forwarding them to the server's RMI stub.
 */
public class RmiProxyServer extends PhasedProxyServer {

    /**
     * Constructs a new {@code RmiProxyServer}.
     *
     * @param clientSide the local {@link ClientApplication} that will process server updates and render the UI.
     */
    public RmiProxyServer ( VirtualClient clientSide ) {
        super(clientSide);
    }


    /**
     * Completes the initialization of this RMI proxy by linking it to the remote server stub.
     * Since this uses Java RMI, the socket streams are ignored.
     *
     * @param serverSide the remote RMI stub ({@link VirtualServer}) exposed by the server.
     * @param input      ignored in RMI (expected {@code null}).
     * @param output     ignored in RMI (expected {@code null}).
     */
    @Override
    public void finishInitialization ( VirtualServer serverSide, ObjectInputStream input, ObjectOutputStream output ) {
        this.serverSide = serverSide;
    }

    // ============================================================
    // VIRTUAL CLIENT
    // ============================================================

    /**
     * Receives a phase change command from the server and forwards it to the local client.
     *
     * @param changePhasePacket the packet containing the new application phase.
     * @throws Exception if an error occurs locally.
     */
    @Override
    public void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception {
        clientSide.changePhaseClient(changePhasePacket);
    }

    /**
     * Receives a generic text message from the server and forwards it to the local client's UI.
     *
     * @param stringPacket the packet containing the message string.
     * @throws Exception if an error occurs locally.
     */
    @Override
    public void sendString ( StringPacket stringPacket ) throws Exception {
        clientSide.sendString(stringPacket);
    }

    // ============================================================
    // VIRTUAL GAME CLIENT
    // ============================================================
    /**
     * Receives the initial game model from the server and forwards it to the local client.
     *
     * @param initializeModelPacket the packet containing the complete mockup model.
     * @throws Exception if an error occurs locally.
     */
    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception {
        clientSide.initializeClientModel(initializeModelPacket);
    }

    /**
     * Receives a game model update from the server and forwards it to the local client.
     *
     * @param updateModelPacket the packet containing partial updates for the mockup model.
     * @throws Exception if an error occurs locally.
     */
    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception {
        clientSide.updateClientModel(updateModelPacket);
    }

    /**
     * Receives an error notification from the server and forwards it to the local client's UI.
     *
     * @param errorPacket the packet detailing the game error.
     * @throws Exception if an error occurs locally.
     */
    @Override
    public void reportError ( ErrorPacket errorPacket ) throws Exception {
        clientSide.reportError(errorPacket);
    }


    // ============================================================
    // VIRTUAL GAME SERVER
    // ============================================================
    /**
     * Forwards an in-game action/command generated by the local user to the remote server.
     *
     * @param commandPacket the packet representing the player's move or action.
     * @throws Exception if a remote communication error occurs.
     */
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        serverSide.sendCommand(commandPacket);
    }

    /**
     * Forwards a disconnection request to the remote server.
     *
     * @param disconnectPacket the packet containing the disconnection details.
     * @throws Exception if a remote communication error occurs.
     */
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        serverSide.disconnect(disconnectPacket);
    }

    // ============================================================
    // VIRTUAL HALL CLIENT
    // ============================================================

    /**
     * Receives the initial hall state from the server and forwards it to the local client.
     *
     * @param initializeHallPacket the packet containing the complete mockup hall.
     * @throws Exception if an error occurs locally.
     */
    @Override
    public void initializeClientHall ( InitializeHallPacket initializeHallPacket ) throws Exception {
        clientSide.initializeClientHall(initializeHallPacket);
    }

    /**
     * Receives an updated hall state from the server and forwards it to the local client.
     *
     * @param updateHallPacket the packet containing the new mockup hall.
     * @throws Exception if an error occurs locally.
     */
    @Override
    public void updateClientHall ( UpdateHallPacket updateHallPacket ) throws Exception {
        clientSide.updateClientHall(updateHallPacket);
    }


    // ============================================================
    // VIRTUAL HALL SERVER
    // ============================================================

    /**
     * Forwards a user's request to join a specific room to the remote server.
     *
     * @param hallJoinPacket the packet containing the target room's name.
     * @throws Exception if a remote communication error occurs.
     */
    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        serverSide.joinRoom(hallJoinPacket);
    }

    /**
     * Forwards a user's request to create a new room to the remote server.
     *
     * @param hallCreatePacket the packet containing the new room's details.
     * @throws Exception if a remote communication error occurs.
     */
    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        serverSide.createRoom(hallCreatePacket);
    }

    // ============================================================
    // VIRTUAL ROOM CLIENT
    // ============================================================

    /**
     * Receives the initial waiting room state from the server and forwards it to the local client.
     *
     * @param initializeRoomPacket the packet containing the complete mockup room.
     * @throws Exception if an error occurs locally.
     */
    @Override
    public void initializeClientRoom ( InitializeRoomPacket initializeRoomPacket ) throws Exception {
        clientSide.initializeClientRoom(initializeRoomPacket);
    }

    /**
     * Receives an updated waiting room state from the server and forwards it to the local client.
     *
     * @param updateRoomPacket the packet containing the new mockup room.
     * @throws Exception if an error occurs locally.
     */
    @Override
    public void updateClientRoom ( UpdateRoomPacket updateRoomPacket ) throws Exception {
        clientSide.updateClientRoom(updateRoomPacket);
    }


    // ============================================================
    // VIRTUAL ROOM SERVER
    // ============================================================

    /**
     * Forwards a user's request to leave their current waiting room to the remote server.
     *
     * @param roomLeavePacket the packet indicating the leave request.
     * @throws Exception if a remote communication error occurs.
     */
    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        serverSide.leaveRoom(roomLeavePacket);
    }

    /**
     * Sends a heartbeat ping to the remote server by directly invoking the
     * {@code receiveHeartbeat} method on the RMI stub.
     *
     * @throws Exception if the remote invocation fails (e.g., server offline).
     */
    @Override
    public void sendHeartbeat() throws Exception {
        ((Heartbeatable) serverSide).receiveHeartbeat();
    }
}
