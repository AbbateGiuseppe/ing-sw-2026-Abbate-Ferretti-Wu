package it.polimi.ingsw.gc49.server.model.Card.EventCard;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.EventManager;
import it.polimi.ingsw.gc49.server.model.Player;

import java.io.Serializable;
import java.util.List;

public abstract class EventCard extends Card implements Comparable<EventCard>, Serializable {
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

    @Override
    public boolean isLowerLineOnSetup () {
        return false;
    }
}
