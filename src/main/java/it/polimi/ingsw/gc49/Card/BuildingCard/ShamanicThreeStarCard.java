package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.DataBank;
import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.EventManager;

public class ShamanicThreeStarCard extends BuildingCard {
    public ShamanicThreeStarCard ( BuildingEvent buildingEvent, EventManager manager, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, manager, pointsEndgame, foodPrice, era, minNumPlayers);
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
}
