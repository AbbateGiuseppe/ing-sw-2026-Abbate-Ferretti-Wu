package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.DataBank;
import it.polimi.ingsw.gc49.server.model.Era;

public class ShamanicThreeStarCard extends BuildingCard {
    public ShamanicThreeStarCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
    }

    @Override
    public void onEventEffect() {
        return;
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
        super.updateDataBank(dataBank);
        dataBank.addNumStar(3);
    }

    @Override
    public String toString() {
        return "ShamanicThreeStarCard{" +
                "era=" + era +
                ", foodPrice=" + foodPrice +
                ", pointsEndgame=" + pointsEndgame +
                ", effect=get 3 virtual stars during the shamanic event" +
                '}';
    }
}
