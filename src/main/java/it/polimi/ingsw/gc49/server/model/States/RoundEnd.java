package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.server.model.Game;

public class RoundEnd extends State {
    public RoundEnd ( Game game ) { super(game); }

    public State executeState () {
        try {
            game.getCardBoard().endRound(game.getNumOfPlayers());
        } catch (EraEndedException e) {
            return new EraEnd(game); //goes to EraEnd.
        }
        return new OfferChoosing(game); //goes to OfferChoosing.
    }
}
