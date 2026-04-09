package it.polimi.ingsw.gc49.model.States;

import it.polimi.ingsw.gc49.model.Game;
import it.polimi.ingsw.gc49.model.Player;

public class OfferExecution extends State{
    public OfferExecution ( Game game ) { super(game);}

    public State executeState () {
        Player currentPlayer = game.getTrack().getNextPlayerOfferAndActivate();
        while (currentPlayer != null) { //loops until there's no player remaining to choose his actions.
            game.setCurrentPlayer(currentPlayer);
            game.setCurrentPlayerIndex(currentPlayer.getPlayerIndex());
            while (!currentPlayer.hasActionsLeft()){ //waits until the current player has no actions left.
                try {
                    wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
            game.getTrack().deassignCurrentOffer(); //deassigning the player from the offer and placing him in the order slots.
            game.callTurnEndEvent(); //calls all the buildings that activate at a turn's end.
            currentPlayer = game.getTrack().getNextPlayerOfferAndActivate();
        }
        game.callRoundEndEvent(); //calls all the buildings that activate at the end of a round.

        if(game.isLastRound()){
            return new GameEnd(game); //goes to GameEnd.
        }else {
            return new RoundEnd(game); //goes to RoundEnd.
        }
    }
}
