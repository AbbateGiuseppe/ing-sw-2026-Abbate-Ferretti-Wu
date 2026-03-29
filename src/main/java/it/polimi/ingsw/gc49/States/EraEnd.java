package it.polimi.ingsw.gc49.States;

import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.Model;

public class EraEnd extends State {
    public EraEnd ( Model model ) { super(model);}

    public State executeState () {
        try {
            model.getCardBoard().endEra(Era.FIRST); //TODO: implement era storing.
        } catch (DeckEmptiedException e) {
            model.setLastRound(true); //sets the cycle as last round.
        }

        return new OfferChoosing(model); //goes to OfferChoosing.
    }
}
