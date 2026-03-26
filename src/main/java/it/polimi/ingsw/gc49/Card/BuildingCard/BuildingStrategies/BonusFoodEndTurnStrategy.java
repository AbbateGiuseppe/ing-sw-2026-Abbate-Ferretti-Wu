package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

public class BonusFoodEndTurnStrategy extends AbsBuildingStrategy {
    public BonusFoodEndTurnStrategy(BuildingEvent event) {
        super(event);
    }

    @Override
    public void effect(Player player) {
        player.addFood(1);
    }

    @Override
    protected boolean condition(Player player) {
        return player.IsGettingBonusFood();
    }

}
