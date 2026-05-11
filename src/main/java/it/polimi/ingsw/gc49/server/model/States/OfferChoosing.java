package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import it.polimi.ingsw.gc49.server.model.Player;

public class OfferChoosing extends State {
    public OfferChoosing ( Game game ) { super(game, States.OFFER_CHOOSING); }

    public State executeState () {
        Player currentPlayer = game.getTrack().getNextPlayerOrderSlot();
        while ( currentPlayer != null) { //loops until there's no player remaining to choose an offer.
            currentPlayer.setChoseAnOffer(false);
            game.setCurrentPlayer(currentPlayer);
            game.setCurrentPlayerIndex(currentPlayer.getPlayerIndex());
            game.broadcastCurrentPlayerTurn();
            while (!currentPlayer.hasChosenAnOffer()){ //waits until the current player has chosen an offer.
                try {
                    Locks.playerInput.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            currentPlayer = game.getTrack().getNextPlayerOrderSlot();
        }

        return new OfferExecution(game); //goes to OfferExecution
    }
}
