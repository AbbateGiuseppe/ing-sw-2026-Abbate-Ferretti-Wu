package it.polimi.ingsw.gc49.client.view.mockupHall;

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
        stringBuilder.append("|Players online, in the hall|: ");
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
        stringBuilder.append(" -----------------------------\n");
        stringBuilder.append(" list of rooms:");
        for(MockupRoom room : rooms){
            stringBuilder.append(" ").append(room);
            stringBuilder.append("\n");
        }
        return  stringBuilder.toString();
    }
}
