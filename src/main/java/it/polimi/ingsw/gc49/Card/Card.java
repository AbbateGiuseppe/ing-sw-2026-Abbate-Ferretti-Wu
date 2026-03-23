package it.polimi.ingsw.gc49.Card;


import it.polimi.ingsw.gc49.Player;

public abstract class Card {
    // Use int instead of enum since enum can't keep track of the order
    private int era;

    public abstract boolean canGet(Player player);
}
