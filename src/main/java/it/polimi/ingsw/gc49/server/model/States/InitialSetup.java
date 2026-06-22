package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.server.model.CardBoard.CardBoard;
import it.polimi.ingsw.gc49.server.model.EventManager;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.Track.Track;

import java.util.ArrayList;
import java.util.List;


/**
 * Represents the initial setup state of the game.
 * <p>
 * This state is responsible for initializing the core components of the game,
 * including creating the players, setting up the track, the event manager, and the card board.
 * Once setup is complete, it transitions the game to the {@link TotemChoosing} state.
 */
public class InitialSetup extends State {
    private final int numOfPlayers;
    private final List<String> playersNicknames;


    /**
     * Constructs the InitialSetup state.
     *
     * @param game             The main game instance.
     * @param locks            The synchronization locks for thread-safe state execution.
     * @param numOfPlayers     The total number of players in the game.
     * @param playersNicknames A list containing the nicknames of all participating players.
     */
    public InitialSetup ( Game game, Locks locks, int numOfPlayers, List<String> playersNicknames ) {
        super(game, States.OTHER, locks);
        this.numOfPlayers = numOfPlayers;
        this.playersNicknames = playersNicknames;
    }


    /**
     * Executes the initial setup logic for the game.
     * <p>
     * Instantiates the required number of {@link Player} objects and assigns their nicknames.
     * It also initializes the event manager, the track, and the main card board.
     *
     * @return The next state in the machine: {@link TotemChoosing}.
     */
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

        return new TotemChoosing(game, locks); //goes to the totem choosing state as the next state.
    }

    /**
     * Returns the human-readable name of this state.
     *
     * @return A string representing the state name ("Preparazione").
     */
    @Override
    public String toString () {
        return "Preparazione";
    }
}
