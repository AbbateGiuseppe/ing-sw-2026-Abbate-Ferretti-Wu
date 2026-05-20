package it.polimi.ingsw.gc49.server.model.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.EventManager;
import it.polimi.ingsw.gc49.server.model.Player;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.List;
import java.util.stream.Collectors;

public class RitualEvent extends EventCard {
    private final int plusPoints;
    private final int minusPoints;

    public RitualEvent( int plusPoints, int minusPoints, EventManager eventManager, Era era, int minNumPlayers ) {
        super(eventManager, era, minNumPlayers);
        this.plusPoints = plusPoints;
        this.minusPoints = minusPoints;
    }

    @Override
    public void resolveEvent(List<Player> players) {
        List<Player> losers = determineLosers(players);
        for(Player player : losers) {
            player.setFoodToPay(0);
            player.setPointsToPay(minusPoints);
        }

        List<Player> winners = determineWinners(players);
        for(Player player : winners) {
            player.setFoodToPay(0);
             player.setPointsToPay(-plusPoints);
        }

        // If there is only one winner then set the flag to true for effect num 7
        if(winners.size() == 1) {
             winners.getFirst().setUniqueWinner(true);
        }

        // Effect num 3 and 7
        eventManager.invokeEvent(BuildingEvent.RITUAL_EVENT);

        for(Player player : players) {
            player.confirmToPay();
        }
    }

    // Returns the players with the most number of stars
    private List<Player> determineWinners(List<Player> players) {
        int maxScore = players.stream()
                .mapToInt(p -> p.data.getNumStars())
                .max()
                .getAsInt();
        return players.stream()
                .filter(p -> p.data.getNumStars() == maxScore)
                .collect(Collectors.toList());
    }

    // Returns the players with the fewest number of stars
    private List<Player> determineLosers(List<Player> players) {
        int minScore = players.stream()
                .mapToInt(p -> p.data.getNumStars())
                .min()
                .getAsInt();
        return players.stream()
                .filter(p -> p.data.getNumStars()  == minScore)
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "Shamanic ritual {\n" +
                " era = " + era +
                ", plusPoints = " + plusPoints +
                ", minusPoints = " + minusPoints +
                "\n}";
    }

    @Override
    public String simpleToString () {
        return "RITUALE SCIAMANICO";
    }

    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("│§│")
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
