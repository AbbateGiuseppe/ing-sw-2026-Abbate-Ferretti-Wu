package it.polimi.ingsw.gc49.server.model.CardBoard;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Artist;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Builder;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Hunter;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Shaman;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Game;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static it.polimi.ingsw.gc49.server.model.Era.FIRST;
import static it.polimi.ingsw.gc49.server.model.Era.SECOND;
import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    private Game game;

    private Deck deck;

    @BeforeEach
    void setUp() {
        game = new Game(2, List.of("Peppe", "Massi"), "room");
        deck = new Deck(game);
    }

    @Test
    void setUpNull(){
        Deck deck2;
        deck2 = new Deck();
        assertNull(deck2.getGameEventManager());
        assertNull(deck2.getQueueUpdater());
    }

    @Test
    void getCard(){
        Card card=new Shaman(2, FIRST, 2, deck.getQueueUpdater());
        deck.addCard(card);
        assertEquals(card, deck.getTribeDeck().getLast());
    }

    @Test
    void cardsFinished(){
        for(int i=0; i<63; i++){
            deck.dealTribeCard();
        }
        assertTrue(deck.getTribeDeck().isEmpty());
    }

    @Test
    void buildingCardsFinishedTwoPlayers(){
        Deck deck2 = new Deck(new Game(2, List.of("Peppe", "Massi"), "room"));
        int countFirst=0;
        int countSecond=0;
        int countThird=0;
        Card cardTaken;
        for(int i=0; i<7; i++){
            cardTaken=deck2.dealBuildingCard();
            if(cardTaken!=null) {
                if (cardTaken.getEra().equals(FIRST)) {
                    countFirst++;
                } else if (cardTaken.getEra().equals(SECOND)) {
                    countSecond++;
                } else {
                    countThird++;
                }
            }

        }
        assertEquals(1, countFirst);
        assertEquals(2, countSecond);
        assertEquals(3, countThird);
        assertTrue(deck2.getBuildingDeck().isEmpty());
    }

    @Test
    void buildingCardsFinishedThreePlayers(){
        Deck deck2 = new Deck(new Game(3, List.of("Peppe", "Massi", "Luigi"), "room"));
        int countFirst=0;
        int countSecond=0;
        int countThird=0;
        Card cardTaken;
        for(int i=0; i<9; i++){
            cardTaken=deck2.dealBuildingCard();
            if(cardTaken!=null) {
                if (cardTaken.getEra().equals(FIRST)) {
                    countFirst++;
                } else if (cardTaken.getEra().equals(SECOND)) {
                    countSecond++;
                } else {
                    countThird++;
                }
            }

        }
        assertEquals(2, countFirst);
        assertEquals(2, countSecond);
        assertEquals(4, countThird);
        assertTrue(deck2.getBuildingDeck().isEmpty());
    }


    @Test
    void buildingCardsFinishedFourPlayers(){
        Deck deck2 = new Deck(new Game(4, List.of("Peppe", "Massi", "Wu", "Luigi"), "room"));
        int countFirst=0;
        int countSecond=0;
        int countThird=0;
        Card cardTaken;
        for(int i=0; i<9; i++){
            cardTaken=deck2.dealBuildingCard();
            if(cardTaken!=null) {
                if (cardTaken.getEra().equals(FIRST)) {
                    countFirst++;
                } else if (cardTaken.getEra().equals(SECOND)) {
                    countSecond++;
                } else {
                    countThird++;
                }
            }

        }
        assertEquals(2, countFirst);
        assertEquals(3, countSecond);
        assertEquals(4, countThird);
        assertTrue(deck2.getBuildingDeck().isEmpty());
    }



    @Test
    void buildingCardsFinishedFivePlayers(){
        Deck deck2 = new Deck(new Game(5, List.of("Peppe", "Massi", "Wu", "Luigi", "Mario"), "room"));
        int countFirst=0;
        int countSecond=0;
        int countThird=0;
        Card cardTaken;
        for(int i=0; i<11; i++){
            cardTaken=deck2.dealBuildingCard();
            if(cardTaken!=null) {
                if (cardTaken.getEra().equals(FIRST)) {
                    countFirst++;
                } else if (cardTaken.getEra().equals(SECOND)) {
                    countSecond++;
                } else {
                    countThird++;
                }
            }

        }
        assertEquals(2, countFirst);
        assertEquals(3, countSecond);
        assertEquals(5, countThird);
        assertTrue(deck2.getBuildingDeck().isEmpty());
    }

}
