package it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.Player;

public class Hunter extends CharacterCard {
    private final boolean drumstick;

    public Hunter(boolean drumstick) {
        this.drumstick = drumstick;
    }

    @Override
    public void updateDataBank(Player player) {
        // databank.numHunters += 1;
        if (drumstick) {
            // player.food += databank.numHunters;
        }
    }
}
