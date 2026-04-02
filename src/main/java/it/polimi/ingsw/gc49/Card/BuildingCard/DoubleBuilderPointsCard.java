package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.EventManager;

public class DoubleBuilderPointsCard extends BuildingCard {
    public DoubleBuilderPointsCard ( BuildingEvent buildingEvent, EventManager manager, int pointsReward, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, manager, pointsReward, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        owner.data.doubleNumBuilderPoints();
    }
}
