package it.polimi.ingsw.gc49.server.model.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.DataBank;
import it.polimi.ingsw.gc49.server.model.Era;

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

    @Override
    public String toString() {
        return "Shaman{" +
                "era=" + era +
                ", numStars=" + numStars +
                '}';
    }

    @Override
    public String simpleToString () {
        return "SCIAMANO";
    }
}
