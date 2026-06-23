package it.polimi.ingsw.gc49.server.proxies;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.HEARTBEAT.HeartbeatPacket;
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
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.HallCommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.RoomCommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualServerAdapter;
import it.polimi.ingsw.gc49.server.controller.PlayerActionEnum;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;


/**
 * The {@code SocketProxyPlayer} class is the Socket-specific implementation of the {@link PhasedProxyPlayer}.
 * It manages the bidirectional communication between the server and a client connected via TCP Sockets.
 * Unlike the RMI implementation which relies on remote method invocations, this class runs a continuous
 * listening loop to receive and deserialize {@link Datapacket} objects, routing them based on their type.
 * It strictly synchronizes outgoing transmissions to prevent stream corruption.
 */
public class SocketProxyPlayer extends PhasedProxyPlayer {

    /**
     * Constructs a new {@code SocketProxyPlayer}.
     * Since this is a Socket connection, the RMI client stub is explicitly set to {@code null}.
     *
     * @param server        the main server multiplexer.
     * @param nickname      the unique nickname of the player.
     * @param startingPhase the initial application phase (usually HALL).
     * @param serverSide    the adapter linking the proxy to the current server logic.
     * @param input         the input stream to read incoming packets.
     * @param output        the output stream to send outgoing packets.
     */
    public SocketProxyPlayer ( ServerMultiplexer server, String nickname,
                               ApplicationPhase startingPhase,
                               VirtualServerAdapter serverSide,
                               ObjectInputStream input, ObjectOutputStream output) {
        super(server, nickname, startingPhase, serverSide, null, input, output);
    }

    /**
     * Lock object used to synchronize concurrent writes to the {@link ObjectOutputStream}.
     * Marked as transient so it is re-instantiated upon server recovery.
     */
    private transient Object writeLock = new Object();

    /**
     * Starts the main listening loop for this socket client.
     * It continuously reads incoming {@link Datapacket} objects, resets the heartbeat timeout,
     * validates the application phase, and routes the packet to the appropriate server method
     * based on its {@code datapacketType}.
     *
     * @throws SocketException if the connection drops unexpectedly.
     */
    @Override
    public void runVirtualClient() throws SocketException {
        super.runVirtualClient();
        connected = true;

        Datapacket datapacket;

        try {
            while (connected) {
                datapacket = (Datapacket) input.readObject();
                receiveHeartbeat(); // when whichever packet received, timeout is reset
                addSenderNickname(datapacket);

                if(assureRightPhase(datapacket)) {
                    switch (datapacket.datapacketType) {
                        case COMMAND -> sendCommand((CommandPacket) datapacket);
                        case DISCONNECT -> disconnect((DisconnectPacket) datapacket);
                        case HALL_COMMAND -> {
                            HallCommandPacket hallCommandPacket = (HallCommandPacket) datapacket;
                            switch (hallCommandPacket.commandType) {
                                case CREATE -> createRoom((HallCreatePacket) hallCommandPacket);
                                case JOIN -> joinRoom((HallJoinPacket) hallCommandPacket);
                                default -> throw new RuntimeException("Uncoded hallcommandpacket type: " + hallCommandPacket.commandType + " from: " + nickname);
                            }
                        }
                        case ROOM_COMMAND -> {
                            RoomCommandPacket roomCommandPacket = (RoomCommandPacket) datapacket;
                            switch (roomCommandPacket.commandType) {
                                case LEAVE -> leaveRoom((RoomLeavePacket) roomCommandPacket);
                                default -> throw new RuntimeException("Uncoded roomcommandpacket type: " + roomCommandPacket.commandType + " from: " + nickname);
                            }
                        }
                        case HEARTBEAT -> receiveHeartbeat();
                        default ->
                                throw new RuntimeException("Unsendable datapacket type: " + datapacket.getDatapacketType() + " from: " + nickname);
                    }
                }
            }
        } catch (SocketException e) {
            throw new SocketException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            stop();
        }
    }

    /**
     * Gracefully stops the listening loop by marking the client as disconnected.
     */
    public void stop() {
        connected = false;
    }


    // ============================================================
    //VIRTUAL CLIENT
    // ============================================================

    /**
     * Sends a packet to instruct the client to change its application phase.
     * Synchronized to prevent stream corruption.
     *
     * @param changePhasePacket the packet containing the new phase.
     * @throws Exception if an I/O error occurs during transmission.
     */
    @Override
    public void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception {
        synchronized (writeLock) {
            changeLocalPhase(changePhasePacket.newPhase);
            output.writeObject(changePhasePacket);
            output.flush();
        }
    }

