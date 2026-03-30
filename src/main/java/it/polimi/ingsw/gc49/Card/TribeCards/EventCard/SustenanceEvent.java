package it.polimi.ingsw.gc49.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.Model;
import it.polimi.ingsw.gc49.Player;

import java.util.List;

public class SustenanceEvent extends EventCard {
    private final int minusPoints;

    public SustenanceEvent(int minusPoints) {
        this.minusPoints = minusPoints;
    }

    @Override
    public void resolveEvent(Model model) {
        for(Player player : model.getPlayers()) {
            // the player has to pay food equal to the number of the charactercards he has
            // and subtract it by the discount of the gatherers
            player.setFoodToPay(Math.max(0, player.data.getNumCharacters() - player.data.getNumSustenanceDiscount()));
            player.setPointsToPay(0);
        }
        // Effect num 2
        eventManager.invokeEvent(BuildingEvent.SUSTENANCE_EVENT,null);

        for(Player player : model.getPlayers()) {
            // if the player doesn't have enough food,subtract from his points
            if(player.getFood() < player.getFoodToPay()) {
                  player.setPointsToPay((player.getFoodToPay() - player.getFood()) * minusPoints);
                  player.setFoodToPay(player.getFood());
            }
            player.confirmToPay();
        }
    }
}
