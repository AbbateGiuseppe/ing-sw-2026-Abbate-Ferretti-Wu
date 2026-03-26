package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

public class ShamanicImmunityStrategy extends AbsBuildingStrategy {
    public ShamanicImmunityStrategy(BuildingEvent event) {
        super(event);
    }

    @Override
    public void effect(Player player) {
         player.setPointsToPay(0);
    }

    protected boolean condition(Player player){
        return player.getPointsToPay() > 0;
    }
}
