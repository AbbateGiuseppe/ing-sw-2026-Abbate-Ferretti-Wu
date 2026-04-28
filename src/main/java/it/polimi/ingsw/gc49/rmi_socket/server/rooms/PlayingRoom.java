package it.polimi.ingsw.gc49.rmi_socket.server.rooms;

import it.polimi.ingsw.gc49.datapacket.sentFromClient.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.ConnectionProxy;
import it.polimi.ingsw.gc49.rmi_socket.server.Hall;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ReferencedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualServer;

public class PlayingRoom extends Room implements VirtualGameServer {

    public PlayingRoom ( Hall hall, int roomId, int maxNumOfPlayers ) {
        super(hall, roomId, maxNumOfPlayers);
    }


    //### Room's methods
    @Override
    public void enterPlayer ( ReferencedProxyPlayer newPlayer ) {
        //Shouldn't be possible to enter a game that already started anyway.
        if(canEnter()) {
            newPlayer.changeSubclass(ConnectionProxy.SubclassType.GAME, (VirtualServer) this);
            super.enterPlayer(newPlayer);
        }else{
            throw new RuntimeException("La partita è già iniziata, non puoi entrare nella stanza " + roomId + "." );
        }
    }
    @Override
    protected boolean canEnter() {
        return false;
    }


    //### client's commands
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {

    }
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {

    }
    @Override
    public void reconnect ( ReconnectPacket reconnectPacket ) throws Exception {

    }
}
