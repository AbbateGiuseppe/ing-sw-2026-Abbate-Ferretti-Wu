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
    // Some effects are only activated on condition,e.g.effect num 3 is triggered only if the player is the loser
    protected abstract boolean condition(Player player);
}
