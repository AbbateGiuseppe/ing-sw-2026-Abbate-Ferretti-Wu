package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

// Effect 3
public class ShamanicImmunityStrategy extends AbsBuildingStrategy {
    // BuildingEvent:RITUAL_POSTERIOR_EVENT
    public ShamanicImmunityStrategy(BuildingEvent event) {
        super(event);
    }

    @Override
    public void effect(Player player) {
         player.setPointsToPay(0);
    }

    // if the player is the loser,i.e. he has to pay some points
    @Override
    protected boolean condition(Player player){
        return player.getPointsToPay() > 0;
    }
}
