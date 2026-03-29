package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

// Effect num 2
public class SustainDiscountByClassStrategy extends AbsBuildingStrategy {
    // BuildingEvent:SUSTENANCE_EVENT
    public SustainDiscountByClassStrategy(BuildingEvent event) {
        super(event);
    }

    CharacterType unit;
    @Override
    public void effect(Player player) {
        player.setFoodToPay(player.getFoodToPay() - player.data.getCharacterCount(unit));
    }

    @Override
    protected boolean condition(Player player) {
        return true;
    }
}
