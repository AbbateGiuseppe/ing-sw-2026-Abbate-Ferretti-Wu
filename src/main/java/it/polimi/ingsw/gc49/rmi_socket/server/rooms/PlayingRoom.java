package it.polimi.ingsw.gc49.rmi_socket.server.rooms;

import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.uncertain.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.Hall;
import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualGameServerAdapter;

public class PlayingRoom extends Room implements VirtualGameServer {

    public PlayingRoom ( Hall hall, int roomId, int maxNumOfPlayers ) {
        super(hall, roomId, maxNumOfPlayers);
    }


    //### Room's methods
    @Override
    public void enterPlayer ( PhasedProxyPlayer newPlayer ) {
        //Shouldn't be possible to enter a game that already started anyway.
        if(canEnter()) {
            newPlayer.changeLocalPhase(ApplicationPhase.GAME);
            super.enterPlayer(newPlayer);
            newPlayer.setServerSideObject(new VirtualGameServerAdapter(this));
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
