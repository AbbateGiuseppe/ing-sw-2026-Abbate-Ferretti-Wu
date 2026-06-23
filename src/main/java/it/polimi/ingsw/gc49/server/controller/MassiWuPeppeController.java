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

public class MassiWuPeppeController implements VirtualGameClient, Serializable{
    private Game game;
    private final int controllingPlayerIndex;
    private PhasedProxyPlayer controllingPlayer;

    public MassiWuPeppeController ( int controllingPlayerIndex, PhasedProxyPlayer controllingPlayer ) {
        game = null;
        this.controllingPlayerIndex = controllingPlayerIndex;
        this.controllingPlayer = controllingPlayer;
    }

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

    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) {
        try {
            controllingPlayer.initializeClientModel(initializeModelPacket);
        } catch (Exception e) {
            game.disconnectPlayer(controllingPlayerIndex);
        }
    }

    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) {
        try {
            controllingPlayer.updateClientModel(updateModelPacket);
        } catch (Exception e) {
            game.disconnectPlayer(controllingPlayerIndex);
        }
    }

    @Override
    public void reportError ( ErrorPacket errorPacket ) {
        try {
            controllingPlayer.reportError(errorPacket);
        } catch (Exception e) {
            game.disconnectPlayer(controllingPlayerIndex);
        }
    }
}
