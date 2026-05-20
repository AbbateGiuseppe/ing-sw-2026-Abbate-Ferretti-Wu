package it.polimi.ingsw.gc49.server.model.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.server.model.Card.TribeCards.TribeCard;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;

import java.io.Serializable;

public abstract class CharacterCard extends TribeCard implements Serializable {
    public CharacterCard ( Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
    }

    @Override
    public boolean canGet(Player player) {
        return true;
    }

    @Override
    public boolean isLowerLineOnSetup () {
        return true;
    }
}
