package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.*;

public class SamePairInventionsCard extends BuildingCard {
    public SamePairInventionsCard ( BuildingEvent buildingEvent, EventManager manager, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, manager, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        owner.addFood(3 * owner.data.getCharacterCount(CharacterType.SamePairInventions));
    }

    @Override
    public void setOwner(Player owner) {
        super.setOwner(owner);
        // Record the current number of the same pair inventions for future comparision
        owner.data.recordInventions();
    }

}
