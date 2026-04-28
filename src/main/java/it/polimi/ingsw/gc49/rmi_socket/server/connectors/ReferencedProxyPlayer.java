package it.polimi.ingsw.gc49.rmi_socket.server.connectors;

import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.datapacket.uncertain.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.rmi_socket.ConnectionProxy;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualHallServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualRoomServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

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
