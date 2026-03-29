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

    /**updates the values of the drawing player's databank during a draw.*/
    public void updateDataBank( DataBank dataBank ) {
        // will be overridden by the subclass card when it has to actually modify the player's databank.
    }

    /**
     * Should be called AFTER updating the databank.
     * It handles the special effects applied by the card after it has been drawn.
     * @param player the drawing player
     */
    public void onDraw( Player player ) {

    }
}
