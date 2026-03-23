package it.polimi.ingsw.gc49.Cards.BuildingCard;

import it.polimi.ingsw.gc49.Totem;

import java.util.EventListener;

public interface BuildingEventListener extends EventListener {

    public void onEventEffect(Totem totem);
}
