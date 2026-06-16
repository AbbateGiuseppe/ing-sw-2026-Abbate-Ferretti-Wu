package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Cardboard.FullCardboardModelElement;
import it.polimi.ingsw.gc49.server.model.Game;

public class EraEnd extends State {
    public EraEnd ( Game game ) { super(game, States.OTHER); }

    public State executeState () {
        try {
            game.getCardBoard().endEra(game.getCardBoard().getNextEra());
            game.queueUpdateModelElement(
                    new FullCardboardModelElement(
                            "-Il round si è concluso ed è finita l'era, le carte in gioco sono state rinnovate-",
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
                            "-Il round si è concluso ed è l'ultimo round!! le carte in gioco sono state rinnovate-",
                            game.getCardBoard().getDiscards(),
                            game.getCardBoard().getCurrentEra(),
                            game.getCardBoard().getLine().getUpperLine(),
                            game.getCardBoard().getLine().getLowerLine(),
                            game.getCardBoard().getLine().getUpperBuilding(),
                            game.getCardBoard().getLine().getLowerBuilding()
                    )
            );
        }

        return new OfferChoosing(game); //goes to OfferChoosing.
    }

    @Override
    public String toString () {
        return "Fine d'era";
    }
}
