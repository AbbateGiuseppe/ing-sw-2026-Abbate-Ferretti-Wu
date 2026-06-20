package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import it.polimi.ingsw.gc49.server.model.Player;

public class OfferChoosing extends State {
    public OfferChoosing ( Game game ) { super(game, States.OFFER_CHOOSING); }

    public State executeState () {
        game.setPhaseStatus("Choose Offer");
        game.restoreConnectedRemovedPlayersToTrack();
        Player currentPlayer = game.getTrack().getNextPlayerOrderSlot();
        while ( currentPlayer != null) { //loops until there's no player remaining to choose an offer.
            if (game.waitIfGameSuspendedByDisconnections()) {
                return new GameEnd(game);
            }
            currentPlayer.setChoseAnOffer(false);
            game.setCurrentPlayer(currentPlayer);
            game.setCurrentPlayerIndex(currentPlayer.getPlayerIndex());
            if (!currentPlayer.isConnected()) {
                game.skipDisconnectedOfferChoice(currentPlayer);
                currentPlayer = game.getTrack().getNextPlayerOrderSlot();
                continue;
            }
            game.broadcastCurrentPlayerTurn();
            while (!currentPlayer.hasChosenAnOffer()){ //waits until the current player has chosen an offer.
                if (game.waitIfGameSuspendedByDisconnections()) {
                    return new GameEnd(game);
                }
                if (!currentPlayer.isConnected()) {
                    game.skipDisconnectedOfferChoice(currentPlayer);
                    break;
                }
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

    @Override
    public String toString () {
        return "Scelta delle offerte";
    }
}
