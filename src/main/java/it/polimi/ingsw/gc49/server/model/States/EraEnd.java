package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Game;

public class EraEnd extends State {
    public EraEnd ( Game game ) { super(game); }

    public State executeState () {
        try {
            game.getCardBoard().endEra(Era.FIRST); //TODO: implement era storing.
        } catch (DeckEmptiedException e) {
            game.setLastRound(true); //sets the cycle as last round.
        }

        return new OfferChoosing(game); //goes to OfferChoosing.
    }
}
