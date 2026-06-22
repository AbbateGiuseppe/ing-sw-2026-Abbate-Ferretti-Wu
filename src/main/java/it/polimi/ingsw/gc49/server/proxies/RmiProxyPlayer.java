package it.polimi.ingsw.gc49.server.proxies;

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
import it.polimi.ingsw.gc49.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Heartbeatable;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualServerAdapter;
import it.polimi.ingsw.gc49.server.controller.PlayerActionEnum;
import it.polimi.ingsw.gc49.server.model.Game;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.rmi.NoSuchObjectException;
import java.rmi.server.UnicastRemoteObject;

/**
 * The {@code RmiProxyPlayer} class is the RMI-specific implementation of the {@link PhasedProxyPlayer}.
 * It manages the bidirectional communication between the server and a client connected via Java RMI.
 * It implements various virtual client and server interfaces, forwarding outgoing packets to the
 * client's RMI stub and routing incoming commands to the appropriate server-side adapter or controller,
 * always ensuring that the application phase is respected.
 */
public class RmiProxyPlayer extends PhasedProxyPlayer {

    /**
     * Constructs a new {@code RmiProxyPlayer}.
     * Since this is an RMI connection, socket streams (input/output) are explicitly set to {@code null}.
     *
     * @param server        the main server multiplexer.
     * @param nickname      the unique nickname of the player.
     * @param startingPhase the initial application phase (usually HALL).
     * @param serverSide    the adapter linking the proxy to the current server logic.
     * @param clientSide    the remote RMI stub used to invoke methods on the client.
     */
    public RmiProxyPlayer ( ServerMultiplexer server, String nickname,
                            ApplicationPhase startingPhase,
                            VirtualServerAdapter serverSide, VirtualClient clientSide ) {
        super(server, nickname, startingPhase, serverSide, clientSide, null, null);
    }

    // ============================================================
    //VIRTUAL CLIENT
    // ============================================================

    /**
     * Instructs the client to change its current application phase and updates the server-side tracker.
     *
     * @param changePhasePacket the packet containing the new phase.
     * @throws Exception if a remote communication error occurs.
     */
    @Override
    public void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception {
        changeLocalPhase(changePhasePacket.newPhase);
        clientSide.changePhaseClient(changePhasePacket);
    }

    /**
     * Sends a generic string message to the client.
     *
     * @param stringPacket the packet containing the string.
     * @throws Exception if a remote communication error occurs.
     */
    @Override
    public void sendString ( StringPacket stringPacket ) throws Exception {
        clientSide.sendString(stringPacket);
    }

    // ============================================================
    //VIRTUAL GAME CLIENT
    // ============================================================

    /**
     * Sends the initial game model state to the client, provided the phase is correct.
     *
     * @param initializeModelPacket the packet containing the mockup model.
     * @throws Exception if a remote communication error occurs.
     */
    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception {
        if(assureRightPhase(initializeModelPacket)){
            clientSide.initializeClientModel(initializeModelPacket);
        }
    }

    /**
     * Sends a game model update to the client, provided the phase is correct.
     *
     * @param updateModelPacket the packet containing the updated mockup model.
     * @throws Exception if a remote communication error occurs.
     */
    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception {
        if(assureRightPhase(updateModelPacket)){
            clientSide.updateClientModel(updateModelPacket);
        }
    }

    /**
     * Sends an error notification to the client during a game.
     *
     * @param errorPacket the packet containing the error details.
     * @throws Exception if a remote communication error occurs.
     */
    @Override
    public void reportError ( ErrorPacket errorPacket ) throws Exception {
        if(assureRightPhase(errorPacket)){
            clientSide.reportError(errorPacket);
        }
    }


    // ============================================================
    //VIRTUAL GAME SERVER
    // ============================================================

