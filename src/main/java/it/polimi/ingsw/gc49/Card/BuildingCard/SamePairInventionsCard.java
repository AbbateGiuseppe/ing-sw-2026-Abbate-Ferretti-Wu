package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.*;

public class SamePairInventionsCard extends BuildingCard {
    public SamePairInventionsCard(BuildingEvent buildingEvent, EventManager manager, int PPReward, int foodPrice) {
        super(buildingEvent, manager, PPReward, foodPrice);
    }

    @Override
    public void onEventEffect() {
        int unitCount = owner.data.getCharacterCount(CharacterType.SamePairInventions);
        owner.addFood(3 * unitCount);
    }

    @Override
    public void setOwner(Player owner) {
        super.setOwner(owner);
        // Record the current number of the same pair inventions for future comparision
        owner.data.recordInventions();
    }

}
