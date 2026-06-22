package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Cardboard.FullCardboardModelElement;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;

/**
 * Represents the FSM state executed at the end of an Era.
 * This state transitions the board to the next Era, updates the view model,
 * and checks if the game has reached its final round. It then transitions
 * the game to the {@link OfferChoosing} state.
 */

public class EraEnd extends State {

    /**
     * Constructs the EraEnd state.
     *
     * @param game  The main game instance.
     * @param locks The synchronization locks for thread-safe state execution.
     */
    public EraEnd ( Game game, Locks locks ) { super(game, States.OTHER, locks); }


    /**
     * Executes the logic for ending an Era.
     * Attempts to trigger the end of the current Era on the game board.
     * If the deck empties (signaling the final Era), it flags the game to trigger the last round.
     * Finally, it broadcasts the updated board state to the clients.
     *
     * @return The next state in the machine: {@link OfferChoosing}.
     */
    public State executeState () {
        try {
            game.getCardBoard().endEra(game.getCardBoard().getNextEra());
            game.queueUpdateModelElement(
                    new FullCardboardModelElement(
                            "-Round ended and an Era has ended, cards have been renewed-",
                            game.getCardBoard().getDiscards(),
                            game.getCardBoard().getCurrentEra(),
                            game.getCardBoard().getLine().getUpperLine(),
                            game.getCardBoard().getLine().getLowerLine(),
                            game.getCardBoard().getLine().getUpperBuilding(),
                            game.getCardBoard().getLine().getLowerBuilding()
                    )
            );
        } catch (DeckEmptiedException e) {
            game.setLastRound(true); //sets the cycle as last round.
            game.queueUpdateModelElement(
                    new FullCardboardModelElement(
                            "-Round ended and it was the last, cards renewed-",
                            game.getCardBoard().getDiscards(),
                            game.getCardBoard().getCurrentEra(),
                            game.getCardBoard().getLine().getUpperLine(),
                            game.getCardBoard().getLine().getLowerLine(),
                            game.getCardBoard().getLine().getUpperBuilding(),
                            game.getCardBoard().getLine().getLowerBuilding()
                    )
            );
        }

        return new OfferChoosing(game, locks); //goes to OfferChoosing.
    }

    @Override
    public String toString () {
        return "Fine d'era";
    }
}
