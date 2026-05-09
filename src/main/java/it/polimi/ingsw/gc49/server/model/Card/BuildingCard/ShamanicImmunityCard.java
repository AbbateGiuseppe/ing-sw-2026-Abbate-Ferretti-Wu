package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;

public class ShamanicImmunityCard extends BuildingCard {
    public ShamanicImmunityCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        if (owner.getPointsToPay() > 0) {
            owner.setPointsToPay(0);
        }
    }
    @Override
    public String toString() {
        return "ShamanicImmunityCard{" +
                "era=" + era +
                ", foodPrice=" + foodPrice +
                ", pointsEndgame=" + pointsEndgame +
                ", effect=if the player is the loser,then he doesn't get penalized during the shamanic event" +
                '}';
    }

    @Override
    public String simpleToString () {
        return "EDIFICIO (immunità sciamanica)";
    }
}
