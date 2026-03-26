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
        for(Player player : players) {
            eventManager.invokeEvent(BuildingEvent.RITUAL_PRIOR_EVENT,player.getTotem());
        }

        List<Player> losers = determineLosers(players);
        for(Player player : losers) {
            player.setPointsToPay(minusPoints);
            player.setFoodToPay(0);
        }

        List<Player> winners = determineWinners(players);
        for(Player player : winners) {
             player.setPointsToPay(-plusPoints);
             player.setFoodToPay(0);
        }
        if(winners.size() == 1) {
             winners.getFirst().setUniqueWinner(true);
        }

        for(Player player : players) {
            eventManager.invokeEvent(BuildingEvent.RITUAL_POSTERIOR_EVENT,player.getTotem());
            player.confirmToPay();
        }
    }

    private List<Player> determineWinners(List<Player> players) {
        int maxScore = players.stream()
                .mapToInt(p -> p.data.getNumStars() + p.getTempStars())
                .max()
                .getAsInt();
        return players.stream()
                .filter(p -> p.data.getNumStars() + p.getTempStars() == maxScore)
                .collect(Collectors.toList());
    }

    private List<Player> determineLosers(List<Player> players) {
        int minScore = players.stream()
                .mapToInt(p -> p.data.getNumStars() + p.getTempStars())
                .min()
                .getAsInt();
        return players.stream()
                .filter(p -> p.data.getNumStars() + p.getTempStars() == minScore)
                .collect(Collectors.toList());
    }
}
