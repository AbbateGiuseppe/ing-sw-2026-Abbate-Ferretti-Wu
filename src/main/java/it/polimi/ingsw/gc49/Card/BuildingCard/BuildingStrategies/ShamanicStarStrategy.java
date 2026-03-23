package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.Card.BuildingCard.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.PlayerDataInterface;

public class ShamanicStarStrategy extends AbsBuildingStrategy {
    public ShamanicStarStrategy(BuildingEvent event, EventManager manager) {
        super(event, manager);
    }

    @Override
    public void effect(PlayerDataInterface player) {
        player.addTempStars(3);
    }

    @Override
    protected boolean condition(PlayerDataInterface playerDataInterface) {
        return true;
    }
}
