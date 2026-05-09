package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;

public class DoubleBuilderPointsCard extends BuildingCard {
    public DoubleBuilderPointsCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        owner.data.setNumBuilderPoints(2 * owner.data.getNumBuilderPoints());
    }

    @Override
    public String toString() {
        return "DoubleBuilderPointsCard{" +
                "era=" + era +
                ", foodPrice=" + foodPrice +
                ", pointsEndgame=" + pointsEndgame +
                ", effect=double the builder points at the end of the game" +
                '}';
    }

    @Override
    public String simpleToString () {
        return "EDIFICIO (doppipunti da costruttori)";
    }
}
