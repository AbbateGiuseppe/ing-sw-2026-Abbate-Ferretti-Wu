package it.polimi.ingsw.gc49.server.model.Card.TribeCards;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Era;

public abstract class TribeCard extends Card {
    public TribeCard ( Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
    }
}
