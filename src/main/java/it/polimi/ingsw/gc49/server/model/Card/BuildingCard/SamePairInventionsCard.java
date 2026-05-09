package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;

public class SamePairInventionsCard extends BuildingCard {
    public SamePairInventionsCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        owner.addFood(3 * owner.data.getCharacterCount(CharacterType.SamePairInventions));
    }
    @Override
    protected void setOwner( Player owner) {
        super.setOwner(owner);
        // Record the current number of the same pair inventions for future comparision
        owner.data.recordInventions();
    }

    @Override
    public String toString() {
        return "SamePairInventionsCard{" +
                "era=" + era +
                ", foodPrice=" + foodPrice +
                ", pointsEndgame=" + pointsEndgame +
                ", effect=get 3 bonus food whenever the player obtains a pair of same inventors" +
                '}';
    }

    @Override
    public String simpleToString () {
        return "EDIFICIO (stracibo da inventori)";
    }
}
