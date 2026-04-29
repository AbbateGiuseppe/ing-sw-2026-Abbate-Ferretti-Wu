package it.polimi.ingsw.gc49.rmi_socket.client.proxies;

import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.HallClientPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.INITIALIZE.HallClientInitializePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.UPDATE.HallClientUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.INITIALIZE.RoomClientInitializePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.RoomClientPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.UPDATE.RoomClientUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.datapacket.uncertain.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

public class SocketProxyServer extends PhasedProxyServer {

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
                    case HALL_CLIENT -> {
                        HallClientPacket hallClientPacket = (HallClientPacket) datapacket;
                        switch (hallClientPacket.commandType) {
                            case INITIALIZE -> initializeClientHall((HallClientInitializePacket) hallClientPacket);
                            case UPDATE -> updateClientHall((HallClientUpdatePacket) hallClientPacket);
                            default -> throw new RuntimeException("Uncoded hallclientpacket type: " + hallClientPacket.commandType);
                        }
                    }
                    case ROOM_CLIENT -> {
                        RoomClientPacket roomClientPacket = (RoomClientPacket) datapacket;
                        switch (roomClientPacket.commandType) {
                            case INITIALIZE -> initializeClientRoom((RoomClientInitializePacket) roomClientPacket);
                            case UPDATE -> updateClientRoom((RoomClientUpdatePacket) roomClientPacket);
                            default -> throw new RuntimeException("Uncoded roomclientpacket type: " + roomClientPacket.commandType);
                        }
                    }
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
        output.writeObject(commandPacket);
        output.flush();
    }
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        output.writeObject(disconnectPacket);
        output.flush();
    }
    public void reconnect ( ReconnectPacket reconnectPacket ) throws Exception {
        output.writeObject(reconnectPacket);
        output.flush();
    }
    //### VirtualHallClient
    public void initializeClientHall ( HallClientInitializePacket hallClientInitializePacket ) throws Exception {
        clientSide.initializeClientHall(hallClientInitializePacket);
    }
    public void updateClientHall ( HallClientUpdatePacket hallClientUpdatePacket ) throws Exception {
        clientSide.updateClientHall(hallClientUpdatePacket);
    }
    //### VirtualHallServer
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        output.writeObject(hallJoinPacket);
        output.flush();
    }
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        output.writeObject(hallCreatePacket);
        output.flush();
    }
    //### VirtualRoomClient
    public void initializeClientRoom ( RoomClientInitializePacket roomClientInitializePacket ) throws Exception {
        clientSide.initializeClientRoom(roomClientInitializePacket);
    }
    public void updateClientRoom ( RoomClientUpdatePacket roomClientUpdatePacket ) throws Exception {
        clientSide.updateClientRoom(roomClientUpdatePacket);
    }
    //### VirtualRoomServer
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        output.writeObject(roomLeavePacket);
        output.flush();
    }
}
