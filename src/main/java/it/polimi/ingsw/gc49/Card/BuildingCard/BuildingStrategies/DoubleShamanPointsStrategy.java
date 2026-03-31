package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

// Effect 7
public class DoubleShamanPointsStrategy extends AbsBuildingStrategy {
    // BuildingEvent:RITUAL_POSTERIOR_EVENT
    public DoubleShamanPointsStrategy(BuildingEvent event) {
        super(event);
    }

    @Override
    public void effect(Player player) {
        player.setPointsToPay(2 * player.getPointsToPay());
    }

    // if the player is the unique winner,i.e. he has to get some points and he is the only player to get points
    @Override
    protected boolean condition(Player player) {
        return player.isUniqueWinner() && player.getPointsToPay() < 0;
    }
}
