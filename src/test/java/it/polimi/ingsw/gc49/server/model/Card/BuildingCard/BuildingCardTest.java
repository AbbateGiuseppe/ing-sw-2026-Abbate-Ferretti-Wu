package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.EventManager;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the behaviour defined by the abstract {@link BuildingCard} class itself.
 * <p>
 * A minimal in-test subclass ({@link TestBuildingCard}) is used to instantiate the
 * abstract base without depending on any concrete game card. Its {@code onEventEffect}
 * just flips a flag, so the listener wiring done by {@code addBuildingToManager} can
 * be verified through {@link EventManager#invokeEvent(BuildingEvent)}.
 */
class BuildingCardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    /** Minimal concrete subclass used to exercise the abstract base. */
    private static class TestBuildingCard extends BuildingCard {
        boolean effectTriggered = false;

        TestBuildingCard(BuildingEvent buildingEvent, int pointsEndgame, int foodPrice,
                         Era era, int minNumPlayers, QueueUpdatable queueUpdater) {
            super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
        }

        @Override
        public void onEventEffect() {
            effectTriggered = true;
        }

        @Override
        public String simpleToString() {
            return "TEST_BUILDING";
        }

        @Override
        public RectangleAttributedString getRectangleAttributedString() {
            return null; // not exercised here
        }

        @Override
        public ItaEngRectangleAttributedString getItaEngRectangleAttributedString() {
            return null; // not exercised here
        }
    }

    private TestBuildingCard newCard(int pointsEndgame, int foodPrice) {
        return new TestBuildingCard(BuildingEvent.HUNTING_EVENT, pointsEndgame, foodPrice,
                Era.FIRST, 2, null);
    }

    // --- constructor & simple getters ---

    @Test
    void gettersAndInitialState() {
        TestBuildingCard card = new TestBuildingCard(
                BuildingEvent.GAME_END, 4, 7, Era.SECOND, 3, null);
        assertEquals(7, card.getFoodPrice());
        assertEquals(Era.SECOND, card.getEra());
        assertEquals(3, card.getMinNumPlayers());
        assertNull(card.getOwner());
    }

    @Test
    void isLowerLineOnSetup() {
        assertTrue(newCard(2, 5).isLowerLineOnSetup());
    }

    // --- canGet ---

    @Test
    void canGetAtExactPrice() {
        TestBuildingCard card = newCard(2, 5);
        peppe.setFood(5);
        assertTrue(card.canGet(peppe));
    }

    @Test
    void canGetOverPrice() {
        TestBuildingCard card = newCard(2, 5);
        peppe.setFood(10);
        assertTrue(card.canGet(peppe));
    }

    @Test
    void cannotGetUnderPrice() {
        TestBuildingCard card = newCard(2, 5);
        peppe.setFood(4);
        assertFalse(card.canGet(peppe));
    }

    @Test
    void canGetWithDiscount() {
        TestBuildingCard card = newCard(2, 5);
        peppe.setFood(3);
        peppe.data.addNumBuildingDiscount(2); // effective price = 3
        assertTrue(card.canGet(peppe));
    }

    @Test
    void canGetDiscountCoversPrice() {
        TestBuildingCard card = newCard(2, 5);
        peppe.data.addNumBuildingDiscount(5);
        assertTrue(card.canGet(peppe));
    }

    // --- updateDataBank ---

    @Test
    void updateDataBankAddsEndgamePoints() {
        TestBuildingCard card = newCard(6, 5);
        card.updateDataBank(peppe.data);
        assertEquals(6, peppe.data.getNumBuildingPoints());
    }

    @Test
    void updateDataBankAccumulates() {
        newCard(3, 5).updateDataBank(peppe.data);
        newCard(4, 5).updateDataBank(peppe.data);
        assertEquals(7, peppe.data.getNumBuildingPoints());
    }

    // --- onDraw ---

    @Test
    void onDrawChargesFullPrice() {
        TestBuildingCard card = newCard(2, 5);
        peppe.setFood(10);
        card.onDraw(peppe);
        assertEquals(5, peppe.getFood());
        assertSame(peppe, card.getOwner());
    }

    @Test
    void onDrawChargesDiscountedPrice() {
        TestBuildingCard card = newCard(2, 5);
        peppe.setFood(10);
        peppe.data.addNumBuildingDiscount(3); // effective price = 2
        card.onDraw(peppe);
        assertEquals(8, peppe.getFood());
    }

    @Test
    void onDrawDiscountCappedAtZero() {
        TestBuildingCard card = newCard(2, 4);
        peppe.setFood(10);
        peppe.data.addNumBuildingDiscount(10); // discount > price
        card.onDraw(peppe);
        assertEquals(10, peppe.getFood());
        assertSame(peppe, card.getOwner());
    }

    // --- addBuildingToManager + listener wiring ---

    @Test
    void addBuildingToManagerWiresListener() {
        TestBuildingCard card = newCard(0, 5);
        peppe.setFood(10);
        card.onDraw(peppe);

        EventManager manager = new EventManager();
        card.addBuildingToManager(peppe, manager);

        manager.invokeEvent(BuildingEvent.HUNTING_EVENT);
        assertTrue(card.effectTriggered, "the registered listener should be invoked");
    }

    @Test
    void unrelatedEventDoesNotTrigger() {
        TestBuildingCard card = newCard(0, 5); // listens to HUNTING_EVENT
        peppe.setFood(10);
        card.onDraw(peppe);

        EventManager manager = new EventManager();
        card.addBuildingToManager(peppe, manager);

        manager.invokeEvent(BuildingEvent.PAINTING_EVENT);
        assertFalse(card.effectTriggered);
    }
}
