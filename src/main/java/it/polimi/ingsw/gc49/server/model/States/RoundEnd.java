package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Cardboard.FullCardboardModelElement;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;

public class RoundEnd extends State {
    public RoundEnd ( Game game, Locks locks ) { super(game, States.OTHER, locks); }

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
        } catch (EraEndedException e) {
            return new EraEnd(game, locks); //goes to EraEnd.
        }

        return new OfferChoosing(game, locks); //goes to OfferChoosing.
    }

    @Override
    public String toString () {
        return "Fine del round";
    }
}
