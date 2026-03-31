package it.polimi.ingsw.gc49.Card;


import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.Player;

public abstract class Card {
    private Era era;

    public Era getEra() {return era;}
    public int getMinNumPlayers() {return minNumPlayers;}

    /**
     *
     * When the player picks a card,this method is called to check whether he can get it
     * it returns always true for charactercards and always false for eventcards,whereas for buildingcards
     * it compares the food and the building discount of the player with the foodprice of the card
     * if it returns true,the player calls updateDataBank() of the card
      */
    public abstract boolean canGet(Player player);

    public Era getEra() {
        return era;
    }

    public void setEra(Era era) {
        this.era = era;
    }
}
