package it.polimi.ingsw.gc49.Cards.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Cards.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.Cards.BuildingCard.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.PlayerDataInterface;

public class BonusFoodEndTurnStrategy extends AbsBuildingStrategy {
    public BonusFoodEndTurnStrategy(BuildingEvent event, EventManager manager) {
        super(event, manager);
    }

    @Override
    public void effect(PlayerDataInterface player) {
        ;
    }

    @Override
    protected boolean condition(PlayerDataInterface player) {
        return player.IsGettingBonusFood();
    }

}
