package it.polimi.ingsw.gc49.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.Card.TribeCards.TribeCard;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

import java.util.List;

public abstract class EventCard extends TribeCard {
    protected EventManager eventManager;

    @Override
    public boolean canGet(Player player) {
        return false;
    }

    public abstract void resolveEvent(List<Player> players);
}
