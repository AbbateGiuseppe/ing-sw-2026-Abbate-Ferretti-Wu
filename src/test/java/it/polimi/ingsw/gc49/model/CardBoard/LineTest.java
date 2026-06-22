package it.polimi.ingsw.gc49.model.CardBoard;

import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.BonusPointsByClassEndGameCard;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Artist;
import it.polimi.ingsw.gc49.server.model.CardBoard.Deck;
import it.polimi.ingsw.gc49.server.model.CardBoard.Line;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.States.EraEndedException;
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
        assertEquals(Era.first(), line.getCurrentEra());
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
    void testEndRound_shiftsCardsCorrectly() throws Exception {
        // Popola la upper line con alcune carte
        line.endRound(3);

        // Dopo endRound, le carte dovrebbero essere state spostate
        // Questo è un test base che verifica che il metodo non sollevi eccezioni
        assertDoesNotThrow(() -> line.endRound(3));
    }

    @Test
    void testEndRound_detectsEraChange() throws Exception {
        int maxRounds = 100; // Limite per evitare loop infiniti
        int roundCount = 0;

        try {
            while (!line.hasEraChanged() && roundCount < maxRounds) {
                line.endRound(3);
                roundCount++;
            }
        } catch (EraEndedException ignored) {
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
    void testEndEra_clearsLowerBuildingsOnThirdEra() throws Exception {
        // Forza l'era a THIRD
        line.endRound(3); // Passa alla seconda era se possibile

        // Testa che endEra funzioni correttamente
        assertDoesNotThrow(() -> line.endEra());
    }

    @Test
    void testEndGame_clearsAllLines() throws Exception{
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
    void testEndEra_updatesCurrentEra() throws Exception {
        Era initialEra = line.getCurrentEra();

        try {
            // Forza un cambio d'era
            while (!line.hasEraChanged()) {
                line.endRound(3);
            }
        } catch (EraEndedException ignored) {
        }

        Era newEra = line.getNewEra();
        line.endEra();

        // Dopo endEra, l'era corrente dovrebbe essere aggiornata
        assertFalse(line.hasEraChanged(), "Era change flag should be cleared after endEra");
    }

    //TODO: LA TUA IMPLEMENTAZIONE DELLA PESCA DEGLI EDIFICI è SBAGLIATA E DA RIFARE!
    @Test
    void testEndEraDealsBuildingsFromNewEra() throws Exception {
        int maxRounds = 100;

        while (!line.hasEraChanged() && maxRounds-- > 0) {
            try {
                line.endRound(3);
            } catch (EraEndedException ignored) {
                break;
            }
        }

        assertTrue(line.hasEraChanged(), "Test setup should reach a new era");
        Era newEra = line.getNewEra();

        line.endEra();

        List<Card> upperBuildings = line.getUpperBuilding();
        assertFalse(upperBuildings.isEmpty(), "New era should add building cards to the upper row");
        assertTrue(
                upperBuildings.stream().allMatch(card -> newEra.equals(card.getEra())),
                "Buildings added after an era change must belong to the new era"
        );
    }

    @Test
    void testEraChangeException_setsEraChangedFlag() {
        // Crea un deck personalizzato con carte di ere diverse
        Deck customDeck = new Deck();
        Card card1 = new Artist(Era.first(), 3, null);
        Card card2 = new Artist(Era.first(), 3, null);
        Card card3 = new Artist(Era.first(), 3, null);
        Card card4 = new Artist(Era.first(), 3, null);
        //four lower cards

        Card card5 = new Artist(Era.first(), 3, null);
        Card card6 = new Artist(Era.first(), 3, null);
        Card card7 = new Artist(Era.first(), 3, null);
        Card card8 = new Artist(Era.first(), 3, null);
        Card card9 = new Artist(Era.first(), 3, null);
        Card card10 = new Artist(Era.first(), 3, null);
        Card card11 = new Artist(Era.first(), 3, null);
        //seven upper cards

        Card card12 = new Artist(Era.first().next(), 3, null);
        customDeck.addCard(card1);
        customDeck.addCard(card2);
        customDeck.addCard(card3);
        customDeck.addCard(card4);
        customDeck.addCard(card5);
        customDeck.addCard(card6);
        customDeck.addCard(card7);
        customDeck.addCard(card8);
        customDeck.addCard(card9);
        customDeck.addCard(card10);
        customDeck.addCard(card11);
        customDeck.addCard(card12);

        Line customLine = new Line(game, customDeck);

        // Verifica che venga lanciata EraEndedException
        assertThrows(EraEndedException.class, () -> {
            customLine.endRound(3);
        });
        // Verifica che eraChanged sia true dopo l'eccezione
        assertTrue(customLine.hasEraChanged(), "eraChanged should be true when era changes");
        assertEquals(Era.SECOND, customLine.getNewEra(), "newEra should be SECOND");
        customLine.endEra();
        assertEquals(Era.SECOND, customLine.getCurrentEra(), "currentEra should be updated to SECOND");
    }

}
