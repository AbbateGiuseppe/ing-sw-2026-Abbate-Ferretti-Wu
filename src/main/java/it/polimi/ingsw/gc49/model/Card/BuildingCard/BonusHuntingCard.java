package it.polimi.ingsw.gc49.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.model.BuildingEvent;
import it.polimi.ingsw.gc49.model.CharacterType;
import it.polimi.ingsw.gc49.model.Era;

public class BonusHuntingCard extends BuildingCard {
    public BonusHuntingCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        owner.setFoodToPay(owner.getFoodToPay() - owner.data.getCharacterCount(CharacterType.Hunter));
        owner.setPointsToPay(owner.getPointsToPay() - owner.data.getCharacterCount(CharacterType.Hunter));
    }
}
