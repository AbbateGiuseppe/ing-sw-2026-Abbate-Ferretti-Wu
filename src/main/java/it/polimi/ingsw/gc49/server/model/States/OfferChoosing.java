package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.GameStatusModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard.OrderboardModelElement;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import it.polimi.ingsw.gc49.server.model.Player;


/**
 * Represents the state where players take turns choosing an offer.
 * <p>
 * The state iterates through the players in the order determined by the track.
 * It waits for the current player to make a selection. It also handles
 * player disconnections during this phase and pauses execution if the game is paused.
 * Once all eligible players have chosen, the game transitions to {@link OfferExecution}.
 */
public class OfferChoosing extends State {

    /**
     * Constructs the OfferChoosing state.
     *
     * @param game  The main game instance.
     * @param locks The synchronization locks for thread-safe state execution.
     */
    public OfferChoosing ( Game game, Locks locks ) { super(game, States.OFFER_CHOOSING, locks); }


    /**
     * Executes the logic for the offer choosing phase.
     * <p>
     * Iterates through the turn order, broadcasting the current player's turn,
     * and suspends the thread until the player makes a choice or disconnects.
     *
     * @return The next state in the machine: {@link OfferExecution}.
     */
    public State executeState () {
        game.queueUpdateModelElement(new GameStatusModelElement("Siamo passati alla scelta delle offerte", currentStateType));
        game.broadcastGameUpdate();
        Player currentPlayer = game.getTrack().getNextPlayerOrderSlot();
        while ( currentPlayer != null ) { //loops until there's no player remaining to choose an offer.
            currentPlayer.setChoseAnOffer(false);
            game.setCurrentPlayer(currentPlayer);
            game.setCurrentPlayerIndex(currentPlayer.getPlayerIndex());
            game.broadcastCurrentPlayerTurn();
            while (!currentPlayer.hasChosenAnOffer() && currentPlayer.isConnected()){ //waits until the current player has chosen an offer.
                try {
                    locks.playerInput.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                if(game.getGameEndedPreemptively().get()){
                    return null;
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
                    locks.playerInput.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            currentPlayer = game.getTrack().getNextPlayerOrderSlot();
        }

        return new OfferExecution(game, locks); //goes to OfferExecution
    }

    /**
     * Returns the human-readable name of this state.
     *
     * @return A string representing the state name ("Scelta delle offerte").
     */

    @Override
    public String toString () {
        return "Scelta delle offerte";
    }
}
