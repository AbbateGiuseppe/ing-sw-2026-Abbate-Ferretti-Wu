package it.polimi.ingsw.gc49.Card;


import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.DataBank;
import it.polimi.ingsw.gc49.Player;

public abstract class Card {
    private Era era;
    private int minNumPlayers;

    public Era getEra() {return era;}
    public int getMinNumPlayers() {return minNumPlayers;}
    public abstract boolean canGet(Player player);

    public void updateDataBank( DataBank dataBank ) {
        // will be overridden by the subclass card when it has to actually modify the player's databank.
    }
}
