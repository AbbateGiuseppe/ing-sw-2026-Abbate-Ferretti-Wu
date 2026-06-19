package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard.ReturnModelElement;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import it.polimi.ingsw.gc49.server.model.Player;

public class OfferExecution extends State{
    public OfferExecution ( Game game, Locks locks ) { super(game, States.OFFER_EXECUTION, locks);}

    public State executeState () {
        Player currentPlayer = game.getTrack().getNextPlayerOfferAndActivate();
        while (currentPlayer != null) { //loops until there's no player remaining to choose his actions.
            game.setCurrentPlayer(currentPlayer);
            game.setCurrentPlayerIndex(currentPlayer.getPlayerIndex());
            game.broadcastCurrentPlayerTurn();
            while (currentPlayer.hasActionsLeft() && currentPlayer.isConnected()){ //waits until the current player has no actions left.
                try {
                    locks.playerInput.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            game.getTrack().deassignCurrentOffer(); //deassigning the player from the offer and placing him in the order slots.
            game.callTurnEndEvent(); //calls all the buildings that activate at a turn's end.

            game.queueUpdateModelElement(
                    new ReturnModelElement(
                            currentPlayer.getNickname() + " è ritornato nelle caselle d'ordine",
                            game.getTrack().giveOfferBoardMockup(),
                            game.getTrack().giveOrderBoardMockup(),
                            currentPlayer.getPlayerIndex(),
                            currentPlayer.getFood(),
                            currentPlayer.getPoints()
                    )
            );

            currentPlayer = game.getTrack().getNextPlayerOfferAndActivate();
        }
        game.callRoundEndEvent(); //calls all the buildings that activate at the end of a round.

        if(game.isLastRound()){
            return new GameEnd(game, locks); //goes to GameEnd.
        } else {
            return new RoundEnd(game, locks); //goes to RoundEnd.
        }
    }

    @Override
    public String toString () {
        return "Eseguimento delle offerte";
    }
}
