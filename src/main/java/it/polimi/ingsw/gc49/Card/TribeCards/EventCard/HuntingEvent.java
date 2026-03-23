package it.polimi.ingsw.gc49.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.Player;

public class HuntingEvent extends EventCard {
    private final int pointsPerHunter;

    public HuntingEvent(int pointsPerHunter) {
        this.pointsPerHunter = pointsPerHunter;
    }

    @Override
    public boolean eventSuccess(Player player) {
        return true;
    }

    @Override
    public void reward(Player player) {
        // player.food += databank.numHunters;
        // player.points += databank.numHunters*pointsPerHunter;
    }

    @Override
    public void penalty(Player player) {
        return;
    }
}
