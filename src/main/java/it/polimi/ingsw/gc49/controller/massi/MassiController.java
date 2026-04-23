package it.polimi.ingsw.gc49.controller.massi;

import it.polimi.ingsw.gc49.datapacket.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.model.Game;

public class MassiController {
    private Game game;
    private final int controllingPlayerIndex;

    public MassiController ( int controllingPlayerIndex ) {
        game = null;
        this.controllingPlayerIndex = controllingPlayerIndex;
    }

    public void connectModel ( Game game ) {
        this.game = game;
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
}
