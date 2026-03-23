package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.Card.BuildingCard.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.PlayerDataInterface;

public class OneMoreCardStrategy extends AbsBuildingStrategy {
    public OneMoreCardStrategy(BuildingEvent event, EventManager manager) {
        super(event, manager);
    }

    @Override
    public void effect(PlayerDataInterface player) {
        //TODO to be decided and waiting for states
    }

    @Override
    protected boolean condition(PlayerDataInterface playerDataInterface) {
        return true;
    }
}
