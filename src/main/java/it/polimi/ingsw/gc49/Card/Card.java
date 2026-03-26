package it.polimi.ingsw.gc49.Card;


import it.polimi.ingsw.gc49.DataBank;
import it.polimi.ingsw.gc49.Player;

public abstract class Card {
    // Use int instead of enum since enum can't keep track of the order
    private int era;

    public abstract boolean canGet(Player player);

    public void updateDataBank( DataBank dataBank ) {
        // will be overridden by the subclass card when it has to actually modify the player's databank.
    }
}
