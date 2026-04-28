package it.polimi.ingsw.gc49.rmi_socket.server.rooms;

import it.polimi.ingsw.gc49.rmi_socket.server.Hall;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ReferencedProxyPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class Room {
    protected final Hall hall;
    public final int roomId;
    public final int maxNumOfPlayers;
    protected int numConnectedPlayers;
    protected final List<ReferencedProxyPlayer> players = new ArrayList<>();

    public Room ( Hall hall, int roomId, int maxNumOfPlayers ) {
        this.hall = hall;
        this.roomId = roomId;
        this.maxNumOfPlayers = maxNumOfPlayers;
        numConnectedPlayers = 0;
    }

    public void enterPlayer ( ReferencedProxyPlayer newPlayer ) {
        numConnectedPlayers++;
        players.add(newPlayer);
    }
    protected abstract boolean canEnter();

    public int getNumConnectedPlayers () {
        return numConnectedPlayers;
    }

    protected ReferencedProxyPlayer getPlayerByString ( String nickname ) {
        for( ReferencedProxyPlayer player : players ) {
            if(nickname.equals(player.getProxy().nickname)){
                return player;
            }
        }
        return null;
    }
}
