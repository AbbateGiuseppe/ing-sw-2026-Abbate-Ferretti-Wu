package it.polimi.ingsw.gc49.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.Card.BuildingCard.BuildingEvent;
import it.polimi.ingsw.gc49.Player;

import java.util.List;

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

        Player[] winners = determineWinners(players);
        Player[] losers = determineLosers(players);

        for(Player player : losers) {
            /*
            * player.setPointsToPay(minusPoints);
            * player.setFoodToPay(0);
            * */
        }

        for(Player player : winners) {
            /*
             * player.setPointsToPay(-plusPoints);
             * player.setFoodToPay(0);
             * */
        }
        if(winners.length == 1) {
            // winners[0].setUniqueWinner(true);
        }

        for(Player player : players) {
            eventManager.invokeEvent(BuildingEvent.RITUAL_POSTERIOR_EVENT,player.getTotem());
            player.confirmToPay();
        }
    }

    // Return the players who have the most stars(using also player.tempStars)
    private Player[] determineWinners(List<Player> players) {
        return null;
    }

    // Return the players who have the least stars(using also player.tempStars)
    private Player[] determineLosers(List<Player> players) {
        return null;
    }
}
