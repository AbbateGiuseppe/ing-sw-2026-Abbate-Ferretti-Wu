package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

public class OneMoreCardStrategy extends AbsBuildingStrategy {
    public OneMoreCardStrategy(BuildingEvent event, EventManager manager) {
        super(event, manager);
    }

    @Override
    public void effect(Player player) {
        //TODO:to be decided and waiting for states
    }

    @Override
    protected boolean condition(Player player) {
        return true;
    }
}
