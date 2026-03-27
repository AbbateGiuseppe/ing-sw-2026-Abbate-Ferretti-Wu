package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

// Effect 13
public class OneMoreCardStrategy extends AbsBuildingStrategy {
    // BuildingEvent:ROUND_END
    public OneMoreCardStrategy(BuildingEvent event) {
        super(event);
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
