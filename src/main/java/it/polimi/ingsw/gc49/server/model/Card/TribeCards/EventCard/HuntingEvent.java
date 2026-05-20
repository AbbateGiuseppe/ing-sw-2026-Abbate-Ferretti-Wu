package it.polimi.ingsw.gc49.server.model.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.*;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.List;

public class HuntingEvent extends EventCard {
    private final int pointsPerHunter;

    public HuntingEvent( int pointsPerHunter, EventManager eventManager, Era era, int minNumPlayers ) {
        super(eventManager, era, minNumPlayers);
        this.pointsPerHunter = pointsPerHunter;
    }

    @Override
    public void resolveEvent(List<Player> players) {
        // Setup the initial amount of food and points each player gets during the event
        for(Player player : players) {
            player.setFoodToPay(-player.data.getCharacterCount(CharacterType.Hunter));
            player.setPointsToPay(-player.data.getCharacterCount(CharacterType.Hunter) * pointsPerHunter);
        }
        // Trigger the effect(effect number 8) of the building card,
        // the building card modifies foodToPay and pointsToPay of the player
        eventManager.invokeEvent(BuildingEvent.HUNTING_EVENT);
        // Finalize the change on food and points of the player
        for(Player player : players) {
            player.confirmToPay();
        }
    }

    @Override
    public String toString() {
        return "Hunt {\n" +
                " era = " + era +
                ", pointsPerHunter = " + pointsPerHunter +
                "\n}";
    }

    @Override
    public String simpleToString () {
        return "CACCIA";
    }

    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("│%│")
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
