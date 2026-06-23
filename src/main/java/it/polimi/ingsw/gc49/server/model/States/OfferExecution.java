package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.GameStatusModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard.ReturnModelElement;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import it.polimi.ingsw.gc49.server.model.Player;


/**
 * Represents the state where players execute the actions associated with their chosen offers.
 * <p>
 * The state iterates through all players who have chosen an offer. For each player,
 * it waits until they have exhausted their actions (or disconnected). After a player's
 * turn, it triggers end-of-turn events and updates the board. Once all players have
 * finished, it triggers end-of-round events and transitions to either the next round
 * or the end of the game.
 */
public class OfferExecution extends State{

    /**
     * Constructs the OfferExecution state.
     *
     * @param game  The main game instance.
     * @param locks The synchronization locks for thread-safe state execution.
     */
    public OfferExecution ( Game game, Locks locks ) { super(game, States.OFFER_EXECUTION, locks);}


    /**
     * Executes the action phase of the round.
     * <p>
     * Cycles through the turn order, allowing each player to perform their actions.
     * Suspends the thread while waiting for player input. Handles end-of-turn
     * building activations and determines the next phase of the game.
     *
     * @return The next state: {@link GameEnd} if this is the last round, otherwise {@link RoundEnd}.
     */
    public State executeState () {
        game.queueUpdateModelElement(new GameStatusModelElement("Siamo passati all'eseguimento delle offerte", currentStateType));
        game.broadcastGameUpdate();
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
                if(game.getGameEndedPreemptively().get()){
                    return null;
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
