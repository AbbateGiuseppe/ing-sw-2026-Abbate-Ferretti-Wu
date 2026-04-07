package it.polimi.ingsw.gc49.model.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventCardTestWithoutBuildingEffect {
    int numPlayers = 3;
    List<Player> ls;

    @BeforeEach
    void setUp() {
        // Comment eventManager.invokeEvent in the event cards
        ls = new ArrayList<>();
        for ( int i = 0 ; i < numPlayers ; i++ ) {
            ls.add(new Player(null,i));
        }
    }

    @Test
    public void testHuntingEvent(){
        /*//Game game = new Game();
        int pointsPerHunter = 2;
        EventCard card = new HuntingEvent(pointsPerHunter, Era.FIRST, 2);
        int[] numHunters = {3, 5, 0};
        ls.forEach(p -> p.data.addCharacterCount(CharacterType.Hunter,numHunters[p.getPlayerIndex()]));
        int[] expectedFood = ls.stream().mapToInt(p -> p.getFood() + numHunters[p.getPlayerIndex()]).toArray();
        int[] expectedPoints = ls.stream().mapToInt(p -> p.getPoints() + numHunters[p.getPlayerIndex()] * pointsPerHunter).toArray();
        card.resolveEvent(ls);
        int[] actualFood = ls.stream().mapToInt(Player::getFood).toArray();
        int[] actualPoints = ls.stream().mapToInt(Player::getPoints).toArray();
        assertArrayEquals(expectedFood,actualFood);
        assertArrayEquals(expectedPoints,actualPoints);*/
    }

    @Test
    public void testPaintingEvent(){
        /*
        int threshold = 2;
        int plusPoints = 2;
        int minusPoints = 3;
        EventCard card = new PaintingEvent(threshold,plusPoints,minusPoints, Era.FIRST, 2);
        int[] numArtist = {3, 2, 1};
        ls.forEach(p -> p.data.addCharacterCount(CharacterType.Artist,numArtist[p.getPlayerIndex()]));
        int[] expectedPoints = ls.stream().mapToInt(p -> numArtist[p.getPlayerIndex()] >= threshold ? p.getPoints() + plusPoints * numArtist[p.getPlayerIndex()] : p.getPoints() - minusPoints).toArray();
        card.resolveEvent(ls);
        int[] actualPoints = ls.stream().mapToInt(Player::getPoints).toArray();
        assertArrayEquals(expectedPoints,actualPoints);*/
    }

    @Test
    public void testSustenanceEvent(){
        /*int minusPoints = 2;
        EventCard card = new SustenanceEvent(minusPoints);
        int[] foods = {10,5,2};
        ls.forEach(p -> p.addFood(foods[p.getPlayerIndex()]));
        int[] numGatherers = {1,3,0};
        ls.forEach(p -> p.data.addCharacterCount(CharacterType.Gatherer,numGatherers[p.getPlayerIndex()]));
        int[] numHunters = {5,3,7}; // just to increase the number of character cards
        ls.forEach(p -> p.data.addCharacterCount(CharacterType.Hunter,numHunters[p.getPlayerIndex()]));
        int[] expectedFood = ls.stream().mapToInt(p -> p.getFood() + p.data.getNumSustenanceDiscount() >= p.data.getNumCharacters() ? Math.min(p.getFood(),p.getFood() + p.data.getNumSustenanceDiscount() - p.data.getNumCharacters()): 0).toArray();
        int[] expectedPoints = ls.stream().mapToInt(p -> p.getFood() + p.data.getNumSustenanceDiscount() >= p.data.getNumCharacters() ? p.getPoints() : p.getPoints() - minusPoints * (p.data.getNumCharacters() - p.getFood() - p.data.getNumSustenanceDiscount())).toArray();
        card.resolveEvent(ls);
        int[] actualFood = ls.stream().mapToInt(Player::getFood).toArray();
        int[] actualPoints = ls.stream().mapToInt(Player::getPoints).toArray();
        assertArrayEquals(expectedPoints,actualPoints);
        assertArrayEquals(expectedFood,actualFood);*/
    }

    @Test
    public void testRitualEvent() {
        /*int plusPoints = 10;
        int minusPoints = 5;
        EventCard card = new RitualEvent(plusPoints,minusPoints);
        List<Integer> numStars = List.of(1,3,3);
        int maxStar = Collections.max(numStars);
        int minStar = Collections.min(numStars);
        ls.forEach(p -> p.data.addNumStar(numStars.get(p.getPlayerIndex())));
        int[] expectedPoints = ls.stream().mapToInt(p -> p.data.getNumStars() == maxStar ? p.getPoints() + plusPoints : p.data.getNumStars() == minStar ? p.getPoints() - minusPoints : p.getPoints()).toArray();
        card.resolveEvent(ls);
        int[] actualPoints = ls.stream().mapToInt(Player::getPoints).toArray();
        assertArrayEquals(expectedPoints,actualPoints);*/
    }

}
