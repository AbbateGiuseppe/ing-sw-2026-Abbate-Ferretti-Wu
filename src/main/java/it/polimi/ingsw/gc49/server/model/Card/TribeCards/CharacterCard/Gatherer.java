package it.polimi.ingsw.gc49.server.model.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.DataBank;
import it.polimi.ingsw.gc49.server.model.Era;

public class Gatherer extends CharacterCard {
    public Gatherer ( Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
         dataBank.addCharacterCount(CharacterType.Gatherer,1);
         dataBank.addNumSustenanceDiscount(3);
    }

    @Override
    public String toString() {
        return "Gatherer{" +
                "era=" + era +
                ", sustenanceDiscount=3" +
                '}';
    }
}
