package it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.Card.TribeCards.TribeCard;
import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.Player;

public abstract class CharacterCard extends TribeCard {
    public CharacterCard ( Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
    }

    @Override
    public boolean canGet(Player player) {
        return true;
    }
}
