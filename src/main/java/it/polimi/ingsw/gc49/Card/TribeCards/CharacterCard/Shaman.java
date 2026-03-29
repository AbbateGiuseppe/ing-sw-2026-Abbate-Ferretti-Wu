package it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.DataBank;
import it.polimi.ingsw.gc49.Player;

public class Shaman extends CharacterCard {
    private final int numStars;

    public Shaman(int numStars) {
        this.numStars = numStars;
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
        dataBank.addNumStar(numStars);
        dataBank.addCharacterCount(CharacterType.Shaman,1);
    }
}
