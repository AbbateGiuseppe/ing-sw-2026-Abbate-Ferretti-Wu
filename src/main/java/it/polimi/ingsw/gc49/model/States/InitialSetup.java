package it.polimi.ingsw.gc49.model.States;

import it.polimi.ingsw.gc49.model.CardBoard.CardBoard;
import it.polimi.ingsw.gc49.model.EventManager;
import it.polimi.ingsw.gc49.model.Game;
import it.polimi.ingsw.gc49.model.Player;
import it.polimi.ingsw.gc49.model.Track.Track;

import java.util.ArrayList;
import java.util.List;

public class InitialSetup extends State {
    private final int numOfPlayers;
    private final String[] playersNicknames;

    public InitialSetup ( Game game, int numOfPlayers, String[] playersNicknames ) {
        super(game);
        this.numOfPlayers = numOfPlayers;
        this.playersNicknames = playersNicknames;
    }

    public State executeState () {
        List<Player> players = new ArrayList<>(numOfPlayers);
        game.setPlayers(players);

        switch(numOfPlayers){
            case 2:
                players.add(0, new Player(playersNicknames[0], 0));
                players.add(1, new Player(playersNicknames[1], 1));
                break;
            case 3:
                players.add(0, new Player(playersNicknames[0], 0));
                players.add(1, new Player(playersNicknames[1], 1));
                players.add(2, new Player(playersNicknames[2], 2));
                break;
            case 4:
                players.add(0, new Player(playersNicknames[0], 0));
                players.add(1, new Player(playersNicknames[1], 1));
                players.add(2, new Player(playersNicknames[2], 2));
                players.add(3, new Player(playersNicknames[3], 3));
                break;
            case 5:
                players.add(0, new Player(playersNicknames[0], 0));
                players.add(1, new Player(playersNicknames[1], 1));
                players.add(2, new Player(playersNicknames[2], 2));
                players.add(3, new Player(playersNicknames[3], 3));
                players.add(4, new Player(playersNicknames[4], 4));
                break;
        }

        game.setLastRound(false);
        game.setEventManager(new EventManager());
        game.setTrack(new Track(numOfPlayers));
        game.setCardBoard(new CardBoard(game));

        return new TotemChoosing(game); //goes to the totem choosing state as the next state.
    }
}
