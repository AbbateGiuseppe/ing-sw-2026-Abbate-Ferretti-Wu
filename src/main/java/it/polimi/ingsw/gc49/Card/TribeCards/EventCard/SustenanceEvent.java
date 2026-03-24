package it.polimi.ingsw.gc49.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.Card.BuildingCard.BuildingEvent;
import it.polimi.ingsw.gc49.Player;

import java.util.List;

public class SustenanceEvent extends EventCard {
    private final int minusPoints;

    public SustenanceEvent(int minusPoints) {
        this.minusPoints = minusPoints;
    }

    @Override
    public void resolveEvent(List<Player> players) {
        for(Player player : players) {
            /*
            *  player.setFoodToPay(Math.max(0,databank.numCharacters - databank.numSustenanceDiscount));
            *  player.setPointsToPay(0);
            *  eventManager.invokeEvent(BuildingEvent.SUSTENANCE_EVENT,player.getTotem());
            *  if(player.getFood() < player.getFoodToPay()) {
            *       player.setPointsToPay((player.getFoodToPay() - player.getFood()) * minusPoints);
            *       player.setFoodToPay(player.getFood())
            * }
            * player.confirmToPay();
            * */
        }
    }
}
