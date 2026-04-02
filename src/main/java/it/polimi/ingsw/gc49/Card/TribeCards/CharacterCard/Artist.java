package it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.DataBank;
import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.Player;

public class Artist extends CharacterCard {
    public Artist ( Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
        dataBank.addCharacterCount(CharacterType.Artist,1);
    }
}
