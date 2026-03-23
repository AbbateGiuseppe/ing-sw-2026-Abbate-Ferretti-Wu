package it.polimi.ingsw.gc49.Cards.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Cards.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.Cards.BuildingCard.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.PlayerDataInterface;

public class DoubleShamanPointsStrategy extends AbsBuildingStrategy {
    public DoubleShamanPointsStrategy(BuildingEvent event, EventManager manager) {
        super(event, manager);
    }

    @Override
    public void effect(PlayerDataInterface player) {
        player.addPoints(player.getReward());
    }

    @Override
    protected boolean condition(PlayerDataInterface player) {
        return player.getReward()>0;
    }
}
