package it.polimi.ingsw.gc49.server.model.Card.TribeCards;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Era;

import java.io.Serializable;

public abstract class TribeCard extends Card implements Serializable {
    public TribeCard ( Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
    }
}
