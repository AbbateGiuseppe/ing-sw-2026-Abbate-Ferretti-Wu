package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;


public class DoubleShamanPointsStrategy extends AbsBuildingStrategy {
    public DoubleShamanPointsStrategy(BuildingEvent event) {
        super(event);
    }

    @Override
    public void effect(Player player) {
        player.setPointsToPay(2 * player.getPointsToPay());
    }

    @Override
    protected boolean condition(Player player) {
        return player.isUniqueWinner() && player.getPointsToPay() < 0;
    }
}
