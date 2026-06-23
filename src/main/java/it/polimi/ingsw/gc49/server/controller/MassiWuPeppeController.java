package it.polimi.ingsw.gc49.server.controller;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.playerExceptions.PlayerException;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualGameClient;

import java.io.Serializable;

import static it.polimi.ingsw.gc49.server.ServerMultiplexer.saveStateAsync;


/**
 * One controller per player. Bridges the player's network proxy and the shared {@link Game}:
 * incoming commands from the client are dispatched to the model via
 * {@link #executeCommand(CommandPacket)}, and model updates flow back through the
 * {@link VirtualGameClient} callbacks.
 * <p>
 * If sending a packet to the client fails, the player is marked as disconnected.
 */
public class MassiWuPeppeController implements VirtualGameClient, Serializable{
    private Game game;
    private final int controllingPlayerIndex;
    private PhasedProxyPlayer controllingPlayer;


    /**
     * Builds a controller bound to {@code controllingPlayerIndex} and to the proxy that
     * talks to its client. Not usable until {@link #connectModel(Game)} is called.
     */
    public MassiWuPeppeController ( int controllingPlayerIndex, PhasedProxyPlayer controllingPlayer ) {
        game = null;
        this.controllingPlayerIndex = controllingPlayerIndex;
        this.controllingPlayer = controllingPlayer;
    }

    /** Wires the controller to a game and registers it as a listener for broadcasts. */
    public void connectModel ( Game game ) {
        this.game = game;
        game.addControllerListener(this);
    }

    /**
     * Changes the player the controller is connected to, is used in reconnection to change between connection types,
     * and might be useful to change replace a player with another player.
     * @param controllingPlayer, the new proxy to which the controller sends updates.
     */
    public void setNewControllingPlayer (PhasedProxyPlayer controllingPlayer) {
        this.controllingPlayer = controllingPlayer;
    }


    /**
     * Dispatches a client command to the matching {@link Game} action for this player.
     * A {@link PlayerException} thrown by the model is sent back as an {@link ErrorPacket}
     * only to the player who issued the command.
     */
    public void executeCommand ( CommandPacket command ) {
        PlayerActionEnum action = command.getAction();
        saveStateAsync();
        try {
            switch (action) {
                case CHOOSE_TOTEM:
                    game.chooseTotem(controllingPlayerIndex, command.getTotem());
                    break;
                case DRAW_UPPER_CHARACTER:
                    game.drawUpperCharacter(controllingPlayerIndex, command.getIndex());
                    break;
                case DRAW_LOWER_CHARACTER:
                    game.drawLowerCharacter(controllingPlayerIndex, command.getIndex());
                    break;
                case DRAW_UPPER_BUILDING:
                    game.drawUpperBuilding(controllingPlayerIndex, command.getIndex());
                    break;
                case DRAW_LOWER_BUILDING:
                    game.drawLowerBuilding(controllingPlayerIndex, command.getIndex());
                    break;
                case CHOOSE_OFFER:
                    game.chooseOffer(controllingPlayerIndex, command.getIndex());
                    break;
                case PASS_TURN:
                    game.passYourTurn(controllingPlayerIndex);
                    break;
                case DISCONNECT:
                    game.disconnectPlayer(controllingPlayerIndex);
                    break;
                case CONNECT:
                    game.connectPlayer(controllingPlayerIndex);
                    break;
                default:
                    // !ERROR!
                    break;
            }
        } catch (PlayerException e) {
            reportError(new ErrorPacket(e.getTitle(), e.getMessage(), false));
        }
    }

    /** Sends a full game snapshot to the client; disconnects the player on delivery failure. */
    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) {
        try {
            controllingPlayer.initializeClientModel(initializeModelPacket);
        } catch (Exception e) {
            game.disconnectPlayer(controllingPlayerIndex);
        }
    }

    /** Sends an incremental update to the client; disconnects the player on delivery failure. */
    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) {
        try {
            controllingPlayer.updateClientModel(updateModelPacket);
        } catch (Exception e) {
            game.disconnectPlayer(controllingPlayerIndex);
        }
    }

    /** Sends an error to the player; disconnects on delivery failure. */
    @Override
    public void reportError ( ErrorPacket errorPacket ) {
        try {
            controllingPlayer.reportError(errorPacket);
        } catch (Exception e) {
            game.disconnectPlayer(controllingPlayerIndex);
        }
    }
}
