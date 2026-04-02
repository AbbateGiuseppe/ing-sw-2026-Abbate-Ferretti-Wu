package it.polimi.ingsw.gc49.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.model.BuildingEvent;
import it.polimi.ingsw.gc49.model.CharacterType;
import it.polimi.ingsw.gc49.model.Era;

public class CharacterSetCompletePointEndGameCard extends BuildingCard {
    public CharacterSetCompletePointEndGameCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
         owner.addFood(6 * owner.data.getCharacterCount(CharacterType.CompleteSet));
    }
}
