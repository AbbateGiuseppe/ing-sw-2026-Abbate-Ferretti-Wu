package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.EventManager;

public class CharacterSetCompletePointEndGameCard extends BuildingCard {
    public CharacterSetCompletePointEndGameCard(BuildingEvent buildingEvent, EventManager manager, int PPReward, int foodPrice) {
        super(buildingEvent, manager, PPReward, foodPrice);
    }

    @Override
    public void onEventEffect() {
         owner.addFood(6 * owner.data.getCharacterCount(CharacterType.CompleteSet));
    }
}
