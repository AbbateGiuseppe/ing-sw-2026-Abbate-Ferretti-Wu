package it.polimi.ingsw.gc49.View.mockupHall;

import java.io.Serializable;
import java.util.List;

public class MockupHall implements Serializable {
    public List<String> connectedPlayers;
    public List<MockupRoom> rooms;

    public MockupHall(List<String> connectedPlayers, List<MockupRoom> rooms) {
        this.connectedPlayers = connectedPlayers;
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Online players: ");
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
        for(MockupRoom room : rooms){
            stringBuilder.append(room);
            stringBuilder.append("\n");
        }
        return  stringBuilder.toString();
    }
}
