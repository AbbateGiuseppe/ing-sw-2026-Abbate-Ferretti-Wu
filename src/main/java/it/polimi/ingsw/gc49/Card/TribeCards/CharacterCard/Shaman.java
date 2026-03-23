package it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.Player;

public class Shaman extends CharacterCard {
    private final int numStars;

    public Shaman(int numStars) {
        this.numStars = numStars;
    }

    @Override
    public void updateDataBank(Player player) {
        // databank.numStars += numStars;
        // databank.numShamans += 1;
    }
}
