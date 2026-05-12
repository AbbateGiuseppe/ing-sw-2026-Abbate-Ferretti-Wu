package it.polimi.ingsw.gc49.client.view.mockupHall;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

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

    public AttributedString toAttributedString() {
        AttributedStringBuilder stringBuilder = new AttributedStringBuilder();
        stringBuilder
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)).append(roomName)
                .style(AttributedStyle.DEFAULT); //restores style
        if(type == RoomType.WAITING) {
            stringBuilder.append(" (it's waiting for players): ");
        }else if(type == RoomType.PLAYING) {
            stringBuilder.append(" (it's in game): ");
        }
        stringBuilder.append(String.valueOf(connectedPlayers.size()));
        stringBuilder.append("/");
        stringBuilder.append(String.valueOf(maxNumOfPlayers));
        stringBuilder.append(" players are inside.");
        return stringBuilder.toAttributedString();
    }

    public AttributedString toAttributedStringPlayers () {
        AttributedStringBuilder stringBuilder = new AttributedStringBuilder();
        stringBuilder.append(" Players online, in the room: ");
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
        return stringBuilder.toAttributedString();
    }
}
