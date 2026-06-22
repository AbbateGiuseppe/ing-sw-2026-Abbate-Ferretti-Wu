package it.polimi.ingsw.gc49.server.model.CardBoard;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link Line}.
 * <p>
 * A real {@link Game} is constructed (its constructor only runs the {@code InitialSetup}
 * state once, without entering the game loop) and a separate, manually controlled
 * {@link Deck} is passed to {@link Line}'s constructor. This lets the tests deterministically
 * decide which cards land in each row.
 */
class LineTest {

    private Game game;
    private List<Player> players;
    private Deck deck;
    private Deck deckEmpty;
    @BeforeEach
    void setUp() {
        // a real Game with 2 players: the constructor runs the InitialSetup state once
        game = new Game(2, List.of("Peppe", "Wu"), "testRoom");
        players = game.getPlayers();
        deck = new Deck(game);
        deckEmpty= new Deck();
    }

    @Test
     void constructorOnEmptyDeck() {
        Line line = new Line(game, deckEmpty);
        assertTrue(line.getUpperLine().isEmpty());
        assertTrue(line.getLowerLine().isEmpty());
        assertTrue(line.getUpperBuilding().isEmpty());
        assertTrue(line.getLowerBuilding().isEmpty());
    }


    @Test
    void placedTwoPlayers() {
        Game gameTwo= new Game(2, List.of("Peppe", "Wu"), "room");
        Line line = new Line(gameTwo, new Deck(gameTwo));

        assertEquals(6, line.getUpperLine().size());
        assertEquals(3, line.getLowerLine().size());
    }

    @Test
    void placedThreePlayers() {
        Game gameThree= new Game(3, List.of("Peppe", "Wu", "Massi"), "room");
        Line line = new Line(gameThree, new Deck(gameThree));

        assertEquals(7, line.getUpperLine().size());
        assertEquals(4, line.getLowerLine().size());
    }

    @Test
    void placedFourPlayers() {
        Game gameFour= new Game(4, List.of("Peppe", "Wu", "Massi", "Luigi"), "room");
        Line line = new Line(gameFour, new Deck(gameFour));

        assertEquals(8, line.getUpperLine().size());
        assertEquals(5, line.getLowerLine().size());
    }

    @Test
    void placedFivePlayers() {
        Game gameFive= new Game(5, List.of("Peppe", "Wu", "Massi", "Luigi", "Mario"), "room");
        Line line = new Line(gameFive, new Deck(gameFive));

        assertEquals(9, line.getUpperLine().size());
        assertEquals(6, line.getLowerLine().size());
    }

    @Test
    void placedBuildingsTwoPlayers() {
        Game gameTwo = new Game(2, List.of("Peppe", "Wu"), "room");
        Line line = new Line(gameTwo, new Deck(gameTwo));

        assertEquals(1, line.getUpperBuilding().size());
        assertEquals(0, line.getLowerBuilding().size());
    }

    @Test
    void placedBuildingsThreePlayers() {
        Game gameThree = new Game(3, List.of("Peppe", "Wu", "Massi"), "room");
        Line line = new Line(gameThree, new Deck(gameThree));

        assertEquals(2, line.getUpperBuilding().size());
        assertEquals(0, line.getLowerBuilding().size());
    }

    @Test
    void placedBuildingsFourPlayers() {
        Game gameFour = new Game(4, List.of("Peppe", "Wu", "Massi", "Luigi"), "room");
        Line line = new Line(gameFour, new Deck(gameFour));

        assertEquals(2, line.getUpperBuilding().size());
        assertEquals(0, line.getLowerBuilding().size());
    }

    @Test
    void placedBuildingsFivePlayers() {
        Game gameFive = new Game(5, List.of("Peppe", "Wu", "Massi", "Luigi", "Mario"), "room");
        Line line = new Line(gameFive, new Deck(gameFive));

        assertEquals(2, line.getUpperBuilding().size());
        assertEquals(0, line.getLowerBuilding().size());
    }

