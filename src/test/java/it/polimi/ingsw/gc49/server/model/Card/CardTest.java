package it.polimi.ingsw.gc49.server.model.Card;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.DataBank;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the behaviour defined by the abstract {@link Card} class itself.
 * <p>
 * A minimal in-test subclass ({@link TestCard}) is used to instantiate the abstract base.
 * The default implementations of {@code updateDataBank} and {@code onDraw} are exercised
 * here so the base contract is covered independently of any concrete card.
 */
class CardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    /** Minimal concrete subclass that does not override the default Card hooks. */
    private static class TestCard extends Card {
        TestCard(Era era, int minNumPlayers, QueueUpdatable queueUpdater) {
            super(era, minNumPlayers, queueUpdater);
        }

        @Override
        public boolean canGet(Player player) {
            return true;
        }

        @Override
        public String simpleToString() {
            return "TEST_CARD";
        }

        @Override
        public boolean isLowerLineOnSetup() {
            return false;
        }

        @Override
        public RectangleAttributedString getRectangleAttributedString() {
            return null;
        }

        @Override
        public ItaEngRectangleAttributedString getItaEngRectangleAttributedString() {
            return null;
        }
    }

    @Test
    @DisplayName("getEra and getMinNumPlayers return the constructor values")
    void gettersFromConstructor() {
        TestCard card = new TestCard(Era.THIRD, 4, null);
        assertEquals(Era.THIRD, card.getEra());
        assertEquals(4, card.getMinNumPlayers());
    }

    @Test
    @DisplayName("default updateDataBank does not modify the player's databank")
    void defaultUpdateDataBankIsNoOp() {
        TestCard card = new TestCard(Era.FIRST, 2, null);
        DataBank before = peppe.data;
        card.updateDataBank(peppe.data);
        assertEquals(0, peppe.data.getNumBuildingPoints());
        assertEquals(0, peppe.data.getNumStars());
        assertEquals(0, peppe.data.getNumBuildingDiscount());
        assertEquals(0, peppe.data.getNumCharacters());
        assertSame(before, peppe.data);
    }

    @Test
    @DisplayName("default onDraw does not modify the player")
    void defaultOnDrawIsNoOp() {
        TestCard card = new TestCard(Era.FIRST, 2, null);
        peppe.setFood(5);
        peppe.setPoints(3);
        card.onDraw(peppe);
        assertEquals(5, peppe.getFood());
        assertEquals(3, peppe.getPoints());
    }

    @Test
    @DisplayName("simpleToString delegates to the subclass implementation")
    void simpleToStringDelegates() {
        TestCard card = new TestCard(Era.FIRST, 2, null);
        assertEquals("TEST_CARD", card.simpleToString());
    }
}
