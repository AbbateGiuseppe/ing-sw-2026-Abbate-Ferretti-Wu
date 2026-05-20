package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OrderboardModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.PlayersModelElement;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.Track.OrderSlot;

import java.util.ArrayList;
import java.util.List;

public class TotemChoosing extends State {
    public TotemChoosing ( Game game ) { super(game, States.TOTEM_CHOOSING); }

    public State executeState () {
        int numOfPlayers = game.getNumOfPlayers();
        while (game.getUsedTotems().size() < numOfPlayers) { //waits until every player has chosen a totem.
            try {
                Locks.playerInput.wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
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

        return new OfferChoosing(game);
    }
}
