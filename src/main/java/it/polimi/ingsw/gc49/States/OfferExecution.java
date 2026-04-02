package it.polimi.ingsw.gc49.States;

import it.polimi.ingsw.gc49.Game;
import it.polimi.ingsw.gc49.Player;

public class OfferExecution extends State{
    public OfferExecution ( Game game ) { super(game);}

    public State executeState () {
        Player currentPlayer = game.getTrack().getNextPlayerOfferAndActivate();
        while ( currentPlayer != null) { //loops until there's no player remaining to choose his actions.
            game.setCurrentPlayer(currentPlayer);
            game.setCurrentPlayerIndex(currentPlayer.getPlayerIndex());
            while (!currentPlayer.hasActionsLeft()){ //waits until the current player has no actions left.
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            currentPlayer = game.getTrack().getNextPlayerOfferAndActivate();
        }

        if(game.isLastRound()){
            return new GameEnd(game); //goes to GameEnd.
        }else {
            return new RoundEnd(game); //goes to RoundEnd.
        }
    }
}
