package it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.Invention;
import it.polimi.ingsw.gc49.Player;

public class Inventor extends CharacterCard {
    private final Invention invention;

    public Inventor(Invention invention) {
        this.invention = invention;
    }

    @Override
    public void updateDataBank(Player player) {
         player.data.addCharacterCount(CharacterType.Inventor,1);
         player.data.addInvention(invention);
    }
}
