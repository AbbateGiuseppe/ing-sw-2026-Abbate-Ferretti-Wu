package it.polimi.ingsw.gc49.server.model.States;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsAllModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.TextModelElement;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Player;

import java.util.Comparator;
import java.util.List;

public class GameEnd extends State {
    private static final int NUMBER_OF_ARTISTS_FOR_POINTS = 2;
    private static final int POINTS_PER_NUMBER_OF_ARTISTS = 10;

    public GameEnd ( Game game ) { super(game, States.OTHER);}

    public State executeState () {
        game.setPhaseStatus("Game End");
        if (game.hasForcedWinner()) {
            Player winner = game.getForcedWinner();
            List<Player> players = game.getPlayers();
            String standingsMessage = "1." + winner.getNickname() + "(victory by disconnection timeout), ";
            game.setFinalStandings(standingsMessage);
            game.queueUpdateModelElement(
                    new FoodAndPointsAllModelElement(
                            "Classifica: " + standingsMessage,
                            FoodAndPointsAllModelElement.getNewFood(players),
                            FoodAndPointsAllModelElement.getNewPoints(players)
                    )
            );
            game.queueUpdateModelElement(
                    new TextModelElement("HA VINTO " + winner.getNickname() + "!!!")
            );
            game.queueStatusUpdate("");
            return null;
        }

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
                .sorted(Comparator.comparingInt(Player::getPoints).reversed().thenComparingInt(Player::getFood).reversed()) //sorts the standings based on the descending order of points. breaks the ties with a descending order of food.
                .toList(); //puts the results to list.
        // !!! THERE STILL MIGHT BE SOME TIED PLAYERS, THEY SHOULD BE CONSIDERED AT THE SAME STANDING !!!

        //send the standings to the players
        StringBuilder standingsMessage = new StringBuilder();
        for(int i=0; i<standings.size(); i++){
            standingsMessage.append(i+1).append(".").append(standings.get(i).getNickname()).append("(").append(standings.get(i).getPoints()).append("), ");
        }
        game.setFinalStandings(standingsMessage.toString());
        game.queueUpdateModelElement(
                new FoodAndPointsAllModelElement(
                        "Classifica: " + standingsMessage,
                        FoodAndPointsAllModelElement.getNewFood(players),
                        FoodAndPointsAllModelElement.getNewPoints(players)
                )
        );

        //send the winner
        String winner = standings.get(0).getNickname();
        game.queueUpdateModelElement(
                new TextModelElement("HA VINTO " + winner + "!!!")
        );
        game.queueStatusUpdate("");

        return null;
    }

    @Override
    public String toString () {
        return "Fine della partita";
    }
}
