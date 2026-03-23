package it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.Card.TribeCards.TribeCard;
import it.polimi.ingsw.gc49.Player;

public abstract class CharacterCard extends TribeCard {
    @Override
    public boolean canGet(Player player) {
        return true;
    }

    // When the card is picked, it updates the databank of the player
    public abstract void updateDataBank(Player player);
}
