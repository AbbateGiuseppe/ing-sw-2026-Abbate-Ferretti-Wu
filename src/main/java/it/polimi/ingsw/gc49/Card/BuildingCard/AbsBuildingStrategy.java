package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

public abstract class AbsBuildingStrategy implements BuildingStrategyInterface {

    protected BuildingEvent event;

    public AbsBuildingStrategy(BuildingEvent event) {
        this.event = event;
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
