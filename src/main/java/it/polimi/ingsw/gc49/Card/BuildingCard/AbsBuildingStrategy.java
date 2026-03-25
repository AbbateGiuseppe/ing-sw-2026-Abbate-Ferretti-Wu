package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

public abstract class AbsBuildingStrategy implements BuildingStrategyInterface {

    BuildingEvent event;
    EventManager manager;

    public AbsBuildingStrategy(BuildingEvent event, EventManager manager) {
        this.event = event;
        this.manager = manager;
    }

    @Override
    public void activateEffect(Player player){
        if(condition(player)){
            effect(player);
        }
    }

    protected abstract void effect(Player player);

    protected abstract boolean condition(Player player);
}
