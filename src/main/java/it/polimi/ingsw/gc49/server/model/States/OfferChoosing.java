package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard.OrderboardModelElement;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import it.polimi.ingsw.gc49.server.model.Player;

public class OfferChoosing extends State {
    public OfferChoosing ( Game game ) { super(game, States.OFFER_CHOOSING); }

    public State executeState () {
        Player currentPlayer = game.getTrack().getNextPlayerOrderSlot();
        while ( currentPlayer != null ) { //loops until there's no player remaining to choose an offer.
            currentPlayer.setChoseAnOffer(false);
            game.setCurrentPlayer(currentPlayer);
            game.setCurrentPlayerIndex(currentPlayer.getPlayerIndex());
            game.broadcastCurrentPlayerTurn();
            while (!currentPlayer.hasChosenAnOffer() && currentPlayer.isConnected()){ //waits until the current player has chosen an offer.
                try {
                    Locks.playerInput.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            //disconnection clause
            if(!currentPlayer.isConnected()){
                game.getTrack().deassignCurrentOrderSlot(currentPlayer);
                currentPlayer.setRemovedFromTrack(true);
                game.queueUpdateModelElement(
                        new OrderboardModelElement(
                                currentPlayer.getNickname() + " è rimosso dalla plancia fino al suo ritorno",
                                game.getTrack().giveOrderBoardMockup()
                        )
                );
                game.broadcastGameUpdate();
            }
            //if the game was paused, wait before selecting the following player
            while( game.isPaused() ) {
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
