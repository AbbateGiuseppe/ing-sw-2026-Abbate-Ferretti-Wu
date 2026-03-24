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
        Player[] winners = determineWinners(players);
        Player[] losers = determineLosers(players);

        for(Player player : losers) {
            // player.pointsToPay = minusPoints;
            eventManager.invokeEvent(BuildingEvent.RITUAL_LOSING_EVENT,player.getTotem());
            // player.points -= player.pointsToPay;
        }

        if(winners.length == 1) {
            // winners[0].pointsToPay = -plusPoints;
            eventManager.invokeEvent(BuildingEvent.RITUAL_WINNING_EVENT,winners[0].getTotem());
            // winners[0].points -= winners[0].pointsToPay;
        } else {
            for(Player player : winners) {
                // player.points += plusPoints;
            }
        }



    }

    // Return the players who have the most stars(using also databank.extraStars)
    private Player[] determineWinners(List<Player> players) {
        return null;
    }

    // Return the players who have the least stars(using also databank.extraStars)
    private Player[] determineLosers(List<Player> players) {
        return null;
    }
}
