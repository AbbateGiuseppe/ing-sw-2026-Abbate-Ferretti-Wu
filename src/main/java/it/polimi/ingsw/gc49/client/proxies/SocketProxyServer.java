package it.polimi.ingsw.gc49.client.proxies;

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
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

/**
 * The {@code SocketProxyServer} class is the Socket-specific implementation of the {@link PhasedProxyServer}.
 * It resides on the client side and manages the TCP Socket connection to the remote server.
 * It runs a continuous listening loop on a dedicated thread to read incoming {@link Datapacket}s,
 * routing them to the local {@link ClientApplication}. Conversely, it takes user commands and
 * safely serializes them over the network using a synchronized lock.
 */
public class SocketProxyServer extends PhasedProxyServer {

    /** * Lock object used to synchronize concurrent write operations to the {@link ObjectOutputStream}.
     * This prevents stream corruption when multiple threads (e.g., UI inputs and Heartbeat scheduler)
     * attempt to send packets simultaneously.
     */
    private final Object writeLock = new Object();


    /**
     * Constructs a new {@code SocketProxyServer}.
     *
     * @param clientSide the local {@link it.polimi.ingsw.gc49.client.ClientApplication} that will process server updates and render the UI.
     */
    public SocketProxyServer ( VirtualClient clientSide ) {
        super(clientSide);
    }


    /**
     * Completes the initialization of this Socket proxy by assigning the network streams.
     *
     * @param serverSide ignored for Sockets (expected {@code null}).
     * @param input      the input stream used to read objects from the server.
     * @param output     the output stream used to send objects to the server.
     */
    @Override
    public void finishInitialization ( VirtualServer serverSide, ObjectInputStream input, ObjectOutputStream output ) {
        this.input = input;
        this.output = output;
    }

    /**
     * Starts the main listening loop for this client.
     * It continuously blocks and waits for incoming {@link Datapacket}s from the server.
     * Once a packet is received, it resets the heartbeat timeout and routes the payload
     * to the appropriate local client method based on its {@code datapacketType}.
     *
     * @throws SocketException if the server connection drops unexpectedly.
     */
    @Override
    public void runVirtualServer() throws SocketException {
        super.runVirtualServer();
        running = true;

        Datapacket datapacket;

        try {
            while (running) {
                datapacket = (Datapacket) input.readObject();
                receiveHeartbeat(); // when whichever packet received, timeout is reset

                switch (datapacket.datapacketType) {
                    case CHANGE_PHASE -> changePhaseClient((ChangePhasePacket) datapacket);
                    case ERROR -> reportError((ErrorPacket) datapacket);
                    case INITIALIZE_MODEL -> initializeClientModel((InitializeModelPacket) datapacket);
                    case UPDATE_MODEL -> updateClientModel((UpdateModelPacket) datapacket);
                    case INITIALIZE_HALL -> initializeClientHall((InitializeHallPacket) datapacket);
                    case UPDATE_HALL -> updateClientHall((UpdateHallPacket) datapacket);
                    case INITIALIZE_ROOM  -> initializeClientRoom((InitializeRoomPacket) datapacket);
                    case UPDATE_ROOM -> updateClientRoom((UpdateRoomPacket) datapacket);
                    case HEARTBEAT -> receiveHeartbeat();
                    case STRING -> sendString((StringPacket) datapacket);
                    default -> throw new RuntimeException("Unsendable datapacket type: " + datapacket.getDatapacketType());
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
     * Gracefully stops the listening loop.
     */
    public void stop() {
        running = false;
    }


    // ============================================================
    // VIRTUAL CLIENT
    // ============================================================

    /**
     * Receives a phase change command from the socket listener and forwards it locally.
     */
    @Override
    public void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception {
        clientSide.changePhaseClient(changePhasePacket);
    }

    /**
     * Receives a text message from the socket listener and forwards it locally.
     */
    @Override
    public void sendString ( StringPacket stringPacket ) throws Exception {
        clientSide.sendString(stringPacket);
    }


    // ============================================================
    // VIRTUAL GAME CLIENT
    // ============================================================

    /**
     * Receives the initial game model from the socket listener and forwards it locally.
     */
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception {
        clientSide.initializeClientModel(initializeModelPacket);
    }

    /**
     * Receives a game model update from the socket listener and forwards it locally.
     */
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception {
        clientSide.updateClientModel(updateModelPacket);
    }

    /**
     * Receives an error notification from the socket listener and forwards it locally.
     */
    public void reportError ( ErrorPacket errorPacket ) throws Exception {
        clientSide.reportError(errorPacket);
    }


    // ============================================================
    // VIRTUAL GAME SERVER
    // ============================================================

    /**
     * Serializes and sends an in-game command to the remote server over the socket.
     * Synchronized to ensure thread safety.
     *
     * @param commandPacket the packet representing the user's action.
     * @throws Exception if an I/O error occurs during transmission.
     */
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(commandPacket);
            output.flush();
        }
    }

    /**
     * Serializes and sends a disconnection request to the remote server.
     * Synchronized to ensure thread safety.
     *
     * @param disconnectPacket the packet representing the disconnection details.
     * @throws Exception if an I/O error occurs during transmission.
     */
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(disconnectPacket);
            output.flush();
        }
    }


    // ============================================================
    // VIRTUAL HALL CLIENT
    // ============================================================
    /**
     * Receives the initial hall state from the socket listener and forwards it locally.
     */
    public void initializeClientHall ( InitializeHallPacket initializeHallPacket ) throws Exception {
        clientSide.initializeClientHall(initializeHallPacket);
    }

    /**
     * Receives an updated hall state from the socket listener and forwards it locally.
     */
    public void updateClientHall ( UpdateHallPacket updateHallPacket ) throws Exception {
        clientSide.updateClientHall(updateHallPacket);
    }


    // ============================================================
    // VIRTUAL HALL SERVER
    // ============================================================

    /**
     * Serializes and sends a request to join a room to the remote server over the socket.
     * Synchronized to ensure thread safety.
     *
     * @param hallJoinPacket the packet containing the target room name.
     * @throws Exception if an I/O error occurs during transmission.
     */
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(hallJoinPacket);
            output.flush();
        }
    }

    /**
     * Serializes and sends a request to create a room to the remote server over the socket.
     * Synchronized to ensure thread safety.
     *
     * @param hallCreatePacket the packet containing the new room details.
     * @throws Exception if an I/O error occurs during transmission.
     */
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(hallCreatePacket);
            output.flush();
        }
    }


    // ============================================================
    // VIRTUAL ROOM CLIENT
    // ============================================================

    /**
     * Receives the initial waiting room state from the socket listener and forwards it locally.
     */
    public void initializeClientRoom ( InitializeRoomPacket initializeRoomPacket ) throws Exception {
        clientSide.initializeClientRoom(initializeRoomPacket);
    }

    /**
     * Receives an updated waiting room state from the socket listener and forwards it locally.
     */
    public void updateClientRoom ( UpdateRoomPacket updateRoomPacket ) throws Exception {
        clientSide.updateClientRoom(updateRoomPacket);
    }


    // ============================================================
    // VIRTUAL ROOM SERVER
    // ============================================================

    /**
     * Serializes and sends a request to leave the current room to the remote server over the socket.
     * Synchronized to ensure thread safety.
     *
     * @param roomLeavePacket the packet indicating the leave request.
     * @throws Exception if an I/O error occurs during transmission.
     */
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(roomLeavePacket);
            output.flush();
        }
    }


    /**
     * Serializes and sends a heartbeat ping over the socket to keep the server connection alive.
     * Synchronized to ensure thread safety.
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
}
