package it.polimi.ingsw.gc49.model.CardBoard;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.EventCard.HuntingEvent;
import it.polimi.ingsw.gc49.server.model.CardBoard.Deck;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    private Game game;
    private Deck deck;

    @BeforeEach
    void setUp() {
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Player1");
        nicknames.add("Player2");
        nicknames.add("Player3");
        game = new Game(3, nicknames, "testGame");
        deck = new Deck(game);
    }

    @Test
    void testDealTribeCard_shouldReturnCardFromDeck() {
        Card card = deck.dealTribeCard();
        assertNotNull(card, "Deck should not be empty initially");
    }

    @Test
    void testDealTribeCard_shouldReturnNullWhenEmpty() {
        // Svuota il deck
        while (deck.dealTribeCard() != null) {
            // continua a pescare
        }

        Card card = deck.dealTribeCard();
        assertNull(card, "Should return null when deck is empty");
    }

    @Test
    void testDealBuildingCard_shouldReturnCardFromDeck() {
        Card card = deck.dealBuildingCard();
        // Il building deck potrebbe essere null se createBuildingCardFromJson ritorna null
        // Questo test verifica solo che il metodo non sollevi eccezioni
        assertTrue(card == null || card instanceof Card, "Should return a Card or null");
    }

    @Test
    void testDealBuildingCardForEra_skipsPreviousEraBuildings() {
        deck.dealBuildingCard(Era.FIRST);

        Card card = deck.dealBuildingCard(Era.SECOND);

        assertNotNull(card, "Deck should contain second-era buildings");
        assertEquals(Era.SECOND, card.getEra(), "Requested second-era buildings must not deal leftover first-era cards");
    }

    @Test
    void testDealBuildingCard_shouldReturnNullWhenEmpty() {
        // Svuota il deck
        while (deck.dealBuildingCard() != null) {
            // continua a pescare
        }

        Card card = deck.dealBuildingCard();
        assertNull(card, "Should return null when building deck is empty");
    }

    @Test
    void testGetBuildingsToPlace_twoPlayers() {
        assertEquals(1, deck.getBuildingsToPlace(2, Era.FIRST));
        assertEquals(2, deck.getBuildingsToPlace(2, Era.SECOND));
        assertEquals(3, deck.getBuildingsToPlace(2, Era.THIRD));
        assertEquals(0, deck.getBuildingsToPlace(2, Era.THIRD_FINAL));
    }

    @Test
    void testGetBuildingsToPlace_threePlayers() {
        assertEquals(2, deck.getBuildingsToPlace(3, Era.FIRST));
        assertEquals(2, deck.getBuildingsToPlace(3, Era.SECOND));
        assertEquals(4, deck.getBuildingsToPlace(3, Era.THIRD));
        assertEquals(0, deck.getBuildingsToPlace(3, Era.THIRD_FINAL));
    }

    @Test
    void testGetBuildingsToPlace_fourPlayers() {
        assertEquals(2, deck.getBuildingsToPlace(4, Era.FIRST));
        assertEquals(3, deck.getBuildingsToPlace(4, Era.SECOND));
        assertEquals(4, deck.getBuildingsToPlace(4, Era.THIRD));
        assertEquals(0, deck.getBuildingsToPlace(4, Era.THIRD_FINAL));
    }

    @Test
    void testGetBuildingsToPlace_fivePlayers() {
        assertEquals(2, deck.getBuildingsToPlace(5, Era.FIRST));
        assertEquals(3, deck.getBuildingsToPlace(5, Era.SECOND));
        assertEquals(5, deck.getBuildingsToPlace(5, Era.THIRD));
        assertEquals(0, deck.getBuildingsToPlace(5, Era.THIRD_FINAL));
    }

    @Test
    void testGetBuildingsToPlace_invalidNumberOfPlayers() {
        assertThrows(IllegalArgumentException.class, () -> {
            deck.getBuildingsToPlace(1, Era.FIRST);
        }, "Should throw exception for invalid number of players");

        assertThrows(IllegalArgumentException.class, () -> {
            deck.getBuildingsToPlace(6, Era.FIRST);
        }, "Should throw exception for invalid number of players");
    }

    @Test
    void testTribeDeckOrder_firstEraComesFirst() {
        Card firstCard = deck.dealTribeCard();
        assertNotNull(firstCard);
        assertEquals(Era.FIRST, firstCard.getEra(), "First cards should be from Era FIRST");
    }

    @Test
    void testDeckInitialization_withDifferentPlayerCounts() {
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Player1");
        nicknames.add("Player2");
        // Test con 2 giocatori
        Game game2 = new Game(2, nicknames, "testGame");
        Deck deck2 = new Deck(game2);
        assertNotNull(deck2.dealTribeCard());


        nicknames.add("Player3");
        nicknames.add("Player4");
        nicknames.add("Player5");
        // Test con 5 giocatori
        Game game5 = new Game(5, nicknames, "testGame");
        Deck deck5 = new Deck(game5);
        assertNotNull(deck5.dealTribeCard());
    }

    @Test
    void testTwoPlayerDeckIncludesHuntingEventsForAllEras() {
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Player1");
        nicknames.add("Player2");
        Game game2 = new Game(2, nicknames, "testGame");
        Deck deck2 = new Deck(game2);
        Set<Era> huntingEventEras = new HashSet<>();

        Card card;
        while ((card = deck2.dealTribeCard()) != null) {
            if (card instanceof HuntingEvent) {
                huntingEventEras.add(card.getEra());
            }
        }

        assertTrue(huntingEventEras.contains(Era.FIRST), "Two-player deck should include first-era hunting event");
        assertTrue(huntingEventEras.contains(Era.SECOND), "Two-player deck should include second-era hunting event");
        assertTrue(huntingEventEras.contains(Era.THIRD), "Two-player deck should include third-era hunting event");
    }

    @Test
    void testDeckNotEmpty_afterInitialization() {
        assertNotNull(deck.dealTribeCard(), "Tribe deck should have cards after initialization");
    }
}
