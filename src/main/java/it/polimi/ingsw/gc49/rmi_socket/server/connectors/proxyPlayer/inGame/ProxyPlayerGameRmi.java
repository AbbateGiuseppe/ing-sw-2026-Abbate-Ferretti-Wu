package it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inGame;

import it.polimi.ingsw.gc49.datapacket.sentFromClient.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.ProxyPlayerConstructor;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inHall.ProxyPlayerHallRmi;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inRoom.ProxyPlayerRoomRmi;
import it.polimi.ingsw.gc49.rmi_socket.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualClients.VirtualGameClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualServer;

public class ProxyPlayerGameRmi extends ProxyPlayerConstructor {
    private final VirtualGameServer serverSide;
    private final VirtualGameClient clientSide;

    public ProxyPlayerGameRmi ( ProxyPlayer proxyPlayer, VirtualServer serverSide, VirtualClient clientSide ) {
        super( proxyPlayer, SubclassType.GAME );
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
            case HALL -> { return new ProxyPlayerHallRmi(this, newServerSide, (VirtualClient) this.clientSide); }
            case ROOM -> { return new ProxyPlayerRoomRmi(this, newServerSide, (VirtualClient) this.clientSide); }
            default -> { return this; }
        }
    }
}
