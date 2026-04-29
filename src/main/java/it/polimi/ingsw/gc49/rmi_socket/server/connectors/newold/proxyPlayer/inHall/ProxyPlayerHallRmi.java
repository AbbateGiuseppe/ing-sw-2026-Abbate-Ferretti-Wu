package it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.proxyPlayer.inHall;

import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.INITIALIZE.HallClientInitializePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.UPDATE.HallClientUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.proxyPlayer.inGame.ProxyPlayerGameRmi;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.proxyPlayer.inRoom.ProxyPlayerRoomRmi;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualHallClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualHallServer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.ProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualRoomServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

public class ProxyPlayerHallRmi extends ProxyPlayer {
    private final VirtualHallServer serverSide;
    private final VirtualHallClient clientSide;

    public ProxyPlayerHallRmi ( ServerMultiplexer server, String nickname,
                                VirtualHallServer serverSide, VirtualClient clientSide ) {
        super( ConnectionType.RMI, SubclassType.HALL, server, nickname );
        this.serverSide = serverSide;
        this.clientSide = clientSide;
    }
    public ProxyPlayerHallRmi ( ProxyPlayer OldProxyPlayer,
                                VirtualHallServer serverSide, VirtualClient clientSide ){
        super( ConnectionType.RMI, SubclassType.HALL, OldProxyPlayer.server, OldProxyPlayer.nickname );
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
    public void initializeClientHall ( HallClientInitializePacket hallClientInitializePacket ) throws Exception {
        clientSide.initializeClientHall(hallClientInitializePacket);
    }
    @Override
    public void updateClientHall ( HallClientUpdatePacket hallClientUpdatePacket ) throws Exception {
        clientSide.updateClientHall(hallClientUpdatePacket);
    }

    //### utils
    @Override
    public ProxyPlayer changeSubclass ( SubclassType newSubclass, VirtualServer newServerSide ){
        switch(newSubclass){
            case GAME -> { return new ProxyPlayerGameRmi(this, (VirtualGameServer) newServerSide, (VirtualClient) this.clientSide); }
            case HALL -> { return this; }
            case ROOM -> { return new ProxyPlayerRoomRmi(this, (VirtualRoomServer) newServerSide, (VirtualClient) this.clientSide); }
            default -> { return this; }
        }
    }
}
