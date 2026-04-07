package it.polimi.ingsw.gc49.model.States;

import it.polimi.ingsw.gc49.model.CardBoard.CardBoard;
import it.polimi.ingsw.gc49.model.EventManager;
import it.polimi.ingsw.gc49.model.Game;
import it.polimi.ingsw.gc49.model.Player;
import it.polimi.ingsw.gc49.model.Track.Track;

import java.util.ArrayList;
import java.util.List;

public class InitialSetup extends State {
    public InitialSetup ( Game game ) {
        super(game);
    }

    public State executeState () {
        int numOfPlayers = game.getNumOfPlayers();
        List<Player> players = new ArrayList<>(numOfPlayers);
        game.setPlayers(players);

        String[] playersNicknames = game.getPlayersNicknames();
        switch(numOfPlayers){
            case 2:
                players.set(0, new Player(playersNicknames[0], 0));
                players.set(1, new Player(playersNicknames[1], 1));
                break;
            case 3:
                players.set(0, new Player(playersNicknames[0], 0));
                players.set(1, new Player(playersNicknames[1], 1));
                players.set(2, new Player(playersNicknames[2], 2));
                break;
            case 4:
                players.set(0, new Player(playersNicknames[0], 0));
                players.set(1, new Player(playersNicknames[1], 1));
                players.set(2, new Player(playersNicknames[2], 2));
                players.set(3, new Player(playersNicknames[3], 3));
                break;
            case 5:
                players.set(0, new Player(playersNicknames[0], 0));
                players.set(1, new Player(playersNicknames[1], 1));
                players.set(2, new Player(playersNicknames[2], 2));
                players.set(3, new Player(playersNicknames[3], 3));
                players.set(4, new Player(playersNicknames[4], 4));
                break;
        }

        game.setLastRound(false);
        game.setEventManager(new EventManager()); ///chiedui a max
        game.setTrack(new Track(numOfPlayers));
        game.setCardBoard(new CardBoard(game));

        while (game.getUsedTotems().size() < numOfPlayers) { //waits until every player has chosen a totem.
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        game.getTrack().randomizeStartingOrder(game.getPlayers()); //randomizes the starting order.

        return new OfferChoosing(game); //goes to the offer choosing state as the next state.
    }
}
