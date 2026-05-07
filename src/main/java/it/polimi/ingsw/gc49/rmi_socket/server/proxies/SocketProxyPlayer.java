package it.polimi.ingsw.gc49.rmi_socket.server.proxies;

import it.polimi.ingsw.gc49.controller.PlayerActionEnum;
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
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.HallCommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.RoomCommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualServerAdapter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

public class SocketProxyPlayer extends PhasedProxyPlayer {

    public SocketProxyPlayer ( ServerMultiplexer server, String nickname,
                               ApplicationPhase startingPhase,
                               VirtualServerAdapter serverSide,
                               ObjectInputStream input, ObjectOutputStream output) {
        super(server, nickname, startingPhase, serverSide, null, input, output);
    }
    private final Object writeLock = new Object();

    //### socket-input reader
    @Override
    public void runVirtualClient() throws SocketException {
        super.startHeartbeating();
        connected = true;

        Datapacket datapacket;

        try {
            while (connected) {
                datapacket = (Datapacket) input.readObject();
                receiveHeartbeat(); // whatever packet received timeout is resetted
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
    public void stop() {
        connected = false;
    }


    //###################
    //### VirtualClient
    @Override
    public void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception {
        synchronized (writeLock) {
            changeLocalPhase(changePhasePacket.newPhase);
            output.writeObject(changePhasePacket);
            output.flush();
        }
    }
    //#######################
    //### VirtualGameClient
    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(initializeModelPacket);
            output.flush();
        }
    }
    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(updateModelPacket);
            output.flush();
        }
    }
    @Override
    public void reportError ( ErrorPacket errorPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(errorPacket);
            output.flush();
        }
    }
    //### VirtualGameServer
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        //executes the command on the controller
        controller.executeCommand(commandPacket);
    }
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        serverSide.disconnect(disconnectPacket);
    }

    //### VirtualHallClient
    @Override
    public void initializeClientHall ( InitializeHallPacket initializeHallPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(initializeHallPacket);
            output.flush();
        }
    }
    @Override
    public void updateClientHall ( UpdateHallPacket updateHallPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(updateHallPacket);
            output.flush();
        }
    }
    //### VirtualHallServer
    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        serverSide.joinRoom(hallJoinPacket);
    }
    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        serverSide.createRoom(hallCreatePacket);
    }
    //### VirtualRoomClient
    @Override
    public void initializeClientRoom ( InitializeRoomPacket initializeRoomPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(initializeRoomPacket);
            output.flush();
        }
    }
    @Override
    public void updateClientRoom ( UpdateRoomPacket updateRoomPacket ) throws Exception {
        synchronized (writeLock) {
            output.writeObject(updateRoomPacket);
            output.flush();
        }
    }
    //### VirtualRoomServer
    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        serverSide.leaveRoom(roomLeavePacket);
    }

    ///-------------------------------
    // Heartbeat
    @Override
    public void sendHeartbeat() throws Exception {
        synchronized (writeLock) {
            output.writeObject(new HeartbeatPacket());
            output.flush();
        }
    }

    @Override
    public void reconnectProcedure ( VirtualClient newClientSide, ObjectInputStream newInput, ObjectOutputStream newOutput ) throws Exception {
        input = newInput;
        output = newOutput;
        controller.executeCommand(new CommandPacket(PlayerActionEnum.CONNECT));
        serverSide.syncPlayer(this);
    }
}
