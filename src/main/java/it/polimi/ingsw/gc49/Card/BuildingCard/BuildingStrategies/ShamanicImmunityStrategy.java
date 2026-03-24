package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.Card.BuildingCard.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.PlayerDataInterface;

public class ShamanicImmunityStrategy extends AbsBuildingStrategy {
    public ShamanicImmunityStrategy(BuildingEvent event, EventManager manager) {
        super(event, manager);
    }

    @Override
    public void effect(PlayerDataInterface player) {
        // player.setPointsToPay(0);
    }

    protected boolean condition(PlayerDataInterface player){
        // return player.getPointTopay() > 0;
    }
}
