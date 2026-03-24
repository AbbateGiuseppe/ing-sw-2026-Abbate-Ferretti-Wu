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
            *  player.foodToPay = databank.numCharacters - databank.numSustenanceDiscount;
            *  eventManager.invokeEvent(BuildingEvent.SUSTENANCE_EVENT,player.getTotem()); // It modifies player.foodToPay
            *   if(player.foodToPay > 0 && player.food >= player.foodToPay) player.food -= player.foodToPay;
            *   else if (player.food < foodToPay) {player.points -= (player.foodToPay - player.food) * minusPoints;player.food = 0;}
            * */
        }
    }
}
