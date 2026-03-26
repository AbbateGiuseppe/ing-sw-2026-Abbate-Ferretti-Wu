package it.polimi.ingsw.gc49.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.Card.TribeCards.TribeCard;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

import java.util.List;

public abstract class EventCard extends TribeCard implements Comparable<EventCard> {
    protected EventManager eventManager;

    @Override
    public boolean canGet(Player player) {
        return false;
    }
    @Override
    public int compareTo(EventCard other) {
        if (this.getEra() > other.getEra()) {
            return 1;
        } else if (this.getEra() < other.getEra()) {
            return -1;
        } else {
            return this instanceof SustenanceEvent ?  1 : 0;
        }

    }
    public abstract void resolveEvent(List<Player> players);
}
