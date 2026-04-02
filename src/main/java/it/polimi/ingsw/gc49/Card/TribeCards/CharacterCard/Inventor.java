package it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.*;

public class Inventor extends CharacterCard {
    private final Invention invention;

    public Inventor( Invention invention, Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
        this.invention = invention;
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
         dataBank.addCharacterCount(CharacterType.Inventor,1);
         dataBank.addInvention(invention);
    }
}
