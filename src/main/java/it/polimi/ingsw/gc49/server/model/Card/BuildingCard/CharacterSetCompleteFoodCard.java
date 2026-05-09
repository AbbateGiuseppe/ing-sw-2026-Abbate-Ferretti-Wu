package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;

public class CharacterSetCompleteFoodCard extends BuildingCard {
    public CharacterSetCompleteFoodCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        if (owner.data.getCharacterCount(CharacterType.CompleteSet) > owner.data.getCurrentNumCompleteCharacterSets()) {
            owner.addFood(5);
            owner.data.incrementCurrentNumCompleteCharacterSets();
        }
    }

    @Override
    public void setOwner( Player owner) {
        super.setOwner(owner);
        // Record the current number of complete character card sets for future comparision
        owner.data.recordCharaSet();
    }

    @Override
    public String toString() {
        return "CharacterSetCompleteFoodCard{" +
                "era=" + era +
                ", foodPrice=" + foodPrice +
                ", pointsEndgame=" + pointsEndgame +
                ", effect=get 5 bonus food whenever completed a full set of character cards" +
                '}';
    }

    @Override
    public String simpleToString () {
        return "EDIFICIO (stracibo da set)";
    }
}
