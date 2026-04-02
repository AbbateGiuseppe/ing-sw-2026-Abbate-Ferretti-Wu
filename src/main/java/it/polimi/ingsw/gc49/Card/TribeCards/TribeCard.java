package it.polimi.ingsw.gc49.Card.TribeCards;

import it.polimi.ingsw.gc49.Card.Card;
import it.polimi.ingsw.gc49.Era;

public abstract class TribeCard extends Card {
    public TribeCard ( Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
    }
}
