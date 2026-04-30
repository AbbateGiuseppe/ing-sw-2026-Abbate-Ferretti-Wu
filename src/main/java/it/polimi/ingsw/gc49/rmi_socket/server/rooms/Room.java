package it.polimi.ingsw.gc49.rmi_socket.server.rooms;

import it.polimi.ingsw.gc49.View.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.rmi_socket.server.Hall;
import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;

import java.util.ArrayList;
import java.util.List;

public abstract class Room {
    protected final Hall hall;
    public final String roomName;
    public final int maxNumOfPlayers;
    protected final List<PhasedProxyPlayer> players = new ArrayList<>();

    public Room ( Hall hall, String roomName, int maxNumOfPlayers ) {
        this.hall = hall;
        this.roomName = roomName;
        this.maxNumOfPlayers = maxNumOfPlayers;
    }
    public Room ( Hall hall, String roomName, int maxNumOfPlayers, List<PhasedProxyPlayer> players ) {
        this.hall = hall;
        this.roomName = roomName;
        this.maxNumOfPlayers = maxNumOfPlayers;
        this.players.addAll(players);
    }

    public void enterPlayer ( PhasedProxyPlayer newPlayer ) throws Exception {
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

    protected List<String> getListPlayerNicknames () {
        List<String> nicknames = new ArrayList<>();
        for( PhasedProxyPlayer player : players ) {
            nicknames.add(player.nickname);
        }
        return nicknames;
    }

    public abstract MockupRoom giveMockupRoom ();
}
