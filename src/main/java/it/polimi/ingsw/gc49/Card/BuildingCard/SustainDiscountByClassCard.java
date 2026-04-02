package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.EventManager;

public class SustainDiscountByClassCard extends BuildingCard {
    private final CharacterType unit;

    public SustainDiscountByClassCard ( CharacterType unit, BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
        this.unit = unit;
    }

    @Override
    public void onEventEffect() {
        owner.setFoodToPay(owner.getFoodToPay() - owner.data.getCharacterCount(unit));
    }
}
