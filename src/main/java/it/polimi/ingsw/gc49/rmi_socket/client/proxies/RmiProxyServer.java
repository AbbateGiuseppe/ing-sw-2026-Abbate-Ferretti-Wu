package it.polimi.ingsw.gc49.rmi_socket.client.proxies;

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
import it.polimi.ingsw.gc49.rmi_socket.client.ClientApplication;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class RmiProxyServer extends PhasedProxyServer {
    public RmiProxyServer ( VirtualClient clientSide ) {
        super(clientSide);
    }

    @Override
    public void finishInitialization ( VirtualServer serverSide, ObjectInputStream input, ObjectOutputStream output ) {
        this.serverSide = serverSide;
    }

    //###################
    //### VirtualClient
    @Override
    public void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception {
        clientSide.changePhaseClient(changePhasePacket);
    }
    //#######################
    //### VirtualGameClient
    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception {
        clientSide.initializeClientModel(initializeModelPacket);
    }
    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception {
        clientSide.updateClientModel(updateModelPacket);
    }
    @Override
    public void reportError ( ErrorPacket errorPacket ) throws Exception {
        clientSide.reportError(errorPacket);
    }
    //### VirtualGameServer
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        serverSide.sendCommand(commandPacket);
    }
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        serverSide.disconnect(disconnectPacket);
    }
    @Override
    public void reconnect ( ReconnectPacket reconnectPacket ) throws Exception {
        serverSide.reconnect(reconnectPacket);
    }
    //### VirtualHallClient
    @Override
    public void initializeClientHall ( HallClientInitializePacket hallClientInitializePacket ) throws Exception {
        clientSide.initializeClientHall(hallClientInitializePacket);
    }
    @Override
    public void updateClientHall ( HallClientUpdatePacket hallClientUpdatePacket ) throws Exception {
        clientSide.updateClientHall(hallClientUpdatePacket);
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
    public void initializeClientRoom ( RoomClientInitializePacket roomClientInitializePacket ) throws Exception {
        clientSide.initializeClientRoom(roomClientInitializePacket);
    }
    @Override
    public void updateClientRoom ( RoomClientUpdatePacket roomClientUpdatePacket ) throws Exception {
        clientSide.updateClientRoom(roomClientUpdatePacket);
    }
    //### VirtualRoomServer
    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        serverSide.leaveRoom(roomLeavePacket);
    }
}
