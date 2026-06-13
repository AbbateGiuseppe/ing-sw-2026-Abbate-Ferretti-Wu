package it.polimi.ingsw.gc49.server.model.Card.EventCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.TextModelElement;
import it.polimi.ingsw.gc49.server.model.*;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.List;

public class PaintingEvent extends EventCard {
    // threshold is the minimum number of the Artist cards in order to get plusPoints,otherwise the player gets minusPoints
    private final int threshold;
    private final int plusPoints;
    private final int minusPoints;

    public PaintingEvent( int threshold, int plusPoints, int minusPoints, EventManager eventManager, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(eventManager, era, minNumPlayers, queueUpdater);
        this.threshold = threshold;
        this.plusPoints = plusPoints;
        this.minusPoints = minusPoints;
    }

    @Override
    public void resolveEvent(List<Player> players) {
        for(Player player : players) {
            player.setFoodToPay(0);
            if (player.data.getCharacterCount(CharacterType.Artist) < threshold) {
                player.setPointsToPay(minusPoints);
            } else {
                player.setPointsToPay(-plusPoints * player.data.getCharacterCount(CharacterType.Artist));
            }
        }
        // Effect num 10
        eventManager.invokeEvent(BuildingEvent.PAINTING_EVENT);

        for(Player player : players) {
            player.confirmToPay();
        }

        if(queueUpdater != null) {
            queueUpdater.queueUpdateModelElement(new TextModelElement(
                    "La carta evento " + simpleToString() + " si è attivata fornendo "
                            + plusPoints + ", altrimenti -" + minusPoints + ", a chi possiede almeno " + threshold + " artisti"
            ));
        }
    }

    @Override
    public String toString() {
        return "Paintings {\n" +
                " era = " + era +
                ", threshold = " + threshold +
                ", plusPoints = " + plusPoints +
                ", minusPoints = " + minusPoints +
                "\n}";
    }

    @Override
    public String simpleToString () {
        return "PITTURE RUPESTRI";
    }

    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("│¤│")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("└─┘")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("║")
                .append("╚═══╝").toAttributedString();
        int height = 4;
        int width = 5;
        return new RectangleAttributedString(height, width, attributedString);
    }
}
