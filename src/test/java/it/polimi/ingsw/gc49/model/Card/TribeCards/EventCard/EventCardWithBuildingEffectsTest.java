package it.polimi.ingsw.gc49.model.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.server.model.*;
import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.*;
import it.polimi.ingsw.gc49.server.model.Card.EventCard.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventCardWithBuildingEffectsTest {
    EventManager eventManager;
    Player p;
    private Era era = Era.FIRST;
    private int minNumPlayers = 3;

    @BeforeEach
    void setUp() {
        eventManager = new EventManager();
        p = new Player(null,0);
    }

    @Test
    public void testHuntingEventWithBuildingEffect(){
        int pointsPerHunter = 2;
        EventCard card = new HuntingEvent(pointsPerHunter,eventManager,era,minNumPlayers, null);

        int PPReward = 0;
        int foodPrice = 0;
        BuildingCard bcard = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT,PPReward,foodPrice,era,minNumPlayers, null);

        int numHunters = 3;
        p.data.addCharacterCount(CharacterType.Hunter,numHunters);

        bcard.updateDataBank(p.data);
        bcard.onDraw(p);
        bcard.addBuildingToManager(p,eventManager);

        int expectedFood = p.getFood() + numHunters + numHunters;
        int expectedPoints = p.getPoints() + numHunters * pointsPerHunter + numHunters;
        card.resolveEvent(List.of(p));
        int actualFood = p.getFood();
        int actualPoints = p.getPoints();
        assertEquals(expectedFood,actualFood);
        assertEquals(expectedPoints,actualPoints);
    }

    @Test
    public void testPaintingEventWithBuildingEffect(){
        int threshold = 2;
        int plusPoints = 2;
        int minusPoints = 3;
        EventCard card = new PaintingEvent(threshold,plusPoints,minusPoints,eventManager,era,minNumPlayers, null);

        int PPReward = 0;
        int foodPrice = 0;
        BuildingCard bcard = new BonusPaintingCard(BuildingEvent.PAINTING_EVENT,PPReward,foodPrice,era,minNumPlayers, null);

        int numArtists = 3;
        p.data.addCharacterCount(CharacterType.Artist, numArtists);

        bcard.updateDataBank(p.data);
        bcard.onDraw(p);
        bcard.addBuildingToManager(p,eventManager);

        int expectedPoints = p.getPoints() + plusPoints * numArtists;
        int expectedFood = p.getFood() + numArtists;
        card.resolveEvent(List.of(p));
        int actualFood = p.getFood();
        int actualPoints = p.getPoints();
        assertEquals(expectedFood,actualFood);
        assertEquals(expectedPoints,actualPoints);
    }

    @Test
    public void testSustenanceEventWithBuildingEffect() {
        int minusPoints = 2;
        EventCard card = new SustenanceEvent(minusPoints,eventManager,era,minNumPlayers, null);

        int PPReward = 0;
        int foodPrice = 0;
        BuildingCard bcard = new SustainDiscountByClassCard(CharacterType.Artist,BuildingEvent.SUSTENANCE_EVENT,PPReward,foodPrice,era,minNumPlayers, null);

        int numArtists = 3;
        int numHunters = 5;
        int food = 10;
        p.data.addCharacterCount(CharacterType.Artist,numArtists);
        p.data.addCharacterCount(CharacterType.Hunter,numHunters);
        p.addFood(food);

        bcard.updateDataBank(p.data);
        bcard.onDraw(p);
        bcard.addBuildingToManager(p,eventManager);

        int expectedFood = 10 - (numArtists + numHunters - numArtists);
        card.resolveEvent(List.of(p));
        int actualFood = p.getFood();
        assertEquals(expectedFood,actualFood);
    }

    @Test
    public void testRitualEventWithBuildingEffect() {
        int plusPoints = 10;
        int minusPoints = 5;
        EventCard card = new RitualEvent(plusPoints,minusPoints,eventManager,era,minNumPlayers, null);

        List<Player> ls = new ArrayList<>();
        int numPlayers = 3;
        for ( int i = 0 ; i < numPlayers ; i++ ) {
            ls.add(new Player(null,i));
        }
        List<Integer> numStars = List.of(1,1,3);
        ls.forEach(p -> p.data.addNumStar(numStars.get(p.getPlayerIndex())));

        int PPReward = 0;
        int foodPrice = 0;
        BuildingCard bcard1 = new ShamanicImmunityCard(BuildingEvent.RITUAL_EVENT,PPReward,foodPrice,era,minNumPlayers, null);
        bcard1.updateDataBank(ls.get(0).data);
        bcard1.onDraw(ls.get(0));
        bcard1.addBuildingToManager(ls.get(0),eventManager);
        BuildingCard bcard2 = new ShamanicThreeStarCard(BuildingEvent.RITUAL_EVENT,PPReward,foodPrice,era,minNumPlayers, null);
        bcard2.updateDataBank(ls.get(1).data);
        bcard2.onDraw(ls.get(1));
        bcard2.addBuildingToManager(ls.get(1),eventManager);
        BuildingCard bcard3 = new DoubleShamanPointsCard(BuildingEvent.RITUAL_EVENT,PPReward,foodPrice,era,minNumPlayers, null);
        bcard3.updateDataBank(ls.get(1).data);
        bcard3.onDraw(ls.get(1));
        bcard3.addBuildingToManager(ls.get(1),eventManager);
        BuildingCard bcard4 = new DoubleShamanPointsCard(BuildingEvent.RITUAL_EVENT,PPReward,foodPrice,era,minNumPlayers, null);
        bcard4.updateDataBank(ls.get(2).data);
        bcard4.onDraw(ls.get(2));
        bcard4.addBuildingToManager(ls.get(2),eventManager);


        int[] expectedPoints = new int[]{ls.get(0).getPoints(), ls.get(1).getPoints() + plusPoints * 2, ls.get(2).getPoints()};
        card.resolveEvent(ls);
        int[] actualPoints = ls.stream().mapToInt(Player::getPoints).toArray();
        assertArrayEquals(expectedPoints,actualPoints);
    }

}