    /**
     * Receives an in-game command from the RMI client, validates the phase,
     * injects the sender's nickname, and forwards it to the game controller.
     *
     * @param commandPacket the packet containing the player's action.
     * @throws Exception if an error occurs during execution.
     */
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        if(assureRightPhase(commandPacket)){
            addSenderNickname(commandPacket);
            //executes the command on the controller
            if(controller != null) {
                controller.executeCommand(commandPacket);
            }
        }
    }

    // ============================================================
    //VIRTUAL HALL CLIENT
    // ============================================================

    /**
     * Sends the initial hall state to the client.
     *
     * @param initializeHallPacket the packet containing the mockup hall.
     * @throws Exception if a remote communication error occurs.
     */
    @Override
    public void initializeClientHall ( InitializeHallPacket initializeHallPacket ) throws Exception {
        if(assureRightPhase(initializeHallPacket)){
            clientSide.initializeClientHall(initializeHallPacket);
        }
    }

    /**
     * Sends a hall update to the client.
     *
     * @param updateHallPacket the packet containing the updated mockup hall.
     * @throws Exception if a remote communication error occurs.
     */
    @Override
    public void updateClientHall ( UpdateHallPacket updateHallPacket ) throws Exception {
        if(assureRightPhase(updateHallPacket)){
            clientSide.updateClientHall(updateHallPacket);
        }
    }


    // ============================================================
    //VIRTUAL HALL SERVER
    // ============================================================

    /**
     * Receives a request from the client to join an existing room.
     *
     * @param hallJoinPacket the packet containing the target room name.
     * @throws Exception if an error occurs while processing the join request.
     */
    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        if(assureRightPhase(hallJoinPacket)){
            addSenderNickname(hallJoinPacket);
            serverSide.joinRoom(hallJoinPacket);
        }
    }

    /**
     * Receives a request from the client to create a new room.
     *
     * @param hallCreatePacket the packet containing the new room details.
     * @throws Exception if an error occurs while creating the room.
     */
    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        if(assureRightPhase(hallCreatePacket)){
            addSenderNickname(hallCreatePacket);
            serverSide.createRoom(hallCreatePacket);
        }
    }


    // ============================================================
    //VIRTUAL ROOM CLIENT
    // ============================================================

    /**
     * Sends the initial waiting room state to the client.
     *
     * @param initializeRoomPacket the packet containing the mockup room.
     * @throws Exception if a remote communication error occurs.
     */
    @Override
    public void initializeClientRoom ( InitializeRoomPacket initializeRoomPacket ) throws Exception {
        if(assureRightPhase(initializeRoomPacket)){
            clientSide.initializeClientRoom(initializeRoomPacket);
        }
    }

    /**
     * Sends a waiting room update to the client.
     *
     * @param updateRoomPacket the packet containing the updated mockup room.
     * @throws Exception if a remote communication error occurs.
     */
    @Override
    public void updateClientRoom ( UpdateRoomPacket updateRoomPacket ) throws Exception {
        if(assureRightPhase(updateRoomPacket)){
            clientSide.updateClientRoom(updateRoomPacket);
        }
    }
    // ============================================================
    //VIRTUAL ROOM SERVER
    // ============================================================

    /**
     * Receives a request from the client to leave the current waiting room.
     *
     * @param roomLeavePacket the packet indicating the leave request.
     * @throws Exception if an error occurs while leaving the room.
     */
    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        if(assureRightPhase(roomLeavePacket)){
            addSenderNickname(roomLeavePacket);
            serverSide.leaveRoom(roomLeavePacket);
        }
    }

    // ============================================================
    //HEARTBEAT
    // ============================================================

    /**
     * Sends a heartbeat ping to the client by directly invoking the
     * {@code receiveHeartbeat} method on the remote RMI stub.
     *
     * @throws Exception if the remote invocation fails (e.g., client disconnected).
     */
    @Override
    public void sendHeartbeat() throws Exception {
        // call method on clientStub
        ((Heartbeatable) clientSide).receiveHeartbeat();
    }

    // ============================================================
    //DISCONNECTION
    // ============================================================

    /**
     * Executes the disconnection logic specific to RMI.
     * Before triggering the standard disconnection procedure, it unexports this object
     * from the RMI runtime to free up the port and prevent memory leaks.
     *
     * @param disconnectPacket the packet containing disconnection details.
     * @throws Exception if an error occurs during the cleanup.
     */
    @Override
    protected void disconnectProcedure ( DisconnectPacket disconnectPacket ) throws Exception {
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException _) {
            //no problem, already cleaned!
        }
        super.disconnectProcedure(disconnectPacket);
    }

    // ============================================================
    //CONVERSION METHODS
    // ============================================================
    /**
     * Returns this instance, as the proxy is already configured for RMI.
     *
     * @return this {@code RmiProxyPlayer} instance.
     */

    @Override
    public RmiProxyPlayer convertToRmi(){
        return this;
    }

    /**
     * Converts this RMI proxy into a Socket-based proxy.
     * This is used if the player disconnected and reconnects using Sockets instead of RMI.
     *
     * @return a newly instantiated {@link SocketProxyPlayer} with the current state.
     */
    @Override
    public SocketProxyPlayer convertToSocket(){
        return new SocketProxyPlayer(server, nickname, currentPhase, serverSide, input, output);
    }

}
