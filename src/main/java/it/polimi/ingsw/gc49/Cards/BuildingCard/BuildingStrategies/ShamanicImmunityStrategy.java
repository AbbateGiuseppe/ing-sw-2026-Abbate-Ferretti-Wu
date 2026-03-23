package it.polimi.ingsw.gc49.Cards.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Cards.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.Cards.BuildingCard.BuildingEvent;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;
import it.polimi.ingsw.gc49.PlayerDataInterface;

public class ShamanicImmunityStrategy extends AbsBuildingStrategy {
    public ShamanicImmunityStrategy(BuildingEvent event, EventManager manager) {
        super(event, manager);
    }

    @Override
    public void effect(PlayerDataInterface player) {
            player.setupToPay(0,0);
    }

    protected boolean condition(PlayerDataInterface player){
        return player.getPPTopay()>0;
    }
}
