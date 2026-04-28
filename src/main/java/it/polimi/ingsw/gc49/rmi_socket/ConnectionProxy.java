package it.polimi.ingsw.gc49.rmi_socket;

import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.datapacket.uncertain.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.INITIALIZE.HallClientInitializePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.UPDATE.HallClientUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.INITIALIZE.RoomClientInitializePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.UPDATE.RoomClientUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

public abstract class ConnectionProxy implements VirtualClient, VirtualServer {
    public enum ConnectionType { RMI, SOCKET }
    public final ConnectionType connectionType;
    public enum SubclassType { GAME, HALL, ROOM }
    public final SubclassType subclassType;

    public ConnectionProxy ( ConnectionType connectionType, SubclassType subclassType ) {
        this.connectionType = connectionType;
        this.subclassType = subclassType;
    }

    //### VirtualClient
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception {}
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception {}
    public void reportError ( ErrorPacket errorPacket ) throws Exception {}
    //### VirtualServer
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {}
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {}
    public void reconnect ( ReconnectPacket reconnectPacket ) throws Exception {}
    //### VirtualHallClient
    public void initializeClientHall ( HallClientInitializePacket hallClientInitializePacket ) throws Exception {}
    public void updateClientHall ( HallClientUpdatePacket hallClientUpdatePacket ) throws Exception {}
    //### VirtualHallServer
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {}
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {}
    //### VirtualRoomClient
    public void initializeClientRoom ( RoomClientInitializePacket roomClientInitializePacket ) throws Exception {}
    public void updateClientRoom ( RoomClientUpdatePacket roomClientUpdatePacket ) throws Exception {}
    //### VirtualRoomServer
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {}
}
