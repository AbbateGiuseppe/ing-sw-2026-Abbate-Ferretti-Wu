package it.polimi.ingsw.gc49.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.model.BuildingEvent;
import it.polimi.ingsw.gc49.model.Era;

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
}
