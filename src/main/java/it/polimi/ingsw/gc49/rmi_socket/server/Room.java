package it.polimi.ingsw.gc49.rmi_socket.server;

import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ProxyPlayer;

import java.util.ArrayList;
import java.util.List;

public class Room {
    public final int MaxNumOfPlayers;
    private int numConnectedPlayers;
    private final List<ProxyPlayer> players = new ArrayList<>();

    public Room(int MaxNumOfPlayers) {
        this.MaxNumOfPlayers = MaxNumOfPlayers;
        numConnectedPlayers = 0;
    }

    public void enterPlayer ( ProxyPlayer newPlayer ) {
        if( numConnectedPlayers >= MaxNumOfPlayers ) {
            return;
        }else{
            numConnectedPlayers++;
            players.add(newPlayer);
        }
    }
}
