package it.polimi.ingsw.gc49.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.model.BuildingEvent;
import it.polimi.ingsw.gc49.model.Era;

public class TwentyFiveBonusPointsEndGame extends BuildingCard {
    public TwentyFiveBonusPointsEndGame ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        owner.addPoints(25);
    }

    @Override
    public String toString() {
        return "TwentyFiveBonusPointsEndGame{" +
                "era=" + era +
                ", foodPrice=" + foodPrice +
                ", pointsEndgame=" + pointsEndgame +
                ", effect=get 25 bonus points at the end of the game" +
                '}';
    }
}
