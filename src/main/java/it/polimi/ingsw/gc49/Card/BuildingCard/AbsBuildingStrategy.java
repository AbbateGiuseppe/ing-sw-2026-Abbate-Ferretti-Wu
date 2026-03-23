package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.PlayerDataInterface;

public abstract class AbsBuildingStrategy implements BuildingStrategyInterface {

    BuildingEvent event;
    EventManager manager;

    public AbsBuildingStrategy(BuildingEvent event, EventManager manager) {
        this.event = event;
        this.manager = manager;
    }

    protected abstract void effect(PlayerDataInterface player);

    @Override
    public void activateEffect(PlayerDataInterface playerDataInterface){
        if(condition(playerDataInterface)){
            effect(playerDataInterface);
        }
    }

    protected abstract boolean condition(PlayerDataInterface playerDataInterface);
}
