package it.polimi.ingsw.gc49.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.model.BuildingEvent;
import it.polimi.ingsw.gc49.model.Era;

public class BonusFoodEndTurnCard extends BuildingCard {
    public BonusFoodEndTurnCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        if (owner.getAssignedOrderSlot().getFoodGain() > 0) {
            owner.addFood(1);
        }
    }
}
