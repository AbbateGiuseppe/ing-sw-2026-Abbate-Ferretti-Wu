package it.polimi.ingsw.gc49.client.view.mockupHall;

import java.io.Serializable;
import java.util.List;

public class MockupRoom implements Serializable {
    public enum RoomType { WAITING, PLAYING }
    public RoomType type;
    public String roomName;
    public int maxNumOfPlayers;
    public List<String> connectedPlayers;

    public MockupRoom(RoomType type, String roomName, int maxNumOfPlayers, List<String> connectedPlayers) {
        this.type = type;
        this.roomName = roomName;
        this.maxNumOfPlayers = maxNumOfPlayers;
        this.connectedPlayers = connectedPlayers;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(roomName);
        if(type == RoomType.WAITING) {
            stringBuilder.append(" (waiting for players): ");
        }else if(type == RoomType.PLAYING) {
            stringBuilder.append(" (in game): ");
        }
        stringBuilder.append(connectedPlayers.size());
        stringBuilder.append("/");
        stringBuilder.append(maxNumOfPlayers);
        stringBuilder.append(" players connected.");
        return stringBuilder.toString();
    }

    public String toStringPlayers() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("PLAYERS: ");
        int i = connectedPlayers.size();
        for(String player : connectedPlayers){
            i--;
            stringBuilder.append(player);
            if(i==0){
                stringBuilder.append(".\n");
            }else{
                stringBuilder.append(", ");
            }
        }
        return stringBuilder.toString();
    }
}
