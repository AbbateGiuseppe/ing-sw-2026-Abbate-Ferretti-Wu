package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.Card.BuildingCard.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.PlayerDataInterface;

public class DoubleShamanPointsStrategy extends AbsBuildingStrategy {
    public DoubleShamanPointsStrategy(BuildingEvent event, EventManager manager) {
        super(event, manager);
    }

    @Override
    public void effect(PlayerDataInterface player) {
        // player.setPointsToPay(2 * player.getPointsToPay());
    }

    @Override
    protected boolean condition(PlayerDataInterface player) {
        // return player.getUniqueWinner() && player.getPointsToPay() < 0;
    }
}
