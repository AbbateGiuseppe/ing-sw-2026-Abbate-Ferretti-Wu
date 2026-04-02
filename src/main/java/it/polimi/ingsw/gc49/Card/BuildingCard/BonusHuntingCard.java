package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.EventManager;

public class BonusHuntingCard extends BuildingCard {
    public BonusHuntingCard ( BuildingEvent buildingEvent, EventManager manager, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, manager, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        owner.setFoodToPay(owner.getFoodToPay() - owner.data.getCharacterCount(CharacterType.Hunter));
        owner.setPointsToPay(owner.getPointsToPay() - owner.data.getCharacterCount(CharacterType.Hunter));
    }
}
