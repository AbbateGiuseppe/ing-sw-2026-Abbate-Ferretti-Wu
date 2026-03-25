package it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.Player;

public class Gatherer extends CharacterCard {
    @Override
    public void updateDataBank(Player player) {
         player.data.addCharacterCount(CharacterType.Gatherer,1);
         player.data.addNumSustenanceDiscount(3);
    }
}
