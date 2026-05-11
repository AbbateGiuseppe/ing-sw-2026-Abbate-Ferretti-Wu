package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.server.model.CardBoard.CardBoard;
import it.polimi.ingsw.gc49.server.model.EventManager;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.Track.Track;

import java.util.ArrayList;
import java.util.List;

public class InitialSetup extends State {
    private final int numOfPlayers;
    private final List<String> playersNicknames;

    public InitialSetup ( Game game, int numOfPlayers, List<String> playersNicknames ) {
        super(game, States.OTHER);
        this.numOfPlayers = numOfPlayers;
        this.playersNicknames = playersNicknames;
    }

    public State executeState () {
        List<Player> players = new ArrayList<>(numOfPlayers);
        game.setPlayers(players);

        switch(numOfPlayers){
            case 2:
                players.add(0, new Player(playersNicknames.get(0), 0));
                players.add(1, new Player(playersNicknames.get(1), 1));
                break;
            case 3:
                players.add(0, new Player(playersNicknames.get(0), 0));
                players.add(1, new Player(playersNicknames.get(1), 1));
                players.add(2, new Player(playersNicknames.get(2), 2));
                break;
            case 4:
                players.add(0, new Player(playersNicknames.get(0), 0));
                players.add(1, new Player(playersNicknames.get(1), 1));
                players.add(2, new Player(playersNicknames.get(2), 2));
                players.add(3, new Player(playersNicknames.get(3), 3));
                break;
            case 5:
                players.add(0, new Player(playersNicknames.get(0), 0));
                players.add(1, new Player(playersNicknames.get(1), 1));
                players.add(2, new Player(playersNicknames.get(2), 2));
                players.add(3, new Player(playersNicknames.get(3), 3));
                players.add(4, new Player(playersNicknames.get(4), 4));
                break;
        }

        game.setLastRound(false);
        game.setEventManager(new EventManager());
        game.setTrack(new Track(numOfPlayers));
        game.setCardBoard(new CardBoard(game));

        return new TotemChoosing(game); //goes to the totem choosing state as the next state.
    }
}
