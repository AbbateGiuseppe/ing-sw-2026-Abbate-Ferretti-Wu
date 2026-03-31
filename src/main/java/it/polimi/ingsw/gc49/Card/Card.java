package it.polimi.ingsw.gc49.Card;


import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.Player;

public abstract class Card {
    private Era era;

    public abstract boolean canGet(Player player);

    public Era getEra() {
        return era;
    }

    public void setEra(Era era) {
        this.era = era;
    }
}
