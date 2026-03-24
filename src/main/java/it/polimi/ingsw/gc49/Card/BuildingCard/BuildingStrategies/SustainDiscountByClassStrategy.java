package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.Card.BuildingCard.BuildingEvent;
import it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard.CharacterType;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.PlayerDataInterface;

public class SustainDiscountByClassStrategy extends AbsBuildingStrategy {
    public SustainDiscountByClassStrategy(BuildingEvent event, EventManager manager) {
        super(event, manager);
    }

    CharacterType unit;
    @Override
    public void effect(PlayerDataInterface player) {
        // player.setFoodToPay(player.getFoodsTopay() - player.GetCharaCount(unit));
    }

    @Override
    protected boolean condition(PlayerDataInterface playerDataInterface) {
        return true;
    }
}
