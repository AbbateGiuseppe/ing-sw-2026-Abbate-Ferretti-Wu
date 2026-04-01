package it.polimi.ingsw.gc49.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.Player;

import java.util.List;
import java.util.stream.Collectors;

public class RitualEvent extends EventCard {
    private final int plusPoints;
    private final int minusPoints;

    public RitualEvent(int plusPoints, int minusPoints) {
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
}
