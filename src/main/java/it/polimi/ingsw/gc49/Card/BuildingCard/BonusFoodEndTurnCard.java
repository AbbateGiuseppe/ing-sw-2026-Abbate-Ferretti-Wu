package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.EventManager;

public class BonusFoodEndTurnCard extends BuildingCard {
    public BonusFoodEndTurnCard ( BuildingEvent buildingEvent, EventManager manager, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, manager, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        if (owner.getAssignedOrderSlot().getFoodGain() > 0) {
            owner.addFood(1);
        }
    }
}
