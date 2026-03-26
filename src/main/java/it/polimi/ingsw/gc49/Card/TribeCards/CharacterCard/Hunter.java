package it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.Player;

public class Hunter extends CharacterCard {
    private final boolean drumstick;

    public Hunter(boolean drumstick) {
        this.drumstick = drumstick;
    }

    @Override
    public void updateDataBank(Player player) {
        player.data.addCharacterCount(CharacterType.Hunter,1);
        if (drumstick) {
             player.addFood(player.data.getCharacterCount(CharacterType.Hunter));
        }
    }
}
