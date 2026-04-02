package it.polimi.ingsw.gc49.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.Card.TribeCards.TribeCard;
import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

import java.util.List;

public abstract class EventCard extends TribeCard implements Comparable<EventCard> {
    protected final EventManager eventManager;

    public EventCard ( EventManager eventManager, Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
        this.eventManager = eventManager;
    }

    @Override
    public boolean canGet(Player player) {
        return false;
    }
    @Override
    public int compareTo(EventCard other) {
        if (this.getEra().compareTo(other.getEra()) != 0) {
            return this.getEra().compareTo(other.getEra());
        } else {
            return this instanceof SustenanceEvent ?  1 : -1;
        }

    }
    public abstract void resolveEvent(List<Player> players);
}
