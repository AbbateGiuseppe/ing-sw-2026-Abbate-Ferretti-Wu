package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;

public class CharacterSetCompletePointEndGameCard extends BuildingCard {
    public CharacterSetCompletePointEndGameCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
         owner.addPoints(6 * owner.data.getCharacterCount(CharacterType.CompleteSet));
    }

    @Override
    public String toString() {
        return "CharacterSetCompletePointEndGameCard{" +
                "era=" + era +
                ", foodPrice=" + foodPrice +
                ", pointsEndgame=" + pointsEndgame +
                ", effect=get 6 bonus points for each completed set of character cards at the end of the game" +
                '}';
    }

    @Override
    public String simpleToString () {
        return "EDIFICIO (strapunti da set)";
    }
}
