package it.polimi.ingsw.gc49.Cards;


import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.Player;

public abstract class Card {
    //pls Don't use enum here, it's not ordered
    private Era era;

    public abstract boolean canGet(Player player);
}
