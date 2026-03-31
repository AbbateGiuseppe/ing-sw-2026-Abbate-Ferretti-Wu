package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

// Effect 9
public class DoubleBuilderCardStrategy extends AbsBuildingStrategy {
    // BuildingEvent:GAMA_END
    public DoubleBuilderCardStrategy(BuildingEvent event) {
        super(event);
    }

    @Override
    public void effect(Player player) {
        player.data.doubleNumBuilderPoints();;
    }

    @Override
    protected boolean condition(Player player) {
        return true;
    }
}
