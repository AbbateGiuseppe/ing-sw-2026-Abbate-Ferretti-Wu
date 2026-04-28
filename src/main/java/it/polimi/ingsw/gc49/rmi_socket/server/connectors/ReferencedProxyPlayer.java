package it.polimi.ingsw.gc49.rmi_socket.server.connectors;

import it.polimi.ingsw.gc49.datapacket.sentFromClient.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.HALL_COMMAND.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.HALL_COMMAND.HallJoinPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.ROOM_COMMAND.RoomLeavePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.HALL_RETURN.ClientHallInitializePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.HALL_RETURN.ClientHallUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.ROOM_RETURN.ClientRoomInitializePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.ROOM_RETURN.ClientRoomUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.ConnectionProxy;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualHallServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualRoomServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualServer;

import java.rmi.Remote;

public class ReferencedProxyPlayer implements VirtualGameServer, VirtualHallServer, VirtualRoomServer {
    private ProxyPlayer proxy;

    public ReferencedProxyPlayer ( ProxyPlayer proxyPlayer ) {
        this.proxy = proxyPlayer;
    }


    public ProxyPlayer getProxy() {
        return proxy;
    }

    public void changeSubclass ( ConnectionProxy.SubclassType newSubclass, VirtualServer newServerSide ){
        this.proxy = proxy.changeSubclass(newSubclass, newServerSide);
    }

    //### VirtualServer
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        proxy.sendCommand( commandPacket );
    }
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        proxy.disconnect( disconnectPacket );
    }
    public void reconnect ( ReconnectPacket reconnectPacket ) throws Exception {
        proxy.reconnect( reconnectPacket );
    }
    //### VirtualHallServer
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        proxy.joinRoom( hallJoinPacket );
    }
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        proxy.createRoom( hallCreatePacket );
    }
    //### VirtualRoomServer
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        proxy.leaveRoom( roomLeavePacket );
    }
}
