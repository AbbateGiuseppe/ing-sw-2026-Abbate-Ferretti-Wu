package it.polimi.ingsw.gc49.model.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.model.CharacterType;
import it.polimi.ingsw.gc49.model.DataBank;
import it.polimi.ingsw.gc49.model.Era;

public class Shaman extends CharacterCard {
    private final int numStars;

    public Shaman( int numStars, Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
        this.numStars = numStars;
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
        dataBank.addNumStar(numStars);
        dataBank.addCharacterCount(CharacterType.Shaman,1);
    }
}
