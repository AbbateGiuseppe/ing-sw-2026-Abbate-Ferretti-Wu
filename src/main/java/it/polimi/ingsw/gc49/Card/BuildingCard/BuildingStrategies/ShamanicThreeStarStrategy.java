package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.Card.BuildingCard.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.PlayerDataInterface;

public class ShamanicThreeStarStrategy extends AbsBuildingStrategy {
    public ShamanicThreeStarStrategy(BuildingEvent event, EventManager manager) {
        super(event, manager);
    }

    @Override
    public void effect(PlayerDataInterface player) {
        // player.addTempStars();
    }

    @Override
    protected boolean condition(PlayerDataInterface playerDataInterface) {
        return true;
    }
}
