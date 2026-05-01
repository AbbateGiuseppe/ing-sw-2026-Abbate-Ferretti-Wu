package it.polimi.ingsw.gc49.model.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.model.CharacterType;
import it.polimi.ingsw.gc49.model.DataBank;
import it.polimi.ingsw.gc49.model.Era;
import it.polimi.ingsw.gc49.model.Player;

public class Hunter extends CharacterCard {
    private final boolean drumstick;

    public Hunter( boolean drumstick, Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
        this.drumstick = drumstick;
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
        dataBank.addCharacterCount(CharacterType.Hunter,1);
    }

    @Override
    public void onDraw( Player player ) {
        // if the card has drumstick symbol,then give the player an amount of food equal to the number of Hunter cards that he has
        if (drumstick) {
            player.addFood(player.data.getCharacterCount(CharacterType.Hunter));
        }
    }

    @Override
    public String toString() {
        return "Hunter{" +
                "era=" + era +
                ", drumstick=" + drumstick +
                '}';
    }
}
