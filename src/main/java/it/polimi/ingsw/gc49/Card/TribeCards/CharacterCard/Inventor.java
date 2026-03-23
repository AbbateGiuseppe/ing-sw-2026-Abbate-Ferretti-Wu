package it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.Player;

public class Inventor extends CharacterCard {
    private final Invention invention;

    public Inventor(Invention invention) {
        this.invention = invention;
    }

    @Override
    public void updateDataBank(Player player) {
        // databank.numInventors += 1;
        // databank.inventions.add(invention)
    }
}