    @Test
    void currentEraInitiallyFirst() {
        Line line = new Line(game, deck);
        assertEquals(Era.FIRST, line.getCurrentEra());
    }

    @Test
    void clearEraChangeResetsFlagAndNewEra() {
        Line line = new Line(game, deck);
        line.clearEraChange();
        assertFalse(line.hasEraChanged());
        assertNull(line.getNewEra());
    }


    @Test
    void drawUpperCharacterNegativeIndexReturnsNull() {
        Line line = new Line(game, deck);
        assertNull(line.drawUpperCharacter(-1, players.get(0)));
    }

    @Test
    void drawUpperCharacterOutOfRangeReturnsNull() {
        Line line = new Line(game, deck);
        assertNull(line.drawUpperCharacter(99, players.get(0)));
    }

    @Test
    void drawUpperCharacterOnEmptyLineReturnsNull() {
        Line line = new Line(game, deckEmpty);
        assertNull(line.drawUpperCharacter(0, players.get(0)));
    }

    @Test
    void drawLowerCharacterReturnsCardAndRemovesIt() {
        Line line = new Line(game, deck);
        int sizeBefore = line.getLowerLine().size();
        Card picked = line.drawLowerCharacter(0, players.get(0));

        assertNotNull(picked);
        assertEquals(sizeBefore - 1, line.getLowerLine().size());
    }

    @Test
    void drawLowerCharacterNegativeIndexReturnsNull() {
        Line line = new Line(game, deck);
        assertNull(line.drawLowerCharacter(-1, players.get(0)));
    }

    @Test
    void drawLowerCharacterOutOfRangeReturnsNull() {
        Line line = new Line(game, deck);
        assertNull(line.drawLowerCharacter(99, players.get(0)));
    }

    @Test
    void drawLowerCharacterOnEmptyLineReturnsNull() {
        Line line = new Line(game, deckEmpty);
        assertNull(line.drawLowerCharacter(0, players.get(0)));
    }

    @Test
    void drawUpperBuildingReturnsCardAndRemovesIt() {
        Line line = new Line(game, deck);
        players.get(0).setFood(100);
        int sizeBefore = line.getUpperBuilding().size();
        Card picked = line.drawUpperBuilding(0, players.get(0));

        assertNotNull(picked);
        assertEquals(sizeBefore - 1, line.getUpperBuilding().size());
    }

    @Test
    void drawUpperBuildingNegativeIndexReturnsNull() {
        Line line = new Line(game, deck);
        assertNull(line.drawUpperBuilding(-1, players.get(0)));
    }

    @Test
    void drawUpperBuildingOutOfRangeReturnsNull() {
        Line line = new Line(game, deck);
        assertNull(line.drawUpperBuilding(99, players.get(0)));
    }

    @Test
    void drawUpperBuildingOnEmptyLineReturnsNull() {
        Line line = new Line(game, deckEmpty);
        assertNull(line.drawUpperBuilding(0, players.get(0)));
    }

    @Test
    void drawLowerBuildingOnEmptyLineReturnsNull() {
        Line line = new Line(game, deck);
        assertNull(line.drawLowerBuilding(0, players.get(0)));
    }

    @Test
    void drawLowerBuildingNegativeIndexReturnsNull() {
        Line line = new Line(game, deck);
        assertNull(line.drawLowerBuilding(-1, players.get(0)));
    }

    @Test
    void drawLowerBuildingOutOfRangeReturnsNull() {
        Line line = new Line(game, deck);
        assertNull(line.drawLowerBuilding(99, players.get(0)));
    }

    @Test
    void endRoundDoesNotThrowOnFreshLine() {
        Line line = new Line(game, deck);
        assertDoesNotThrow(() -> line.endRound(2));
    }

    @Test
    void endEraDoesNotThrow() {
        Line line = new Line(game, deck);
        assertDoesNotThrow(line::endEra);
    }

    @Test
    void endGameDoesNotThrow() {
        Line line = new Line(game, deck);
        assertDoesNotThrow(line::endGame);
    }

}
