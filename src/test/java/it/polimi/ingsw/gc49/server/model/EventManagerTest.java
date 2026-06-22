package it.polimi.ingsw.gc49.server.model;

import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventManagerTest {

    /** Simple listener that counts how many times its effect was triggered. */
    private static class CountingListener implements BuildingEventListener {
        int triggered = 0;

        @Override
        public void onEventEffect() {
            triggered++;
        }
    }

    private EventManager manager;
    private Player peppe;
    private Player wu;

    @BeforeEach
    void setUp() {
        manager = new EventManager();
        peppe = new Player("Peppe", 0);
        wu = new Player("Wu", 1);
    }

    @Test
    @DisplayName("invokeEvent on an empty manager does nothing and does not throw")
    void invokeEmpty() {
        assertDoesNotThrow(() -> manager.invokeEvent(BuildingEvent.HUNTING_EVENT));
    }

    @Test
    @DisplayName("invokeEvent triggers all listeners registered to that event")
    void invokeEventTriggersAll() {
        CountingListener a = new CountingListener();
        CountingListener b = new CountingListener();
        manager.addEventListener(BuildingEvent.HUNTING_EVENT, new Pair<>(peppe, a));
        manager.addEventListener(BuildingEvent.HUNTING_EVENT, new Pair<>(wu, b));

        manager.invokeEvent(BuildingEvent.HUNTING_EVENT);

        assertEquals(1, a.triggered);
        assertEquals(1, b.triggered);
    }

    @Test
    @DisplayName("invokeEvent does not trigger listeners registered to a different event")
    void invokeEventIgnoresOtherEvents() {
        CountingListener hunting = new CountingListener();
        CountingListener painting = new CountingListener();
        manager.addEventListener(BuildingEvent.HUNTING_EVENT, new Pair<>(peppe, hunting));
        manager.addEventListener(BuildingEvent.PAINTING_EVENT, new Pair<>(peppe, painting));

        manager.invokeEvent(BuildingEvent.HUNTING_EVENT);

        assertEquals(1, hunting.triggered);
        assertEquals(0, painting.triggered);
    }

    @Test
    @DisplayName("the same listener registered twice is triggered twice")
    void duplicateRegistrationTriggersTwice() {
        CountingListener listener = new CountingListener();
        manager.addEventListener(BuildingEvent.GAME_END, new Pair<>(peppe, listener));
        manager.addEventListener(BuildingEvent.GAME_END, new Pair<>(peppe, listener));

        manager.invokeEvent(BuildingEvent.GAME_END);

        assertEquals(2, listener.triggered);
    }

    @Test
    @DisplayName("invokeEventByPlayer triggers only the listeners owned by that player")
    void invokeByPlayerFiltersByOwner() {
        CountingListener peppeListener = new CountingListener();
        CountingListener wuListener = new CountingListener();
        manager.addEventListener(BuildingEvent.DRAW_EVENT, new Pair<>(peppe, peppeListener));
        manager.addEventListener(BuildingEvent.DRAW_EVENT, new Pair<>(wu, wuListener));

        manager.invokeEventByPlayer(peppe, BuildingEvent.DRAW_EVENT);

        assertEquals(1, peppeListener.triggered);
        assertEquals(0, wuListener.triggered);
    }

    @Test
    @DisplayName("invokeEventByPlayer does nothing when no listener belongs to that player")
    void invokeByPlayerNoMatch() {
        CountingListener peppeListener = new CountingListener();
        manager.addEventListener(BuildingEvent.DRAW_EVENT, new Pair<>(peppe, peppeListener));

        manager.invokeEventByPlayer(wu, BuildingEvent.DRAW_EVENT);

        assertEquals(0, peppeListener.triggered);
    }

    @Test
    @DisplayName("invokeEventByPlayer does not cross over to another event")
    void invokeByPlayerEventScoped() {
        CountingListener huntingListener = new CountingListener();
        manager.addEventListener(BuildingEvent.HUNTING_EVENT, new Pair<>(peppe, huntingListener));

        manager.invokeEventByPlayer(peppe, BuildingEvent.PAINTING_EVENT);

        assertEquals(0, huntingListener.triggered);
    }

    @Test
    @DisplayName("the constructor initializes a non-null list for every BuildingEvent value")
    void constructorInitializesAllBuckets() {
        // if any bucket were missing, invokeEvent would NPE on the get(event).
        for (BuildingEvent event : BuildingEvent.values()) {
            assertDoesNotThrow(() -> manager.invokeEvent(event),
                    "invokeEvent must not throw on a freshly built manager for " + event);
        }
    }
}
