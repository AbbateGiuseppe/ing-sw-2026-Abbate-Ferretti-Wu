package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.EventManager;

public class ShamanicImmunityCard extends BuildingCard {
    public ShamanicImmunityCard ( BuildingEvent buildingEvent, EventManager manager, int pointsReward, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, manager, pointsReward, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        if (owner.getPointsToPay() > 0) {
            owner.setPointsToPay(0);
        }
    }
}
