package it.polimi.ingsw.gc49.client.view.mockupHall;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.Serializable;
import java.util.List;

public class MockupHall implements Serializable {
    public List<String> connectedPlayers;
    public List<MockupRoom> rooms;

    public MockupHall(List<String> connectedPlayers, List<MockupRoom> rooms) {
        this.connectedPlayers = connectedPlayers;
        this.rooms = rooms;
    }

    public AttributedString toAttributedString() {
        AttributedStringBuilder stringBuilder = new AttributedStringBuilder();
        stringBuilder.append("|Players online, in the hall|: ");
        int i = connectedPlayers.size();
        for(String player : connectedPlayers){
            i--;
            stringBuilder
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append(player)
                    .style(AttributedStyle.DEFAULT); //restores style
            if(i==0){
                stringBuilder.append(".\n");
            }else{
                stringBuilder.append(", ");
            }
        }
        stringBuilder.append(" -----------------------------\n");
        stringBuilder.append(" list of rooms:\n");
        for(MockupRoom room : rooms){
            stringBuilder.append(" ").append(room.toAttributedString());
            stringBuilder.append("\n");
        }
        return  stringBuilder.toAttributedString();
    }
}
