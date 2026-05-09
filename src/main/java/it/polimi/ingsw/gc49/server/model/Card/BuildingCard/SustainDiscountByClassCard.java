package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;

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

    @Override
    public String toString() {
        return "SustainDiscountByClassCard{" +
                "era=" + era +
                ", foodPrice=" + foodPrice +
                ", pointsEndgame=" + pointsEndgame +
                ", effect=get 1 food discount for each " + unit + " in possession during the sustenance event" +
                '}';
    }

    @Override
    public String simpleToString () {
        return "EDIFICIO (sconto sostentamento)";
    }
}
