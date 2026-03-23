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
    int discountPerUnit;
    @Override
    public void effect(PlayerDataInterface player) {
        int tot = player.getFoodsTopay();
        int discount = player.GetCharaCount(unit)*discountPerUnit;
        tot-=discount;
        player.setupToPay(tot,0);
    }

    @Override
    protected boolean condition(PlayerDataInterface playerDataInterface) {
        return true;
    }
}
