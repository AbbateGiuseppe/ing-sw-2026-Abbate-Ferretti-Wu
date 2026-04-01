package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.*;

public class SamePairInventionsCard extends BuildingCard {
    public SamePairInventionsCard(BuildingEvent buildingEvent, EventManager manager, int PPReward, int foodPrice) {
        super(buildingEvent, manager, PPReward, foodPrice);
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
