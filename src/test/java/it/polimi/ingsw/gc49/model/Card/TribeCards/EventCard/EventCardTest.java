package it.polimi.ingsw.gc49.model.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.TribeCards.CharacterCard.Gatherer;
import it.polimi.ingsw.gc49.server.model.Card.TribeCards.CharacterCard.Hunter;
import it.polimi.ingsw.gc49.server.model.Card.TribeCards.EventCard.HuntingEvent;
import it.polimi.ingsw.gc49.server.model.Card.TribeCards.EventCard.PaintingEvent;
import it.polimi.ingsw.gc49.server.model.Card.TribeCards.EventCard.RitualEvent;
import it.polimi.ingsw.gc49.server.model.Card.TribeCards.EventCard.SustenanceEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.EventManager;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EventCardTest {

    private EventManager eventManager;
    private List<Player> players;
    private Player player1;
    private Player player2;
    private Player player3;

    @BeforeEach
    void setUp() {
        eventManager = new EventManager();
        players = new ArrayList<>();

        player1 = new Player("Player1", 0);
        player2 = new Player("Player2", 1);
        player3 = new Player("Player3", 2);

        players.add(player1);
        players.add(player2);
        players.add(player3);
    }

    @Test
    void testHuntingEvent_canGet_alwaysFalse() {
        HuntingEvent event = new HuntingEvent(2, eventManager, Era.FIRST, 2);
        assertFalse(event.canGet(player1), "Event cards should never be drawable by players");
    }

    @Test
    void testHuntingEvent_resolveEvent_givesFood() {
        player1.data.addCharacterCount(CharacterType.Hunter, 3);
        player2.data.addCharacterCount(CharacterType.Hunter, 1);

        HuntingEvent event = new HuntingEvent(2, eventManager, Era.FIRST, 2);
        event.resolveEvent(players);

        assertEquals(3, player1.getFood(), "Player1 should gain food equal to hunter count");
        assertEquals(1, player2.getFood(), "Player2 should gain food equal to hunter count");
        assertEquals(0, player3.getFood(), "Player3 with no hunters should gain no food");
    }

    @Test
    void testHuntingEvent_resolveEvent_givesPoints() {
        player1.data.addCharacterCount(CharacterType.Hunter, 3);
        player2.data.addCharacterCount(CharacterType.Hunter, 2);

        HuntingEvent event = new HuntingEvent(5, eventManager, Era.SECOND, 2);
        event.resolveEvent(players);

        assertEquals(15, player1.getPoints(), "Player1 should gain 3 hunters * 5 points = 15");
        assertEquals(10, player2.getPoints(), "Player2 should gain 2 hunters * 5 points = 10");
        assertEquals(0, player3.getPoints(), "Player3 should gain 0 points");
    }

    @Test
    void testPaintingEvent_canGet_alwaysFalse() {
        PaintingEvent event = new PaintingEvent(2, 3, 4, eventManager, Era.SECOND, 2);
        assertFalse(event.canGet(player1), "Event cards should never be drawable by players");
    }

    @Test
    void testPaintingEvent_belowThreshold_losesPoints() {
        player1.data.addCharacterCount(CharacterType.Artist, 1);
        player2.data.addCharacterCount(CharacterType.Artist, 0);

        PaintingEvent event = new PaintingEvent(2, 3, 4, eventManager, Era.SECOND, 2);
        event.resolveEvent(players);

        assertEquals(-4, player1.getPoints(), "Player1 below threshold should lose 4 points");
        assertEquals(-4, player2.getPoints(), "Player2 below threshold should lose 4 points");
    }

    @Test
    void testPaintingEvent_aboveThreshold_gainsPoints() {
        player1.data.addCharacterCount(CharacterType.Artist, 3);
        player2.data.addCharacterCount(CharacterType.Artist, 2);

        PaintingEvent event = new PaintingEvent(2, 5, 10, eventManager, Era.SECOND, 2);
        event.resolveEvent(players);

        assertEquals(15, player1.getPoints(), "Player1 with 3 artists should gain 3*5=15 points");
        assertEquals(10, player2.getPoints(), "Player2 with 2 artists should gain 2*5=10 points");
    }

    @Test
    void testPaintingEvent_exactThreshold_gainsPoints() {
        player1.data.addCharacterCount(CharacterType.Artist, 2);

        PaintingEvent event = new PaintingEvent(2, 4, 6, eventManager, Era.SECOND, 2);
        event.resolveEvent(players);

        assertEquals(8, player1.getPoints(), "Player1 at threshold should gain points (2*4=8)");
    }

    @Test
    void testRitualEvent_canGet_alwaysFalse() {
        RitualEvent event = new RitualEvent(10, 5, eventManager, Era.FIRST, 2);
        assertFalse(event.canGet(player1), "Event cards should never be drawable by players");
    }

    @Test
    void testRitualEvent_singleWinner_gainsPoints() {
        player1.data.addNumStar(5);
        player2.data.addNumStar(2);
        player3.data.addNumStar(1);

        RitualEvent event = new RitualEvent(10, 3, eventManager, Era.FIRST, 2);
        event.resolveEvent(players);

        assertEquals(10, player1.getPoints(), "Winner should gain 10 points");
        assertEquals(-3, player3.getPoints(), "Losers should lose 3 points");
    }

    @Test
    void testRitualEvent_multipleWinners_noUniqueFlag() {
        player1.data.addNumStar(5);
        player2.data.addNumStar(5);
        player3.data.addNumStar(2);

        RitualEvent event = new RitualEvent(10, 3, eventManager, Era.SECOND, 2);
        event.resolveEvent(players);

        assertEquals(10, player1.getPoints(), "Winner should gain 10 points");
        assertEquals(10, player2.getPoints(), "Winner should gain 10 points");
        assertFalse(player1.isUniqueWinner(), "Multiple winners should not be marked as unique");
        assertFalse(player2.isUniqueWinner(), "Multiple winners should not be marked as unique");
    }

    @Test
    void testRitualEvent_losers_losePoints() {
        player1.data.addNumStar(5);
        player2.data.addNumStar(1);
        player3.data.addNumStar(1);

        RitualEvent event = new RitualEvent(8, 6, eventManager, Era.THIRD_FINAL, 2);
        event.resolveEvent(players);

        assertEquals(8, player1.getPoints(), "Winner should gain 8 points");
        assertEquals(-6, player2.getPoints(), "Loser should lose 6 points");
        assertEquals(-6, player3.getPoints(), "Loser should lose 6 points");
    }

    @Test
    void testSustenanceEvent_canGet_alwaysFalse() {
        SustenanceEvent event = new SustenanceEvent(3, eventManager, Era.FIRST, 2);
        assertFalse(event.canGet(player1), "Event cards should never be drawable by players");
    }

    @Test
    void testSustenanceEvent_withEnoughFood_paysFood() {
        player1.addFood(10);
        player1.data.addCharacterCount(CharacterType.Hunter, 2);
        player1.data.addCharacterCount(CharacterType.Artist, 1);

        SustenanceEvent event = new SustenanceEvent(3, eventManager, Era.FIRST, 2);
        event.resolveEvent(players);

        assertEquals(7, player1.getFood(), "Player should pay 3 food (3 characters - 0 discount)");
        assertEquals(0, player1.getPoints(), "Player with enough food should not lose points");
    }

    @Test
    void testSustenanceEvent_withGathererDiscount() {
        player1.addFood(10);
        Card hunter = new Hunter(false, Era.FIRST, 2);
        player1.addCharacterCard(hunter);
        player1.addCharacterCard(hunter);
        player1.addCharacterCard(hunter);
        Card gatherer = new Gatherer(Era.FIRST, 2);
        player1.addCharacterCard(gatherer);

        SustenanceEvent event = new SustenanceEvent(2, eventManager, Era.FIRST, 2);
        event.resolveEvent(players);

        assertEquals(9, player1.getFood(), "Player should pay 1 food (4 characters - 3 discount)");
    }

    @Test
    void testSustenanceEvent_withoutEnoughFood_losesPoints() {
        player1.addFood(2);
        player1.data.addCharacterCount(CharacterType.Hunter, 5);

        SustenanceEvent event = new SustenanceEvent(3, eventManager, Era.SECOND, 2);
        event.resolveEvent(players);

        assertEquals(0, player1.getFood(), "Player should spend all available food");
        assertEquals(-9, player1.getPoints(), "Player should lose (5-2)*3 = 9 points");
    }

    @Test
    void testSustenanceEvent_noCharacters_paysNothing() {
        player1.addFood(10);

        SustenanceEvent event = new SustenanceEvent(3, eventManager, Era.FIRST, 2);
        event.resolveEvent(players);

        assertEquals(10, player1.getFood(), "Player with no characters should not pay food");
        assertEquals(0, player1.getPoints(), "Player should not lose points");
    }

    @Test
    void testEventCard_compareTo_byEra() {
        HuntingEvent event1 = new HuntingEvent(2, eventManager, Era.FIRST, 2);
        PaintingEvent event2 = new PaintingEvent(2, 3, 4, eventManager, Era.SECOND, 2);

        assertTrue(event1.compareTo(event2) < 0, "FIRST era should come before SECOND era");
        assertTrue(event2.compareTo(event1) > 0, "SECOND era should come after FIRST era");
    }

    @Test
    void testEventCard_compareTo_sustenanceEventLast() {
        HuntingEvent hunting = new HuntingEvent(2, eventManager, Era.FIRST, 2);
        SustenanceEvent sustenance = new SustenanceEvent(3, eventManager, Era.FIRST, 2);

        assertTrue(hunting.compareTo(sustenance) < 0, "Non-sustenance events should come before sustenance");
        assertTrue(sustenance.compareTo(hunting) > 0, "Sustenance event should come last");
    }

    @Test
    void testEventCard_eraAndMinPlayers() {
        HuntingEvent event = new HuntingEvent(2, eventManager, Era.THIRD_FINAL, 4);

        assertEquals(Era.THIRD_FINAL, event.getEra(), "Event should have correct era");
        assertEquals(4, event.getMinNumPlayers(), "Event should have correct min players");
    }

    @Test
    void testMultipleEvents_integration() {
        player1.addFood(20);
        player1.data.addCharacterCount(CharacterType.Hunter, 3);
        player1.data.addCharacterCount(CharacterType.Artist, 2);
        player1.data.addNumStar(5);

        HuntingEvent hunting = new HuntingEvent(2, eventManager, Era.FIRST, 2);
        hunting.resolveEvent(players);

        assertEquals(23, player1.getFood(), "After hunting: 20 + 3 food");
        assertEquals(6, player1.getPoints(), "After hunting: 3 hunters * 2 points");

        PaintingEvent painting = new PaintingEvent(2, 3, 5, eventManager, Era.FIRST, 2);
        painting.resolveEvent(players);

        assertEquals(12, player1.getPoints(), "After painting: 6 + (2 artists * 3 points)");
    }
}
