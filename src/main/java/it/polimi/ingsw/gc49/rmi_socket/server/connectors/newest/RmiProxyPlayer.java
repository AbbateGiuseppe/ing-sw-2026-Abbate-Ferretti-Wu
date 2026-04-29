package it.polimi.ingsw.gc49.rmi_socket.server.connectors.newest;

import it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.INITIALIZE.HallClientInitializePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.UPDATE.HallClientUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.INITIALIZE.RoomClientInitializePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.UPDATE.RoomClientUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.datapacket.uncertain.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

public class RmiProxyPlayer extends PhasedProxyPlayer {
    public RmiProxyPlayer ( ServerMultiplexer server, String nickname,
                            ApplicationPhase startingPhase,
                            VirtualServer serverSide, VirtualClient clientSide ) {
        super(server, nickname, startingPhase, serverSide, clientSide, null, null);
    }

    //###################
    //### VirtualClient
    @Override
    public void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception {
        changeLocalPhase(changePhasePacket.newPhase);
        clientSide.changePhaseClient(changePhasePacket);
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
            serverSide.sendCommand(commandPacket);
        }
    }
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        if(assureRightPhase(disconnectPacket)){
            addSenderNickname(disconnectPacket);
            serverSide.disconnect(disconnectPacket);
        }
    }
    @Override
    public void reconnect ( ReconnectPacket reconnectPacket ) throws Exception {
        if(assureRightPhase(reconnectPacket)){
            addSenderNickname(reconnectPacket);
            serverSide.reconnect(reconnectPacket);
        }
    }
    //### VirtualHallClient
    @Override
    public void initializeClientHall ( HallClientInitializePacket hallClientInitializePacket ) throws Exception {
        if(assureRightPhase(hallClientInitializePacket)){
            clientSide.initializeClientHall(hallClientInitializePacket);
        }
    }
    @Override
    public void updateClientHall ( HallClientUpdatePacket hallClientUpdatePacket ) throws Exception {
        if(assureRightPhase(hallClientUpdatePacket)){
            clientSide.updateClientHall(hallClientUpdatePacket);
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
    public void initializeClientRoom ( RoomClientInitializePacket roomClientInitializePacket ) throws Exception {
        if(assureRightPhase(roomClientInitializePacket)){
            clientSide.initializeClientRoom(roomClientInitializePacket);
        }
    }
    @Override
    public void updateClientRoom ( RoomClientUpdatePacket roomClientUpdatePacket ) throws Exception {
        if(assureRightPhase(roomClientUpdatePacket)){
            clientSide.updateClientRoom(roomClientUpdatePacket);
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
}
