package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.GameStatusModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard.OrderboardModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.PlayersModelElement;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.Track.OrderSlot;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the state where players choose their starting totems.
 * <p>
 * This state pauses the game progression until all players have successfully
 * selected a totem. Once complete, it randomizes the initial turn order,
 * distributes the starting resources (like food), broadcasts the updated
 * board state to all clients, and transitions to the {@link OfferChoosing} state.
 */
public class TotemChoosing extends State {

    /**
     * Constructs the TotemChoosing state.
     *
     * @param game  The main game instance.
     * @param locks The synchronization locks for thread-safe state execution.
     */
    public TotemChoosing ( Game game, Locks locks ) { super(game, States.TOTEM_CHOOSING, locks); }


    /**
     * Executes the totem selection phase.
     * <p>
     * Suspends the thread until the number of used totems matches the number of players.
     * Then, it assigns the randomized turn order, prepares the UI update models,
     * and advances the game state.
     *
     * @return The next state in the machine: {@link OfferChoosing}.
     */
    public State executeState () {
        game.queueUpdateModelElement(new GameStatusModelElement("Siamo passati alla scelta dei totem", currentStateType));
        game.broadcastGameUpdate();

        int numOfPlayers = game.getNumOfPlayers();
        while (game.getUsedTotems().size() < numOfPlayers) { //waits until every player has chosen a totem.
            try {
                locks.playerInput.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
            if(game.getGameEndedPreemptively().get()){
                return null;
            }
        }

        game.getTrack().randomizeStartingOrder(game.getPlayers()); //randomizes the starting order.

        StringBuilder actionInfo = new StringBuilder();
        actionInfo.append("L'ordine di turno dei giocatori è stato mescolato: ");
        List<OrderSlot> orderSlotList = game.getTrack().getOrderBoard();
        for (int i = 0; i < orderSlotList.size(); i++) {
            Player player = orderSlotList.get(i).getAssignedPlayer();
            actionInfo.append(i).append(". ").
                    append(player.getNickname()).append("(").append(player.getPlayerIndex()).append("); ");
        }
        game.queueUpdateModelElement(new OrderboardModelElement(
                actionInfo.toString(),
                game.getTrack().giveOrderBoardMockup()
        ));
        List<MockupPlayer> players = new ArrayList<>();
        for (Player player : game.getPlayers()) {
            players.add(player.giveMockupPlayer());
        }
        game.queueUpdateModelElement(new PlayersModelElement(
                "E' stato distribuito il cibo ai giocatori",
                players
        ));
        game.broadcastGameUpdate();

        return new OfferChoosing(game, locks);
    }


    /**
     * Returns the human-readable name of this state.
     *
     * @return A string representing the state name ("Scelta del totem").
     */
    @Override
    public String toString () {
        return "Scelta del totem";
    }
}
