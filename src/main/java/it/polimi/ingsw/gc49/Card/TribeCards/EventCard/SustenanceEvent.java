package it.polimi.ingsw.gc49.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.Player;

public class SustenanceEvent extends EventCard {
    private final int minusPoints;

    public SustenanceEvent(int minusPoints) {
        this.minusPoints = minusPoints;
    }

    @Override
    public boolean eventSuccess(Player player) {

        return false;
    }

    @Override
    public void reward(Player player) {

    }

    @Override
    public void penalty(Player player) {

    }
}