    /**
     * Sends a generic string packet to the client.
     * Synchronized to prevent stream corruption.
     *
     * @param stringPacket the string packet to send.
     * @throws Exception if an I/O error occurs during transmission.
     */
    @Override
    public void sendString ( StringPacket stringPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(stringPacket);
            output.flush();
            output.reset();
        }
    }


    // ============================================================
    //VIRTUAL GAME CLIENT
    // ============================================================

    /**
     * Sends the initial game model to the client and clears the stream cache.
     *
     * @param initializeModelPacket the packet containing the initial mockup model.
     * @throws Exception if an I/O error occurs during transmission.
     */
    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(initializeModelPacket);
            output.flush();
            output.reset();
        }
    }

    /**
     * Sends a game model update to the client and clears the stream cache.
     *
     * @param updateModelPacket the packet containing the updated mockup model.
     * @throws Exception if an I/O error occurs during transmission.
     */
    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(updateModelPacket);
            output.flush();
            output.reset();
        }
    }

    /**
     * Sends an error notification packet to the client.
     *
     * @param errorPacket the packet detailing the error.
     * @throws Exception if an I/O error occurs during transmission.
     */
    @Override
    public void reportError ( ErrorPacket errorPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(errorPacket);
            output.flush();
            output.reset();
        }
    }


    // ============================================================
    //VIRTUAL GAME SERVER
    // ============================================================

    /**
     * Forwards an in-game command received via socket to the player's controller.
     *
     * @param commandPacket the packet containing the player's action.
     * @throws Exception if an error occurs during execution.
     */
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        //executes the command on the controller
        controller.executeCommand(commandPacket);
    }

    // ============================================================
    //VIRTUAL HALL CLIENT
    // ============================================================
    /**
     * Sends the initial hall view to the client.
     *
     * @param initializeHallPacket the packet containing the initial mockup hall.
     * @throws Exception if an I/O error occurs during transmission.
     */
    @Override
    public void initializeClientHall ( InitializeHallPacket initializeHallPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(initializeHallPacket);
            output.flush();
            output.reset();
        }
    }

    /**
     * Sends an updated hall view to the client.
     *
     * @param updateHallPacket the packet containing the updated mockup hall.
     * @throws Exception if an I/O error occurs during transmission.
     */
    @Override
    public void updateClientHall ( UpdateHallPacket updateHallPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(updateHallPacket);
            output.flush();
            output.reset();
        }
    }

    // ============================================================
    //VIRTUAL HALL SERVER
    // ============================================================
    /**
     * Forwards a join room request received via socket to the server logic.
     *
     * @param hallJoinPacket the packet specifying the room to join.
     * @throws Exception if an error occurs while processing the request.
     */
    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        serverSide.joinRoom(hallJoinPacket);
    }

    /**
     * Forwards a create room request received via socket to the server logic.
     *
     * @param hallCreatePacket the packet detailing the room to create.
     * @throws Exception if an error occurs while processing the request.
     */
    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        serverSide.createRoom(hallCreatePacket);
    }


    // ============================================================
    //VIRTUAL ROOM CLIENT
    // ============================================================

    /**
     * Sends the initial waiting room view to the client.
     *
     * @param initializeRoomPacket the packet containing the initial mockup room.
     * @throws Exception if an I/O error occurs during transmission.
     */
    @Override
    public void initializeClientRoom ( InitializeRoomPacket initializeRoomPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(initializeRoomPacket);
            output.flush();
            output.reset();
        }
    }

    /**
     * Sends an updated waiting room view to the client.
     *
     * @param updateRoomPacket the packet containing the updated mockup room.
     * @throws Exception if an I/O error occurs during transmission.
     */
    @Override
    public void updateClientRoom ( UpdateRoomPacket updateRoomPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(updateRoomPacket);
            output.flush();
            output.reset();
        }
    }


    // ============================================================
    //VIRTUAL ROOM SERVER
    // ============================================================
    /**
     * Forwards a leave room request received via socket to the server logic.
     *
     * @param roomLeavePacket the packet indicating the leave action.
     * @throws Exception if an error occurs while processing the request.
     */

    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        serverSide.leaveRoom(roomLeavePacket);
    }

    // ============================================================
    //HEARTBEAT
    // ============================================================
    /**
     * Sends a heartbeat ping over the socket connection to keep the client alive.
     * Synchronized to prevent stream corruption.
     *
     * @throws Exception if an I/O error occurs during transmission.
     */
    @Override
    public void sendHeartbeat() throws Exception {
        synchronized (writeLock) {
            output.writeObject(new HeartbeatPacket());
            output.flush();
        }
    }

    // ============================================================
    //RESILIENCE
    // ============================================================

    /**
     * Executes the disconnection logic specific to Sockets.
     * It forcibly closes the input stream before proceeding with the standard disconnection routine.
     *
     * @param disconnectPacket the packet containing disconnection details.
     * @throws Exception if an error occurs during cleanup.
     */
    @Override
    protected void disconnectProcedure ( DisconnectPacket disconnectPacket ) throws Exception {
        input.close();
        super.disconnectProcedure(disconnectPacket);
    }


    // ============================================================
    //CONVERSION METHODS
    // ============================================================
    /**
     * Converts this Socket proxy into an RMI-based proxy.
     * This is used if the player disconnected and reconnects using RMI instead of Sockets.
     *
     * @return a newly instantiated {@link RmiProxyPlayer} with the current state.
     */
    @Override
    public RmiProxyPlayer convertToRmi(){
        RmiProxyPlayer converted = new RmiProxyPlayer(server, nickname, currentPhase, serverSide, clientSide);
        if(controller != null) {
            converted.setController(controller);
            controller.setNewControllingPlayer(converted);
        }
        return converted;
    }

    /**
     * Returns this instance, as the proxy is already configured for Sockets.
     *
     * @return this {@code SocketProxyPlayer} instance.
     */
    @Override
    public SocketProxyPlayer convertToSocket(){
        return this;
    }


    // ============================================================
    //PERSISTENCE
    // ============================================================

    /**
     * Re-initializes transient fields after the server has recovered its state from disk.
     * Specifically, it recreates the lock object used for output synchronization.
     *
     * @param server the newly booted {@link ServerMultiplexer} to attach to.
     */
    @Override
    public void resumeAfterServerCrash(ServerMultiplexer server) {
        super.resumeAfterServerCrash(server);
        this.writeLock = new Object();
    }

}
