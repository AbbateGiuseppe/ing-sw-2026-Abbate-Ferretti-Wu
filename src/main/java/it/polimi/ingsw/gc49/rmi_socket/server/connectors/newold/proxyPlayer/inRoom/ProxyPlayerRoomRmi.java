package it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.proxyPlayer.inRoom;

import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.INITIALIZE.RoomClientInitializePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.UPDATE.RoomClientUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.proxyPlayer.inGame.ProxyPlayerGameRmi;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.proxyPlayer.inHall.ProxyPlayerHallRmi;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualRoomClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualHallServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualRoomServer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.ProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

public class ProxyPlayerRoomRmi extends ProxyPlayer {
    private final VirtualRoomServer serverSide;
    private final VirtualRoomClient clientSide;

    public ProxyPlayerRoomRmi ( ServerMultiplexer server, String nickname,
                                VirtualRoomServer serverSide, VirtualClient clientSide ) {
        super( ConnectionType.RMI, SubclassType.ROOM, server, nickname );
        this.serverSide = serverSide;
        this.clientSide = clientSide;
    }
    public ProxyPlayerRoomRmi ( ProxyPlayer OldProxyPlayer,
                                VirtualRoomServer serverSide, VirtualClient clientSide ){
        super( ConnectionType.RMI, SubclassType.ROOM, OldProxyPlayer.server, OldProxyPlayer.nickname );
        this.serverSide = serverSide;
        this.clientSide = clientSide;
    }


    //### from client to server commands
    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        addSenderNickname(roomLeavePacket);
        serverSide.leaveRoom(roomLeavePacket);
    }

    //### from server to client command
    @Override
    public void initializeClientRoom ( RoomClientInitializePacket roomClientInitializePacket ) throws Exception {
        clientSide.initializeClientRoom(roomClientInitializePacket);
    }
    @Override
    public void updateClientRoom ( RoomClientUpdatePacket roomClientUpdatePacket ) throws Exception {
        clientSide.updateClientRoom(roomClientUpdatePacket);
    }

    //### utils
    @Override
    public ProxyPlayer changeSubclass ( SubclassType newSubclass, VirtualServer newServerSide ){
        switch(newSubclass){
            case GAME -> { return new ProxyPlayerGameRmi(this, (VirtualGameServer) newServerSide, (VirtualClient) this.clientSide); }
            case HALL -> { return new ProxyPlayerHallRmi(this, (VirtualHallServer) newServerSide, (VirtualClient) this.clientSide); }
            case ROOM -> { return this; }
            default -> { return this; }
        }
    }
}
