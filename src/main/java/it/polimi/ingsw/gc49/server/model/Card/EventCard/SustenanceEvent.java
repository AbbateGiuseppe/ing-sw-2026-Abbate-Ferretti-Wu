package it.polimi.ingsw.gc49.server.model.Card.EventCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.EventManager;
import it.polimi.ingsw.gc49.server.model.Player;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.List;

public class SustenanceEvent extends EventCard {
    private final int minusPoints;

    public SustenanceEvent( int minusPoints, EventManager eventManager, Era era, int minNumPlayers ) {
        super(eventManager, era, minNumPlayers);
        this.minusPoints = minusPoints;
    }

    @Override
    public void resolveEvent(List<Player> players) {
        for(Player player : players) {
            // the player has to pay food equal to the number of the charactercards he has
            // and subtract it by the discount of the gatherers
            player.setFoodToPay(Math.max(0, player.data.getNumCharacters() - player.data.getNumSustenanceDiscount()));
            player.setPointsToPay(0);
        }
        // Effect num 2
        eventManager.invokeEvent(BuildingEvent.SUSTENANCE_EVENT);

        for(Player player : players) {
            // if the player doesn't have enough food,subtract from his points
            if(player.getFood() < player.getFoodToPay()) {
                  player.setPointsToPay((player.getFoodToPay() - player.getFood()) * minusPoints);
                  player.setFoodToPay(player.getFood());
            }
            player.confirmToPay();
        }
    }

    @Override
    public String toString() {
        return "Sustenance {\n" +
                " era = " + era +
                ", minusPoints = " + minusPoints +
                "\n}";
    }

    @Override
    public String simpleToString () {
        return "SOSTENTAMENTO";
    }

    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("│€│")
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
