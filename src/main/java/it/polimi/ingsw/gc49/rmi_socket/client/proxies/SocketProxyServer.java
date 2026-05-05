package it.polimi.ingsw.gc49.rmi_socket.client.proxies;

import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.datapacket.HEARTBEAT.HeartbeatPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_HALL.InitializeHallPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_HALL.UpdateHallPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_ROOM.InitializeRoomPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_ROOM.UpdateRoomPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

public class SocketProxyServer extends PhasedProxyServer {
    private final Object writeLock = new Object();

    public SocketProxyServer ( VirtualClient clientSide ) {
        super(clientSide);
    }


    @Override
    public void finishInitialization ( VirtualServer serverSide, ObjectInputStream input, ObjectOutputStream output ) {
        this.input = input;
        this.output = output;
    }

    //### socket-input reader
    @Override
    public void runVirtualServer() throws SocketException {
        running = true;

        Datapacket datapacket;

        try {
            while (running) {
                datapacket = (Datapacket) input.readObject();

                switch (datapacket.datapacketType) {
                    case CHANGE_PHASE -> changePhaseClient((ChangePhasePacket) datapacket);
                    case ERROR -> reportError((ErrorPacket) datapacket);
                    case INITIALIZE_MODEL -> initializeClientModel((InitializeModelPacket) datapacket);
                    case UPDATE_MODEL -> updateClientModel((UpdateModelPacket) datapacket);
                    case INITIALIZE_HALL -> initializeClientHall((InitializeHallPacket) datapacket);
                    case UPDATE_HALL -> updateClientHall((UpdateHallPacket) datapacket);
                    case INITIALIZE_ROOM  -> initializeClientRoom((InitializeRoomPacket) datapacket);
                    case UPDATE_ROOM -> updateClientRoom((UpdateRoomPacket) datapacket);
                    case HEARTBEAT -> {receiveHeartbeat();}
                    default -> throw new RuntimeException("Unsendable datapacket type: " + datapacket.getDatapacketType());
                }
            }
        } catch (SocketException e) {
            throw new SocketException(e);
        } catch (InvalidClassException e) {
            // QUI vedrai esattamente quale classe causa il flag conflict
            System.err.println("ERRORE SERIALIZZAZIONE: " + e.classname);
            System.err.println("Motivo: " + e.getMessage());
            throw new RuntimeException("Riavvia tutto dopo un Clean!", e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            stop();
        }
    }
    public void stop() {
        running = false;
    }


    //###################
    //### VirtualClient
    @Override
    public void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception {
        clientSide.changePhaseClient(changePhasePacket);
    }
    //#######################
    //### VirtualGameClient
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception {
        clientSide.initializeClientModel(initializeModelPacket);
    }
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception {
        clientSide.updateClientModel(updateModelPacket);
    }
    public void reportError ( ErrorPacket errorPacket ) throws Exception {
        clientSide.reportError(errorPacket);
    }
    //### VirtualGameServer
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(commandPacket);
            output.flush();
        }
    }
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(disconnectPacket);
            output.flush();
        }
    }
    //### VirtualHallClient
    public void initializeClientHall ( InitializeHallPacket initializeHallPacket ) throws Exception {
        clientSide.initializeClientHall(initializeHallPacket);
    }
    public void updateClientHall ( UpdateHallPacket updateHallPacket ) throws Exception {
        clientSide.updateClientHall(updateHallPacket);
    }
    //### VirtualHallServer
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(hallJoinPacket);
            output.flush();
        }
    }
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(hallCreatePacket);
            output.flush();
        }
    }
    //### VirtualRoomClient
    public void initializeClientRoom ( InitializeRoomPacket initializeRoomPacket ) throws Exception {
        clientSide.initializeClientRoom(initializeRoomPacket);
    }
    public void updateClientRoom ( UpdateRoomPacket updateRoomPacket ) throws Exception {
        clientSide.updateClientRoom(updateRoomPacket);
    }
    //### VirtualRoomServer
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(roomLeavePacket);
            output.flush();
        }
    }


    @Override
    protected void pingServer() throws Exception {
        synchronized (writeLock) {
            output.writeObject(new HeartbeatPacket());
            output.flush();
        }
    }

    @Override
    public void syncPlayer(PhasedProxyPlayer p) throws Exception {

    }


}
