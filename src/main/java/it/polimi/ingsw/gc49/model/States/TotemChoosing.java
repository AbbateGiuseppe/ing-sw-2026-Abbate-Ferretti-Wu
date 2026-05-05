package it.polimi.ingsw.gc49.model.States;

import it.polimi.ingsw.gc49.model.Game;
import it.polimi.ingsw.gc49.model.Locks;

public class TotemChoosing extends State {
    public TotemChoosing ( Game game ) { super(game); }

    public State executeState () {
        synchronized (playerInputLock) {
            int numOfPlayers = game.getNumOfPlayers();
            while (game.getUsedTotems().size() < numOfPlayers) { //waits until every player has chosen a totem.
                try {
                    playerInputLock.wait();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }

            game.getTrack().randomizeStartingOrder(game.getPlayers()); //randomizes the starting order.

            return new OfferChoosing(game);
        }
    }
}
