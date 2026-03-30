package it.polimi.ingsw.gc49.States;

import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.Model;
import it.polimi.ingsw.gc49.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GameEnd extends State {
    private static final int NUMBER_OF_ARTISTS_FOR_POINTS = 2;
    private static final int POINTS_PER_NUMBER_OF_ARTISTS = 10;

    public GameEnd ( Model model ) { super(model);}

    public State executeState () {
        //solving the last events
        model.getCardBoard().endGame();

        //calculating the end game points
        List<Player> players = model.getPlayers();
        for(Player player : players){
            player.addPoints( player.data.getNumBuilderPoints() ); //adds the points of the player's builder cards.
            player.addPoints( player.data.getCharacterCount(CharacterType.Inventor) * player.data.getDifferentInventionCount() ); //adds the result of the multiplication between the number of inventors and the number of different inventions to the player's points.
            player.addPoints( (player.data.getCharacterCount(CharacterType.Artist) / NUMBER_OF_ARTISTS_FOR_POINTS) * POINTS_PER_NUMBER_OF_ARTISTS ); //adds 10 points for every 2 artist.
            player.addPoints( player.data.getNumBuildingPoints() ); //adds the points of the player's buildings.
        }

        //forming the standings based on each of the players' points
        List<Player> standings = players.stream()
                .sorted(Comparator.comparingInt(Player::getPoints).reversed().thenComparingInt(Player::getFood).reversed()) //sorts the standings based on the descending order of points. breaks the ties with a descending order of food.
                .collect(Collectors.toList()); //puts the results to list.
        // !!! THERE STILL MIGHT BE SOME TIED PLAYERS, THEY SHOULD BE CONSIDERED AT THE SAME STANDING !!!

        return null;
    }
}
