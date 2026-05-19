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

public class RmiProxyPlayer extends PhasedProxyPlayer {
    public RmiProxyPlayer ( ServerMultiplexer server, String nickname,
                            ApplicationPhase startingPhase,
                            VirtualServerAdapter serverSide, VirtualClient clientSide ) {
        super(server, nickname, startingPhase, serverSide, clientSide, null, null);
    }

    //###################
    //### VirtualClient
    @Override
    public void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception {
        changeLocalPhase(changePhasePacket.newPhase);
        clientSide.changePhaseClient(changePhasePacket);
    }

    @Override
    public void sendString ( StringPacket stringPacket ) throws Exception {
        clientSide.sendString(stringPacket);
    }
    //#######################
    //### VirtualGameClient
    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception {
        if(assureRightPhase(initializeModelPacket)){
            clientSide.initializeClientModel(initializeModelPacket);
        }
    }
    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception {
        if(assureRightPhase(updateModelPacket)){
            clientSide.updateClientModel(updateModelPacket);
        }
    }
    @Override
    public void reportError ( ErrorPacket errorPacket ) throws Exception {
        if(assureRightPhase(errorPacket)){
            clientSide.reportError(errorPacket);
        }
    }
    //### VirtualGameServer
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

    //### VirtualHallClient
    @Override
    public void initializeClientHall ( InitializeHallPacket initializeHallPacket ) throws Exception {
        if(assureRightPhase(initializeHallPacket)){
            clientSide.initializeClientHall(initializeHallPacket);
        }
    }
    @Override
    public void updateClientHall ( UpdateHallPacket updateHallPacket ) throws Exception {
        if(assureRightPhase(updateHallPacket)){
            clientSide.updateClientHall(updateHallPacket);
        }
    }
    //### VirtualHallServer
    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        if(assureRightPhase(hallJoinPacket)){
            addSenderNickname(hallJoinPacket);
            serverSide.joinRoom(hallJoinPacket);
        }
    }
    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        if(assureRightPhase(hallCreatePacket)){
            addSenderNickname(hallCreatePacket);
            serverSide.createRoom(hallCreatePacket);
        }
    }
    //### VirtualRoomClient
    @Override
    public void initializeClientRoom ( InitializeRoomPacket initializeRoomPacket ) throws Exception {
        if(assureRightPhase(initializeRoomPacket)){
            clientSide.initializeClientRoom(initializeRoomPacket);
        }
    }
    @Override
    public void updateClientRoom ( UpdateRoomPacket updateRoomPacket ) throws Exception {
        if(assureRightPhase(updateRoomPacket)){
            clientSide.updateClientRoom(updateRoomPacket);
        }
    }
    //### VirtualRoomServer
    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        if(assureRightPhase(roomLeavePacket)){
            addSenderNickname(roomLeavePacket);
            serverSide.leaveRoom(roomLeavePacket);
        }
    }

    ///-------------------------------
    // Heartbeat
    @Override
    public void sendHeartbeat() throws Exception {
        // call method on clientStub
        ((Heartbeatable) clientSide).receiveHeartbeat();
    }

    ///---------------------------------------
    // methods for disconnection/reconnection
    @Override
    protected void disconnectProcedure ( DisconnectPacket disconnectPacket ) throws Exception {
        try {
            UnicastRemoteObject.unexportObject(this, true);
        } catch (NoSuchObjectException _) {
            //no problem, already cleaned!
        }
        super.disconnectProcedure(disconnectPacket);
    }

    ///-------------------------------
    // conversions methods
    @Override
    public RmiProxyPlayer convertToRmi(){
        return this;
    }
    @Override
    public SocketProxyPlayer convertToSocket(){
        return new SocketProxyPlayer(server, nickname, currentPhase, serverSide, input, output);
    }

    @Override
    public void reconnect (VirtualClient newClientSide, ObjectInputStream newInput, ObjectOutputStream newOutput) throws Exception {
        clientSide = newClientSide;
        input = newInput;
        output = newOutput;
        if (controller != null) {
            controller.executeCommand(new CommandPacket(PlayerActionEnum.CONNECT));
        }
        changeLocalPhase(ApplicationPhase.GAME);
        this.changePhaseClient(new ChangePhasePacket(ApplicationPhase.GAME));
        serverSide.syncPlayer(this);

    }

}
