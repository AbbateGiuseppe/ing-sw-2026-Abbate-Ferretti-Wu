package it.polimi.ingsw.gc49.rmi_socket.server.rooms;

import it.polimi.ingsw.gc49.rmi_socket.server.Hall;
import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class Room {
    protected final Hall hall;
    public final int roomId;
    public final int maxNumOfPlayers;
    protected final List<PhasedProxyPlayer> players = new ArrayList<>();

    public Room ( Hall hall, int roomId, int maxNumOfPlayers ) {
        this.hall = hall;
        this.roomId = roomId;
        this.maxNumOfPlayers = maxNumOfPlayers;
    }

    public void enterPlayer ( PhasedProxyPlayer newPlayer ) {
        players.add(newPlayer);
    }
    protected abstract boolean canEnter();

    public int getNumConnectedPlayers () {
        return players.size();
    }

    protected PhasedProxyPlayer getPlayerByString ( String nickname ) {
        for( PhasedProxyPlayer player : players ) {
            if(nickname.equals(player.nickname)){
                return player;
            }
        }
        return null;
    }
}
