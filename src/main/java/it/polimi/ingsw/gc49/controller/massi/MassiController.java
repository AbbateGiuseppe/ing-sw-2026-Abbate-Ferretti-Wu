package it.polimi.ingsw.gc49.controller.massi;

import it.polimi.ingsw.gc49.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.model.Game;
import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualGameClient;

public class MassiController implements VirtualGameClient {
    private Game game;
    private final int controllingPlayerIndex;
    private final PhasedProxyPlayer controllingPlayer;

    public MassiController ( int controllingPlayerIndex, PhasedProxyPlayer controllingPlayer ) {
        game = null;
        this.controllingPlayerIndex = controllingPlayerIndex;
        this.controllingPlayer = controllingPlayer;
    }

    public void connectModel ( Game game ) {
        this.game = game;
        game.addControllerListener(this);
    }

    public void executeCommand ( CommandPacket command ) {
        MassiPlayerActionEnum action = command.getAction();
        switch ( action ) {
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
    }

    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception {
        controllingPlayer.initializeClientModel(initializeModelPacket);
    }

    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception {
        controllingPlayer.updateClientModel(updateModelPacket);
    }

    @Override
    public void reportError ( ErrorPacket errorPacket ) throws Exception {
        controllingPlayer.reportError(errorPacket);
    }
}
