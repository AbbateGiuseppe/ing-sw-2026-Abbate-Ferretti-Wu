package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

public class SustainDiscountByClassStrategy extends AbsBuildingStrategy {
    public SustainDiscountByClassStrategy(BuildingEvent event, EventManager manager) {
        super(event, manager);
    }

    CharacterType unit;
    @Override
    public void effect(Player player) {
        player.setFoodToPay(player.getFoodToPay() - player.data.getCharacterCount(unit));
    }

    @Override
    protected boolean condition(Player playerDataInterface) {
        return true;
    }
}
