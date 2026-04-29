package it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.proxyPlayer.inGame;

import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.uncertain.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.proxyPlayer.inHall.ProxyPlayerHallRmi;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.proxyPlayer.inRoom.ProxyPlayerRoomRmi;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualGameClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.ProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualHallServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualRoomServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

public class ProxyPlayerGameRmi extends ProxyPlayer {
    private final VirtualGameServer serverSide;
    private final VirtualGameClient clientSide;

    public ProxyPlayerGameRmi ( ServerMultiplexer server, String nickname,
                                VirtualGameServer serverSide, VirtualClient clientSide ) {
        super( ConnectionType.RMI, SubclassType.GAME, server, nickname );
        this.serverSide = serverSide;
        this.clientSide = clientSide;
    }
    public ProxyPlayerGameRmi ( ProxyPlayer OldProxyPlayer,
                                VirtualGameServer serverSide, VirtualClient clientSide ) {
        super(ConnectionType.RMI, SubclassType.GAME, OldProxyPlayer.server, OldProxyPlayer.nickname);
        this.serverSide = serverSide;
        this.clientSide = clientSide;
    }


    //### from client to server commands
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        addSenderNickname(commandPacket);
        serverSide.sendCommand(commandPacket);
    }
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        addSenderNickname(disconnectPacket);
        serverSide.disconnect(disconnectPacket);
    }
    @Override
    public void reconnect ( ReconnectPacket reconnectPacket ) throws Exception {
        addSenderNickname(reconnectPacket);
        serverSide.reconnect(reconnectPacket);
    }

    //### from server to client command
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

    //### utils
    @Override
    public ProxyPlayer changeSubclass ( SubclassType newSubclass, VirtualServer newServerSide ){
        switch(newSubclass){
            case GAME -> { return this; }
            case HALL -> { return new ProxyPlayerHallRmi(this, (VirtualHallServer) newServerSide, (VirtualClient) this.clientSide); }
            case ROOM -> { return new ProxyPlayerRoomRmi(this, (VirtualRoomServer) newServerSide, (VirtualClient) this.clientSide); }
            default -> { return this; }
        }
    }
}
