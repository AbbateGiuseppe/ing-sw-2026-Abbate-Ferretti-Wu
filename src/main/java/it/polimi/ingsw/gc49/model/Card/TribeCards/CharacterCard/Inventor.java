package it.polimi.ingsw.gc49.model.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.model.CharacterType;
import it.polimi.ingsw.gc49.model.DataBank;
import it.polimi.ingsw.gc49.model.Era;
import it.polimi.ingsw.gc49.model.Invention;

public class Inventor extends CharacterCard {
    private final Invention invention;

    public Inventor( Invention invention, Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
        this.invention = invention;
    }

    @Override
    public void updateDataBank( DataBank dataBank) {
         dataBank.addCharacterCount(CharacterType.Inventor,1);
         dataBank.addInvention(invention);
    }

    @Override
    public String toString() {
        return "Inventor{" +
                "era=" + era +
                ", invention=" + invention +
                '}';
    }
}
