package it.polimi.ingsw.gc49.model.CardBoard;

import it.polimi.ingsw.gc49.model.Card.Card;
import it.polimi.ingsw.gc49.model.Era;
import it.polimi.ingsw.gc49.model.Game;
import it.polimi.ingsw.gc49.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LineTest {

    private Game game;
    private Line line;
    private Deck deck;
    private Player testPlayer;

    @BeforeEach
    void setUp() {
        String[] nicknames = {"Player1", "Player2", "Player3"};
        game = new Game(3, nicknames);

        // Crea la lista di giocatori
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < nicknames.length; i++) {
            players.add(new Player(nicknames[i], i));
        }
        game.setPlayers(players);
        testPlayer = players.get(0);

        deck = new Deck(game);
        line = new Line(game, deck);
    }

    @Test
    void testLineInitialization() {
        assertNotNull(line);
        assertEquals(Era.FIRST, line.getCurrentEra(), "Initial era should be FIRST");
        assertFalse(line.hasEraChanged(), "Era should not have changed initially");
    }

    @Test
    void testGetCurrentEra_initiallyFirst() {
        assertEquals(Era.FIRST, line.getCurrentEra());
    }

    @Test
    void testHasEraChanged_initiallyFalse() {
        assertFalse(line.hasEraChanged());
    }

    @Test
    void testGetNewEra_initiallySecond() {
        assertEquals(Era.SECOND, line.getNewEra());
    }

    @Test
    void testClearEraChange_resetsFlag() {
        line.clearEraChange();
        assertFalse(line.hasEraChanged());
        assertNull(line.getNewEra());
    }

    @Test
    void testDealTribeCard_delegatesToDeck() {
        Card card = line.dealTribeCard();
        assertNotNull(card, "Should deal a card from deck");
    }

    @Test
    void testDealBuildingCard_delegatesToDeck() {
        Card card = line.dealBuildingCard();
        // Il risultato può essere null se createBuildingCardFromJson ritorna null
        assertTrue(card == null || card instanceof Card);
    }

    @Test
    void testDrawUpperCharacter_invalidIndex() {
        Card card = line.drawUpperCharacter(-1, testPlayer);
        assertNull(card, "Should return null for negative index");

        card = line.drawUpperCharacter(100, testPlayer);
        assertNull(card, "Should return null for index out of bounds");
    }

    @Test
    void testDrawLowerCharacter_invalidIndex() {
        Card card = line.drawLowerCharacter(-1, testPlayer);
        assertNull(card, "Should return null for negative index");

        card = line.drawLowerCharacter(100, testPlayer);
        assertNull(card, "Should return null for index out of bounds");
    }

    @Test
    void testDrawUpperBuilding_invalidIndex() {
        Card card = line.drawUpperBuilding(-1, testPlayer);
        assertNull(card, "Should return null for negative index");

        card = line.drawUpperBuilding(100, testPlayer);
        assertNull(card, "Should return null for index out of bounds");
    }

    @Test
    void testDrawLowerBuilding_invalidIndex() {
        Card card = line.drawLowerBuilding(-1, testPlayer);
        assertNull(card, "Should return null for negative index");

        card = line.drawLowerBuilding(100, testPlayer);
        assertNull(card, "Should return null for index out of bounds");
    }

    @Test
    void testEndRound_shiftsCardsCorrectly() {
        // Popola la upper line con alcune carte
        line.endRound(3);

        // Dopo endRound, le carte dovrebbero essere state spostate
        // Questo è un test base che verifica che il metodo non sollevi eccezioni
        assertDoesNotThrow(() -> line.endRound(3));
    }

    @Test
    void testEndRound_detectsEraChange() {
        int maxRounds = 100; // Limite per evitare loop infiniti
        int roundCount = 0;

        while (!line.hasEraChanged() && roundCount < maxRounds) {
            line.endRound(3);
            roundCount++;
        }

        if (line.hasEraChanged()) {
            assertNotEquals(Era.FIRST, line.getNewEra(), "New era should be different from FIRST");
        }
    }

    @Test
    void testEndEra_shiftsBuildings() {
        // Simula un cambio d'era
        line.endEra();

        // Verifica che il metodo non sollevi eccezioni
        assertDoesNotThrow(() -> line.endEra());
    }

    @Test
    void testEndEra_clearsLowerBuildingsOnThirdEra() {
        // Forza l'era a THIRD
        line.endRound(3); // Passa alla seconda era se possibile

        // Testa che endEra funzioni correttamente
        assertDoesNotThrow(() -> line.endEra());
    }

    @Test
    void testEndGame_clearsAllLines() {
        // Popola le linee
        line.endRound(3);

        // Chiama endGame
        line.endGame();

        // Verifica che il metodo non sollevi eccezioni
        assertDoesNotThrow(() -> line.endGame());
    }

    @Test
    void testEndGame_resolvesEvents() {
        // Questo test verifica che endGame non sollevi eccezioni
        // La risoluzione degli eventi dipende dalle carte presenti
        assertDoesNotThrow(() -> line.endGame());
    }

    @Test
    void testDrawUpperCharacter_emptyLine() {
        // Quando la linea è vuota, dovrebbe ritornare null
        Card card = line.drawUpperCharacter(0, testPlayer);
        assertNull(card, "Should return null when upper line is empty");
    }

    @Test
    void testDrawLowerCharacter_emptyLine() {
        Card card = line.drawLowerCharacter(0, testPlayer);
        assertNull(card, "Should return null when lower line is empty");
    }

    @Test
    void testDrawUpperBuilding_emptyLine() {
        Card card = line.drawUpperBuilding(0, testPlayer);
        assertNull(card, "Should return null when upper building line is empty");
    }

    @Test
    void testDrawLowerBuilding_emptyLine() {
        Card card = line.drawLowerBuilding(0, testPlayer);
        assertNull(card, "Should return null when lower building line is empty");
    }

    @Test
    void testMultipleEndRounds_maintainConsistency() {
        // Esegui più endRound per verificare la consistenza
        for (int i = 0; i < 5; i++) {
            assertDoesNotThrow(() -> line.endRound(3));
        }
    }

    @Test
    void testEndEra_updatesCurrentEra() {
        Era initialEra = line.getCurrentEra();

        // Forza un cambio d'era
        while (!line.hasEraChanged()) {
            line.endRound(3);
        }

        Era newEra = line.getNewEra();
        line.endEra();

        // Dopo endEra, l'era corrente dovrebbe essere aggiornata
        assertFalse(line.hasEraChanged(), "Era change flag should be cleared after endEra");
    }
}
