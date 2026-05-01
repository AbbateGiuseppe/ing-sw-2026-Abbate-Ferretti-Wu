package it.polimi.ingsw.gc49.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.model.BuildingEvent;
import it.polimi.ingsw.gc49.model.Era;

public class OneMoreCardCard extends BuildingCard {
    public OneMoreCardCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        owner.setDrawableUpper(owner.getDrawableUpper()  + 1);
    }

    @Override
    public String toString() {
        return "OneMoreCardCard{" +
                "era=" + era +
                ", foodPrice=" + foodPrice +
                ", pointsEndgame=" + pointsEndgame +
                ", effect=the player gets to pick another card at the end of the round" +
                '}';
    }
}
