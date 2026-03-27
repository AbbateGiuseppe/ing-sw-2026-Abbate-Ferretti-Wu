package it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.DataBank;
import it.polimi.ingsw.gc49.Invention;
import it.polimi.ingsw.gc49.Player;

public class Inventor extends CharacterCard {
    private final Invention invention;

    public Inventor(Invention invention) {
        this.invention = invention;
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
         dataBank.addCharacterCount(CharacterType.Inventor,1);
         dataBank.addInvention(invention);
    }
}
