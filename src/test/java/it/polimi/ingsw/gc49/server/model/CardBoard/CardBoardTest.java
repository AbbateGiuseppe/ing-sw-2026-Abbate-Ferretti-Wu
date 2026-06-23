package it.polimi.ingsw.gc49.server.model.CardBoard;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Hunter;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.States.DeckEmptiedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for {@link CardBoard}.
 * <p>
 * A real {@link Game} is constructed (its constructor only runs the {@code InitialSetup}
 * state once, without starting the game loop). The CardBoard built by the game is then
 * exercised through its public API. Note: the {@code Deck} of the game is built from the
 * bundled JSON resources, so this is closer to an integration test for the board layout.
 */
class CardBoardTest {

    private Game game;
    private CardBoard board;

    @BeforeEach
    void setUp() {
        game = new Game(2, List.of("Peppe", "Wu"), "testRoom");
        board = game.getCardBoard();
    }

    @Test
    void componentsAreInitialized() {
        assertNotNull(board.getLine());
        assertNotNull(board.getDeck());
        assertNotNull(board.getDiscards());
    }

    @Test
    void initialEra() {
        assertEquals(Era.first(), board.getCurrentEra());
    }

    @Test
    void noEraChangeAtStart() {
        assertFalse(board.hasEraChanged());
    }

    @Test
    void discardsCopyIsIndependent() {
        List<Card> external = board.getDiscards();
        external.add(new Hunter(false, Era.FIRST, 2, null));
        assertEquals(0, board.getDiscards().size(), "the internal pile must remain empty");
    }

    @Test
    void addToDiscards() {
        board.addToDiscards(new Hunter(false, Era.FIRST, 2, null));
        assertEquals(1, board.getDiscards().size());

        board.addToDiscards(null);
        assertEquals(1, board.getDiscards().size(), "null must not be added");
    }

    @Test
    void drawUpperCharacterNegativeIndex() {
        assertNull(board.drawUpperCharacter(-1, game.getPlayers().get(0)));
    }

    @Test
    void drawLowerCharacterOutOfRange() {
        assertNull(board.drawLowerCharacter(9999, game.getPlayers().get(0)));
    }

    @Test
    void drawUpperBuildingNegativeIndex() {
        assertNull(board.drawUpperBuilding(-1, game.getPlayers().get(0)));
    }

    @Test
    void drawLowerBuildingNegativeIndex() {
        assertNull(board.drawLowerBuilding(-1, game.getPlayers().get(0)));
    }

    @Test
    void endEraNonFinal() {
        // after construction the current era is FIRST; moving to SECOND is non-final
        assertDoesNotThrow(() -> board.endEra(Era.SECOND));
    }

    @Test
    void endEraFinalThrows() {
        assertThrows(DeckEmptiedException.class, () -> board.endEra(Era.THIRD_FINAL));
    }
}
