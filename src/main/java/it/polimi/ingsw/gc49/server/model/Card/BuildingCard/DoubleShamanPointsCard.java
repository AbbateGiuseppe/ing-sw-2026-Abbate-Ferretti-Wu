package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;

public class DoubleShamanPointsCard extends BuildingCard{
    public DoubleShamanPointsCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        if (owner.isUniqueWinner() && owner.getPointsToPay() < 0) {
            owner.setPointsToPay(2 * owner.getPointsToPay());
        }
    }

    @Override
    public String toString() {
        return "DoubleShamanPointsCard{" +
                "era=" + era +
                ", foodPrice=" + foodPrice +
                ", pointsEndgame=" + pointsEndgame +
                ", effect=if the player is the unique winner,then double the reward points during the shamanic event" +
                '}';
    }
}
