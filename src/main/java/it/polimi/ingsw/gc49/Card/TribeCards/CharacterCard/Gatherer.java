package it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.DataBank;
import it.polimi.ingsw.gc49.Player;

public class Gatherer extends CharacterCard {
    @Override
    public void updateDataBank(DataBank dataBank) {
         dataBank.addCharacterCount(CharacterType.Gatherer,1);
         dataBank.addNumSustenanceDiscount(3);
    }
}
