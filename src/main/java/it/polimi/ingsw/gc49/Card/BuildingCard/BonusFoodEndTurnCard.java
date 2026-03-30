package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;

public class BonusFoodEndTurnCard extends BuildingCard {
    public BonusFoodEndTurnCard(BuildingEvent buildingEvent, EventManager manager, int PPReward, int foodPrice) {
        super(buildingEvent, manager, PPReward, foodPrice);
    }

    @Override
    public void onEventEffect() {
        if (owner.getAssignedOrderSlot().getFoodGain() > 0) {
            owner.addFood(1);
        }
    }
}
