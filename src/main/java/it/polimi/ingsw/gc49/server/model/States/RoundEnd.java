package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Cardboard.FullCardboardModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.GameStatusModelElement;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;


/**
 * Represents the FSM state executed at the end of a standard round.
 * <p>
 * This state attempts to end the current round by refreshing the card board.
 * If the round ends normally, it broadcasts the updated board and transitions
 * back to the {@link OfferChoosing} state for the next round. If ending the
 * round triggers an Era change, it catches the resulting exception and
 * transitions to the {@link EraEnd} state instead.
 */
public class RoundEnd extends State {

    /**
     * Constructs the RoundEnd state.
     *
     * @param game  The main game instance.
     * @param locks The synchronization locks for thread-safe state execution.
     */
    public RoundEnd ( Game game, Locks locks ) { super(game, States.OTHER, locks); }


    /**
     * Executes the logic for ending a round.
     * <p>
     * Refreshes the board's cards and checks for an Era transition.
     *
     * @return The next state: {@link EraEnd} if an era transition occurred,
     * otherwise {@link OfferChoosing}.
     */
    public State executeState () {
        try {
            game.getCardBoard().endRound();
            game.queueUpdateModelElement(
                    new FullCardboardModelElement(
                            "-Il round si è concluso, le carte in gioco sono state rinnovate-",
                            game.getCardBoard().getDiscards(),
                            game.getCardBoard().getLine().getCurrentEra(),
                            game.getCardBoard().getLine().getUpperLine(),
                            game.getCardBoard().getLine().getLowerLine(),
                            game.getCardBoard().getLine().getUpperBuilding(),
                            game.getCardBoard().getLine().getLowerBuilding()
                    )
            );
            game.queueUpdateModelElement(new GameStatusModelElement("Siamo passati alla fine del round", currentStateType));
            game.broadcastGameUpdate();
        } catch (EraEndedException e) {
            return new EraEnd(game, locks); //goes to EraEnd.
        }

        return new OfferChoosing(game, locks); //goes to OfferChoosing.
    }

    /**
     * Returns the human-readable name of this state.
     *
     * @return A string representing the state name ("Fine del round").
     */
    @Override
    public String toString () {
        return "Fine del round";
    }
}
