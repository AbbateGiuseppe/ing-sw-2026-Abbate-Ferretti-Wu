package it.polimi.ingsw.gc49.States;

import it.polimi.ingsw.gc49.Model;
import it.polimi.ingsw.gc49.Player;

public class OfferChoosing extends State {

    public OfferChoosing ( Model model ) { super(model); }

    public State executeState () {
        Player currentPlayer = model.getTrack().getNextPlayerOrderSlot();
        while ( currentPlayer != null) { //loops until there's no player remaining to choose an offer.
            model.setCurrentPlayer(currentPlayer);
            model.setCurrentPlayerIndex(currentPlayer.getPlayerIndex());
            while (!currentPlayer.hasChosenAnOffer()){ //waits until the current player has chosen an offer.
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            currentPlayer = model.getTrack().getNextPlayerOrderSlot();
        }

        return new OfferExecution(model); //goes to OfferExecution
    }
}
