package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.Totem;

import java.util.EventListener;

public interface BuildingEventListener extends EventListener {
    /**
     *
     * @param totem is used for authentication
     */
    public void onEventEffect(Totem totem);
}
