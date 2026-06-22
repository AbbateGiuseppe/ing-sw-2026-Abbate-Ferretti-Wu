package it.polimi.ingsw.gc49.server.model.Card.EventCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.EventManager;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests the behaviour defined by the abstract {@link EventCard} class itself.
 * <p>
 * A minimal in-test subclass is used to instantiate the abstract base
 * without depending on any concrete event card.
 */
class EventCardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    /** Minimal concrete subclass to exercise the abstract base. */
    private static class TestEventCard extends EventCard {
        TestEventCard(EventManager eventManager, Era era, int minNumPlayers, QueueUpdatable q) {
            super(eventManager, era, minNumPlayers, q);
        }

        @Override public void resolveEvent(List<Player> players) { /* no-op */ }
        @Override public String simpleToString() { return "TEST_EVENT"; }
        @Override public RectangleAttributedString getRectangleAttributedString() { return null; }
        @Override public ItaEngRectangleAttributedString getItaEngRectangleAttributedString() { return null; }
    }

    @Test
    @DisplayName("canGet is always false for an event card")
    void canGetIsAlwaysFalse() {
        TestEventCard card = new TestEventCard(new EventManager(), Era.FIRST, 2, null);
        assertFalse(card.canGet(peppe));
    }

    @Test
    @DisplayName("isLowerLineOnSetup is always false for an event card")
    void lowerLineOnSetupIsAlwaysFalse() {
        TestEventCard card = new TestEventCard(new EventManager(), Era.FIRST, 2, null);
        assertFalse(card.isLowerLineOnSetup());
    }

    @Test
    @DisplayName("inherited getters from Card return the constructor values")
    void inheritedGetters() {
        TestEventCard card = new TestEventCard(new EventManager(), Era.SECOND, 3, null);
        assertEquals(Era.SECOND, card.getEra());
        assertEquals(3, card.getMinNumPlayers());
    }

    @Test
    @DisplayName("compareTo orders by era when eras differ")
    void compareToByEra() {
        EventManager mgr = new EventManager();
        TestEventCard first = new TestEventCard(mgr, Era.FIRST, 2, null);
        TestEventCard second = new TestEventCard(mgr, Era.SECOND, 2, null);
        assertTrue(first.compareTo(second) < 0);
        assertTrue(second.compareTo(first) > 0);
    }
}
