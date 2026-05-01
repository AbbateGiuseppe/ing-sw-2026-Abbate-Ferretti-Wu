package it.polimi.ingsw.gc49.model.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.model.CharacterType;
import it.polimi.ingsw.gc49.model.DataBank;
import it.polimi.ingsw.gc49.model.Era;

public class Artist extends CharacterCard {
    public Artist ( Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
        dataBank.addCharacterCount(CharacterType.Artist,1);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "era=" +  era +
                '}';
    }
}
