package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.Card.BuildingCard.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.PlayerDataInterface;

public class DoubleBuilderCardStrategy extends AbsBuildingStrategy {
    public DoubleBuilderCardStrategy(BuildingEvent event, EventManager manager) {
        super(event, manager);
    }

    /***\
     *
     * @param player doubles builderCard's point at end game
     */
    @Override
    public void effect(PlayerDataInterface player) {
        int reward = player.GetBuilderPP();
        player.addPoints(reward);
    }

    @Override
    protected boolean condition(PlayerDataInterface playerDataInterface) {
        return true;
    }
}
