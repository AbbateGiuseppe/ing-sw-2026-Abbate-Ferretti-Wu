package it.polimi.ingsw.gc49.rmi_socket;

import it.polimi.ingsw.gc49.datapacket.sentFromClient.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.HALL_COMMAND.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.HALL_COMMAND.HallJoinPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.ROOM_COMMAND.RoomLeavePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.HALL_RETURN.ClientHallInitializePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.HALL_RETURN.ClientHallUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.ROOM_RETURN.ClientRoomInitializePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.ROOM_RETURN.ClientRoomUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualServer;

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
    public void initializeClientHall ( ClientHallInitializePacket clientHallInitializePacket ) throws Exception {}
    public void updateClientHall ( ClientHallUpdatePacket clientHallUpdatePacket ) throws Exception {}
    //### VirtualHallServer
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {}
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {}
    //### VirtualRoomClient
    public void initializeClientRoom ( ClientRoomInitializePacket clientRoomInitializePacket ) throws Exception {}
    public void updateClientRoom ( ClientRoomUpdatePacket clientRoomUpdatePacket ) throws Exception {}
    //### VirtualRoomServer
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {}
}
