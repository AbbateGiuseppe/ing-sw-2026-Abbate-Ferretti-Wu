package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard.ReturnModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsOneModelElement;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import it.polimi.ingsw.gc49.server.model.Player;

public class OfferExecution extends State{
    public OfferExecution ( Game game ) { super(game, States.OFFER_EXECUTION);}

    public State executeState () {
        game.setPhaseStatus("Execute Offers");
        Player currentPlayer = game.getTrack().getNextPlayerOfferAndActivate();
        while (currentPlayer != null) { //loops until there's no player remaining to choose his actions.
            if (game.waitIfGameSuspendedByDisconnections()) {
                return new GameEnd(game);
            }
            game.setCurrentPlayer(currentPlayer);
            game.setCurrentPlayerIndex(currentPlayer.getPlayerIndex());
            game.broadcastCurrentPlayerTurn();
            if (!currentPlayer.isConnected()) {
                game.skipDisconnectedOfferExecution(currentPlayer);
            }
            while (currentPlayer.hasActionsLeft()){ //waits until the current player has no actions left.
                if (game.waitIfGameSuspendedByDisconnections()) {
                    return new GameEnd(game);
                }
                if (!currentPlayer.isConnected()) {
                    game.skipDisconnectedOfferExecution(currentPlayer);
                    break;
                }
                if (game.autoPassCurrentPlayerIfNoAvailableCards()) {
                    break;
                }
                try {
                    Locks.playerInput.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            game.getTrack().deassignCurrentOffer(); //deassigning the player from the offer and placing him in the order slots.
            game.queueUpdateModelElement(
                    new FoodAndPointsOneModelElement(
                            currentPlayer.getNickname() + " ha risolto la casella d'ordine",
                            currentPlayer.getPlayerIndex(),
                            currentPlayer.getFood(),
                            currentPlayer.getPoints()
                    )
            );
            game.callTurnEndEvent(); //calls all the buildings that activate at a turn's end.

            game.queueUpdateModelElement(
                    new ReturnModelElement(
                            currentPlayer.getNickname() + " e' ritornato nelle caselle d'ordine",
                            game.getTrack().giveOfferBoardMockup(),
                            game.getTrack().giveOrderBoardMockup(),
                            currentPlayer.getPlayerIndex(),
                            currentPlayer.getFood(),
                            currentPlayer.getPoints()
                    )
            );
            game.broadcastGameUpdate();

            currentPlayer = game.getTrack().getNextPlayerOfferAndActivate();
        }
        game.queueStatusUpdate("");
        game.callRoundEndEvent(); //calls all the buildings that activate at the end of a round.
        game.broadcastGameUpdate();

        if(game.isLastRound()){
            return new GameEnd(game); //goes to GameEnd.
        } else {
            return new RoundEnd(game); //goes to RoundEnd.
        }
    }

    @Override
    public String toString () {
        return "Eseguimento delle offerte";
    }
}

