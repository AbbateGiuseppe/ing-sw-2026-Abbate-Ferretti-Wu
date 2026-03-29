package it.polimi.ingsw.gc49.States;

import it.polimi.ingsw.gc49.Model;

public class RoundEnd extends State {
    public RoundEnd ( Model model ) { super(model);}

    public State executeState () {
        try {
            model.getCardBoard().endRound(model.getNumOfPlayers());
        } catch (EraEndedException e) {
            return new EraEnd(model);
        }
        return new OfferChoosing(model);
    }
}
