package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

// Effect 4
public class BonusFoodEndTurnStrategy extends AbsBuildingStrategy {
    //BuildingEvent:TURN_END
    public BonusFoodEndTurnStrategy(BuildingEvent event) {
        super(event);
    }

    @Override
    public void effect(Player player) {
        player.addFood(1);
    }

    // if the player's totem gets food on the order slot
    @Override
    protected boolean condition(Player player) {
        return player.IsGettingBonusFood();
    }

}
