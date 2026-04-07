package it.polimi.ingsw.gc49.model.Card.TribeCards;

import it.polimi.ingsw.gc49.model.Card.Card;
import it.polimi.ingsw.gc49.model.Era;

public abstract class TribeCard extends Card {
    public TribeCard ( Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
    }
}
