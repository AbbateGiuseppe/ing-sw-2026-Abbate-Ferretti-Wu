package it.polimi.ingsw.gc49.client.view.mockupHall;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.Serializable;
import java.util.List;

/**
 * The {@code MockupRoom} class represents the client-side snapshot of a game room.
 * It provides the user interface with essential information about the room's status,
 * including its type (waiting or playing), its capacity, and the players currently inside.
 */
public class MockupRoom implements Serializable {

    /**
     * Defines the current state of the room.
     */
    public enum RoomType { WAITING, PLAYING }

    /** The current state of this room (WAITING or PLAYING). */
    public RoomType type;
    public String roomName;
    public int maxNumOfPlayers;
    public List<String> connectedPlayers;

    /**
     * Constructs a new {@code MockupRoom} snapshot.
     *
     * @param type             the current state of the room.
     * @param roomName         the unique name of the room.
     * @param maxNumOfPlayers  the maximum capacity of the room.
     * @param connectedPlayers the list of players currently inside.
     */
    public MockupRoom(RoomType type, String roomName, int maxNumOfPlayers, List<String> connectedPlayers) {
        this.type = type;
        this.roomName = roomName;
        this.maxNumOfPlayers = maxNumOfPlayers;
        this.connectedPlayers = connectedPlayers;
    }

    /**
     * Generates a stylized, single-line string summarizing the room's status.
     * The room name is colored in cyan, followed by a text description of its state
     * (waiting or in game) and the current player count versus the maximum capacity.
     * <p>
     * Example: {@code Room1 (it's waiting for players): 2/4 players are inside.}
     *
     * @return an {@link AttributedString} representing the room's summary.
     */
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


    /**
     * Generates a stylized string listing all the players currently inside the room.
     * The players' nicknames are colored in yellow and comma-separated.
     *
     * @return an {@link AttributedString} containing the list of connected players.
     */
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
