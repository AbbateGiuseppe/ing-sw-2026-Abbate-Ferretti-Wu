package it.polimi.ingsw.gc49.client.view.mockupHall;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.Serializable;
import java.util.List;


/**
 * The {@code MockupHall} class represents the client-side snapshot of the main lobby (Hall).
 * It contains the current state of players who are connected but not yet in a game,
 * as well as the list of all currently active rooms (both waiting and playing).
 * This data is used by the client's user interface to render the main menu and matchmaking screen.
 */
public class MockupHall implements Serializable {
    public List<String> connectedPlayers;
    public List<MockupRoom> rooms;


    /**
     * Constructs a new {@code MockupHall} snapshot.
     *
     * @param connectedPlayers the list of nicknames of players in the hall.
     * @param rooms            the list of current {@link MockupRoom}s.
     */
    public MockupHall(List<String> connectedPlayers, List<MockupRoom> rooms) {
        this.connectedPlayers = connectedPlayers;
        this.rooms = rooms;
    }


    /**
     * Generates a stylized string representation of the hall's current state.
     * It lists all connected players in the hall (color-coded in yellow to indicate
     * they are waiting) followed by a formatted list of all available rooms.
     *
     * @return an {@link AttributedString} ready to be printed on a JLine terminal.
     */
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
