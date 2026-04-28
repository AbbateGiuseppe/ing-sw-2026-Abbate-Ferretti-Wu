package it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inRoom;

import it.polimi.ingsw.gc49.datapacket.sentFromServer.ROOM_RETURN.ClientRoomInitializePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.ROOM_RETURN.ClientRoomUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.ROOM_COMMAND.RoomLeavePacket;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.ProxyPlayerConstructor;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inGame.ProxyPlayerGameRmi;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inHall.ProxyPlayerHallRmi;
import it.polimi.ingsw.gc49.rmi_socket.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualClients.VirtualRoomClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualRoomServer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualServer;

public class ProxyPlayerRoomRmi extends ProxyPlayerConstructor {
    private final VirtualRoomServer serverSide;
    private final VirtualRoomClient clientSide;

    public ProxyPlayerRoomRmi ( ProxyPlayer proxyPlayer, VirtualServer serverSide,
                                VirtualClient clientSide ) {
        super( proxyPlayer, SubclassType.ROOM );
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
    public void initializeClientRoom ( ClientRoomInitializePacket clientRoomInitializePacket ) throws Exception {
        clientSide.initializeClientRoom(clientRoomInitializePacket);
    }
    @Override
    public void updateClientRoom ( ClientRoomUpdatePacket clientRoomUpdatePacket ) throws Exception {
        clientSide.updateClientRoom(clientRoomUpdatePacket);
    }

    //### utils
    @Override
    public ProxyPlayer changeSubclass ( SubclassType newSubclass, VirtualServer newServerSide ){
        switch(newSubclass){
            case GAME -> { return new ProxyPlayerGameRmi(this, newServerSide, (VirtualClient) this.clientSide); }
            case HALL -> { return new ProxyPlayerHallRmi(this, newServerSide, (VirtualClient) this.clientSide); }
            case ROOM -> { return this; }
            default -> { return this; }
        }
    }
}
