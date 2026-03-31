package it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.DataBank;
import it.polimi.ingsw.gc49.Player;

public class Hunter extends CharacterCard {
    private final boolean drumstick;

    public Hunter(boolean drumstick) {
        this.drumstick = drumstick;
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
        dataBank.addCharacterCount(CharacterType.Hunter,1);
        // if the card has drumstick symbol,then give the player an amount of food equal to the number of Hunter cards that he has
        if (drumstick) {
             dataBank.assignedPlayer.addFood(dataBank.getCharacterCount(CharacterType.Hunter));
        }
    }
}
