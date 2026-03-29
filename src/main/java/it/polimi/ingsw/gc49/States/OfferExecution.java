package it.polimi.ingsw.gc49.States;

import it.polimi.ingsw.gc49.Model;
import it.polimi.ingsw.gc49.Player;

public class OfferExecution extends State{
    public OfferExecution ( Model model ) { super(model);}

    public State executeState () {
        Player currentPlayer = model.getTrack().getNextPlayerOfferAndActivate();
        while ( currentPlayer != null) { //loops until there's no player remaining to choose his actions.
            model.setCurrentPlayer(currentPlayer);
            model.setCurrentPlayerIndex(currentPlayer.getPlayerIndex());
            while (!currentPlayer.hasActionsLeft()){ //waits until the current player has no actions left.
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            currentPlayer = model.getTrack().getNextPlayerOfferAndActivate();
        }

        //TODO: implement switch to GameEnd if it's the last round.
        return new RoundEnd(model); //goes to RoundEnd.
    }
}
