package it.polimi.ingsw.gc49.model.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.model.BuildingEvent;
import it.polimi.ingsw.gc49.model.Era;
import it.polimi.ingsw.gc49.model.EventManager;
import it.polimi.ingsw.gc49.model.Player;

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
}
