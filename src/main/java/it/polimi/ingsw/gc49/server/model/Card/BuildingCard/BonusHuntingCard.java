package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;

public class BonusHuntingCard extends BuildingCard {
    public BonusHuntingCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        owner.setFoodToPay(owner.getFoodToPay() - owner.data.getCharacterCount(CharacterType.Hunter));
        owner.setPointsToPay(owner.getPointsToPay() - owner.data.getCharacterCount(CharacterType.Hunter));
    }

    @Override
    public String toString() {
        return "BonusHuntingCard{" +
                "era=" + era +
                ", foodPrice=" + foodPrice +
                ", pointsEndgame=" + pointsEndgame +
                ", effect=get bonus food and points equal to the number of hunters in possession during the hunting event" +
                '}';
    }
}
