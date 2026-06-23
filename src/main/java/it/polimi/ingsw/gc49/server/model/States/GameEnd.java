package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.EndGameModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.GameStatusModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsAllModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.TextModelElement;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Locks;
import it.polimi.ingsw.gc49.server.model.Player;

import java.util.Comparator;
import java.util.List;


/**
 * Represents the final state of the game in the Finite State Machine.
 * <p>
 * This state handles the resolution of final events, calculates the end-game scoring
 * for all players, determines the final standings (breaking ties with food),
 * and broadcasts the winner and results to all clients.
 */
public class GameEnd extends State {
    private static final int NUMBER_OF_ARTISTS_FOR_POINTS = 2;
    private static final int POINTS_PER_NUMBER_OF_ARTISTS = 10;


    /**
     * Constructs the GameEnd state.
     *
     * @param game  The main game instance.
     * @param locks The synchronization locks for thread-safe state execution.
     */
    public GameEnd ( Game game, Locks locks ) { super(game, States.GAME_END, locks);}


    /**
     * Executes the end-of-game logic, calculates final scores, and determines the winner.
     *
     * @return {@code null}, as this is the final state and halts the Finite State Machine.
     */
    public State executeState () {
        game.queueUpdateModelElement(new GameStatusModelElement("Siamo passati alla fine partita", currentStateType));
        game.broadcastGameUpdate();
        //solving the last events
        game.getCardBoard().endGame();

        //calling all the buildings that activate at the end of the game.
        game.callGameEndEvent();

        //calculating the end game points
        List<Player> players = game.getPlayers();



        for(Player player : players){
            player.addPoints( player.data.getNumBuilderPoints() ); //adds the points of the player's builder cards.
            player.addPoints( player.data.getCharacterCount(CharacterType.Inventor) * player.data.getDifferentInventionCount() ); //adds the result of the multiplication between the number of inventors and the number of different inventions to the player's points.
            player.addPoints( (player.data.getCharacterCount(CharacterType.Artist) / NUMBER_OF_ARTISTS_FOR_POINTS) * POINTS_PER_NUMBER_OF_ARTISTS ); //adds 10 points for every 2 artist.
            player.addPoints( player.data.getNumBuildingPoints() ); //adds the points of the player's buildings.
        }

        //forming the standings based on each of the players' points
        List<Player> standings = players.stream()
                .sorted(Comparator.comparingInt(Player::getPoints).reversed().thenComparingInt(Player::getFood)) //sorts the standings based on the descending order of points. breaks the ties with a descending order of food.
                .toList(); //puts the results to list.
        // !!! THERE STILL MIGHT BE SOME TIED PLAYERS, THEY SHOULD BE CONSIDERED AT THE SAME STANDING !!!

        //send the standings to the players
        StringBuilder standingsMessage = new StringBuilder();
        for(int i=0; i<standings.size(); i++){
            standingsMessage.append(i+1).append(".").append(standings.get(i).getNickname()).append("(").append(standings.get(i).getPoints()).append("), ");
        }
        game.queueUpdateModelElement(
                new EndGameModelElement(
                        "Classifica: " + standingsMessage,
                        FoodAndPointsAllModelElement.getNewFood(players),
                        FoodAndPointsAllModelElement.getNewPoints(players),
                        currentStateType
                )
        );

        //send the winner
        String winner = standings.getFirst().getNickname();
        game.queueUpdateModelElement(
                new TextModelElement("HA VINTO " + winner + "!!!")
        );

        return null;
    }

    /**
     * Returns the human-readable name of this state.
     *
     * @return A string representing the state name ("Fine della partita").
     */
    @Override
    public String toString () {
        return "End of the match";
    }
}
