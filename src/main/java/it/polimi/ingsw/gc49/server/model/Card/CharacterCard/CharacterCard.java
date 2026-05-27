package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;

import java.io.Serializable;

public abstract class CharacterCard extends Card implements Serializable {
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
