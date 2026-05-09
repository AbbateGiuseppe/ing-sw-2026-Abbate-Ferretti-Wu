package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;

public class BonusFoodEndTurnCard extends BuildingCard {
    public BonusFoodEndTurnCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override //TODO: how can it know it's the player's end turn?!
    public void onEventEffect() {
        if (owner.getAssignedOrderSlot().getFoodGain() > 0) {
            owner.addFood(1);
        }
    }

    @Override
    public String toString() {
        return "BonusFoodEndTurnCard{" +
                "era=" + era +
                ", foodPrice=" + foodPrice +
                ", pointsEndgame=" + pointsEndgame +
                ", effect=get one bonus food if the totem is placed on an orderslot with food at the end of the turn" +
                '}';
    }

    @Override
    public String simpleToString () {
        return "EDIFICIO (cibo da piazzamento)";
    }
}
