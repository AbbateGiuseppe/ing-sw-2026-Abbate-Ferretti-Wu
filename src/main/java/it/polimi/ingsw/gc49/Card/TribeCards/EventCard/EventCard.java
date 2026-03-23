package it.polimi.ingsw.gc49.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.Card.TribeCards.TribeCard;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

import java.util.List;

public abstract class EventCard extends TribeCard {
    private EventManager eventManager;


    @Override
    public boolean canGet(Player player) {
        return false;
    }

    public void resolveEvent(List<Player> players) {
        for(Player player : players) {
            if(eventSuccess(player)) {
                reward(player);
            } else {
                penalty(player);
            }
        }
    }

    public abstract boolean eventSuccess(Player player);
    public abstract void reward(Player player);
    public abstract void penalty(Player player);
}
