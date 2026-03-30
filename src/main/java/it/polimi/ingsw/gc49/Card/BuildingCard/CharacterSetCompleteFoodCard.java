package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.*;

public class CharacterSetCompleteFoodCard extends BuildingCard {
    public CharacterSetCompleteFoodCard(BuildingEvent buildingEvent, EventManager manager, int PPReward, int foodPrice) {
        super(buildingEvent, manager, PPReward, foodPrice);
    }

    @Override
    public void onEventEffect() {
        int unitCount = owner.data.getCharacterCount(CharacterType.CompleteSet);
        unitCount -= owner.data.getCurrentNumCompleteCharacterSets();
        owner.data.addCurrentNumCompleteCharacterSets(unitCount);
        owner.addFood(5 * unitCount);
    }

    @Override
    public void setOwner(Player owner) {
        super.setOwner(owner);
        // Record the current number of complete character card sets for future comparision
        owner.data.recordCharaSet();
    }
}
