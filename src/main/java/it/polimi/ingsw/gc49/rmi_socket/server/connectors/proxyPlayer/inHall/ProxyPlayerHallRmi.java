package it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inHall;

import it.polimi.ingsw.gc49.datapacket.sentFromServer.HALL_RETURN.ClientHallInitializePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.HALL_RETURN.ClientHallUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.HALL_COMMAND.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.HALL_COMMAND.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.ProxyPlayerConstructor;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inGame.ProxyPlayerGameRmi;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inRoom.ProxyPlayerRoomRmi;
import it.polimi.ingsw.gc49.rmi_socket.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualClients.VirtualHallClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualHallServer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualServer;

public class ProxyPlayerHallRmi extends ProxyPlayerConstructor {
    private final VirtualHallServer serverSide;
    private final VirtualHallClient clientSide;

    public ProxyPlayerHallRmi ( ProxyPlayer proxyPlayer, VirtualServer serverSide,
                                VirtualClient clientSide ) {
        super( proxyPlayer, SubclassType.HALL );
        this.serverSide = serverSide;
        this.clientSide = clientSide;
    }


    //### from client to server commands
    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        addSenderNickname(hallJoinPacket);
        serverSide.joinRoom(hallJoinPacket);
    }
    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        addSenderNickname(hallCreatePacket);
        serverSide.createRoom(hallCreatePacket);
    }

    //### from server to client command
    @Override
    public void initializeClientHall ( ClientHallInitializePacket clientHallInitializePacket ) throws Exception {
        clientSide.initializeClientHall(clientHallInitializePacket);
    }
    @Override
    public void updateClientHall ( ClientHallUpdatePacket clientHallUpdatePacket ) throws Exception {
        clientSide.updateClientHall(clientHallUpdatePacket);
    }

    //### utils
    @Override
    public ProxyPlayer changeSubclass ( SubclassType newSubclass, VirtualServer newServerSide ){
        switch(newSubclass){
            case GAME -> { return new ProxyPlayerGameRmi(this, newServerSide, (VirtualClient) this.clientSide); }
            case HALL -> { return this; }
            case ROOM -> { return new ProxyPlayerRoomRmi(this, newServerSide, (VirtualClient) this.clientSide); }
            default -> { return this; }
        }
    }
}
