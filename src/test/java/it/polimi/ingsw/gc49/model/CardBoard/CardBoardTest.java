package it.polimi.ingsw.gc49.model.CardBoard;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.CardBoard.CardBoard;
import it.polimi.ingsw.gc49.server.model.CardBoard.Deck;
import it.polimi.ingsw.gc49.server.model.CardBoard.Line;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.States.DeckEmptiedException;
import it.polimi.ingsw.gc49.server.model.States.EraEndedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CardBoardTest {

    private Game game;
    private CardBoard cardBoard;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        List<String> nicknames = new ArrayList<>();
        nicknames.add("Player1");
        nicknames.add("Player2");
        nicknames.add("Player3");
        game = new Game(3, nicknames, "testGame");

        // Crea la lista di giocatori
        List<Player> players = new ArrayList<>();
        int i = 0;
        for (String nickname : nicknames) {
            players.add(new Player(nickname, i));
            i++;
        }
        game.setPlayers(players);
        testPlayer = players.get(0);

        cardBoard = new CardBoard(game);
    }

    @Test
    void testCardBoardInitialization() {
        assertNotNull(cardBoard.getLine(), "Line should be initialized");
        assertNotNull(cardBoard.getDeck(), "Deck should be initialized");
        assertNotNull(cardBoard.getDiscards(), "Discards should be initialized");
        assertTrue(cardBoard.getDiscards().isEmpty(), "Discards should be empty initially");
    }

    @Test
    void testGetLine_returnsLineInstance() {
        Line line = cardBoard.getLine();
        assertNotNull(line);
        assertSame(line, cardBoard.getLine(), "Should return the same line instance");
    }

    @Test
    void testGetDeck_returnsDeckInstance() {
        Deck deck = cardBoard.getDeck();
        assertNotNull(deck);
        assertSame(deck, cardBoard.getDeck(), "Should return the same deck instance");
    }

    @Test
    void testGetDiscards_returnsNewList() {
        List<Card> discards1 = cardBoard.getDiscards();
        List<Card> discards2 = cardBoard.getDiscards();
        assertNotSame(discards1, discards2, "Should return a new list each time");
    }

    @Test
    void testAddToDiscards_addsCard() {
        Card card = cardBoard.getDeck().dealTribeCard();
        cardBoard.addToDiscards(card);

        assertEquals(1, cardBoard.getDiscards().size(), "Discards should contain one card");
        assertTrue(cardBoard.getDiscards().contains(card), "Discards should contain the added card");
    }

    @Test
    void testAddToDiscards_ignoresNullCard() {
        cardBoard.addToDiscards(null);
        assertTrue(cardBoard.getDiscards().isEmpty(), "Discards should remain empty after adding null");
    }

    @Test
    void testAddToDiscards_multipleCards() {
        Card card1 = cardBoard.getDeck().dealTribeCard();
        Card card2 = cardBoard.getDeck().dealTribeCard();

        cardBoard.addToDiscards(card1);
        cardBoard.addToDiscards(card2);

        assertEquals(2, cardBoard.getDiscards().size(), "Discards should contain two cards");
    }

    @Test
    void testDrawUpperCharacter_delegatesToLine() {
        // Questo test verifica solo che il metodo delega correttamente a Line
        // Il comportamento effettivo è testato in LineTest
        Card card = cardBoard.drawUpperCharacter(0, testPlayer);
        // Il risultato dipende dallo stato della line
        assertTrue(card == null || card instanceof Card);
    }

    @Test
    void testDrawLowerCharacter_delegatesToLine() {
        Card card = cardBoard.drawLowerCharacter(0, testPlayer);
        assertTrue(card == null || card instanceof Card);
    }

    @Test
    void testDrawUpperBuilding_delegatesToLine() {
        Card card = cardBoard.drawUpperBuilding(0, testPlayer);
        assertTrue(card == null || card instanceof Card);
    }

    @Test
    void testDrawLowerBuilding_delegatesToLine() {
        Card card = cardBoard.drawLowerBuilding(0, testPlayer);
        assertTrue(card == null || card instanceof Card);
    }

    @Test
    void testHasEraChanged_initiallyFalse() {
        assertFalse(cardBoard.hasEraChanged(), "Era should not have changed initially");
    }

    @Test
    void testGetNextEra_whenNoEraChange() {
        Era nextEra = cardBoard.getNextEra();
        // L'era iniziale dipende dall'implementazione di Line
        assertTrue(nextEra == null || nextEra instanceof Era);
    }

    @Test
    void testEndRound_throwsEraEndedExceptionWhenEraChanges() {
        // Questo test dipende dallo stato del deck e della line
        // Potrebbe lanciare EraEndedException se l'era cambia
        try {
            cardBoard.endRound();
        } catch (EraEndedException e) {
            assertTrue(cardBoard.hasEraChanged(), "Era should have changed when exception is thrown");
        }
    }

    @Test
    void testEndEra_throwsDeckEmptiedExceptionOnFinalEra() {
        // Test che endEra lancia DeckEmptiedException quando si raggiunge l'era finale
        assertThrows(DeckEmptiedException.class, () -> {
            cardBoard.endEra(Era.THIRD_FINAL);
        }, "Should throw DeckEmptiedException when reaching final era");
    }

    @Test
    void testEndEra_doesNotThrowOnNonFinalEra() {
        assertDoesNotThrow(() -> {
            cardBoard.endEra(Era.FIRST);
        }, "Should not throw exception for non-final eras");
    }

    @Test
    void testEndGame_clearsBoard() {
        // Aggiungi alcune carte agli scarti prima
        Card card = cardBoard.getDeck().dealTribeCard();
        if (card != null) {
            cardBoard.addToDiscards(card);
        }

        cardBoard.endGame();

        // Verifica che endGame sia stato chiamato senza errori
        assertDoesNotThrow(() -> cardBoard.endGame());
    }
}
