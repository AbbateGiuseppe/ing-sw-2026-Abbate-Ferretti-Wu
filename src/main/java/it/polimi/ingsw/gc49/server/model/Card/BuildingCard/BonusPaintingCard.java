package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;

public class BonusPaintingCard extends BuildingCard {
    public BonusPaintingCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        owner.setFoodToPay(owner.getFoodToPay() - owner.data.getCharacterCount(CharacterType.Artist));
    }

    @Override
    public String toString() {
        return "BonusPaintingCard{" +
                "era=" + era +
                ", foodPrice=" + foodPrice +
                ", pointsEndgame=" + pointsEndgame +
                ", effect=get bonus food equal to the number of artists in possession during the painting event" +
                '}';
    }

    @Override
    public String simpleToString () {
        return "EDIFICIO (strapitture)";
    }
}
