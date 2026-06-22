package it.polimi.ingsw.gc49.server.model;

import javafx.util.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unified test class for {@link EventManager}. Targets ≥90% line and branch coverage by
 * exercising the constructor (which populates a list for every {@link BuildingEvent}),
 * subscription, global invocation, per-player invocation (both branches of the player
 * filter), and the multi-listener cases.
 */
class EventManagerTest {

    private EventManager manager;
    private Player peppe;
    private Player wu;

    /** Counting listener: increments a counter each time {@code onEventEffect} fires. */
    private static class CountingListener implements BuildingEventListener {
        final AtomicInteger calls = new AtomicInteger();
        @Override
        public void onEventEffect() { calls.incrementAndGet(); }
    }

    @BeforeEach
    void setUp() {
        manager = new EventManager();
        peppe = new Player("Peppe", 0);
        wu = new Player("Wu", 1);
    }

    // --- constructor ---

    @Test
    @DisplayName("constructor initializes an empty list for every BuildingEvent")
    void constructorPopulatesEveryEvent() {
        // invokeEvent on a fresh manager must not throw for any event, since each
        // event maps to an empty list of listeners.
        for (BuildingEvent event : BuildingEvent.values()) {
            assertDoesNotThrow(() -> manager.invokeEvent(event),
                    event + " must be present with an empty listener list");
        }
    }

    @Test
    @DisplayName("EventManager is Serializable")
    void isSerializable() {
        assertInstanceOf(java.io.Serializable.class, manager);
    }

    // --- addEventListener + invokeEvent ---

    @Test
    @DisplayName("invokeEvent triggers every listener subscribed to that event")
    void invokeEventTriggersAllListeners() {
        CountingListener a = new CountingListener();
        CountingListener b = new CountingListener();

        manager.addEventListener(BuildingEvent.HUNTING_EVENT, new Pair<>(peppe, a));
        manager.addEventListener(BuildingEvent.HUNTING_EVENT, new Pair<>(wu, b));

        manager.invokeEvent(BuildingEvent.HUNTING_EVENT);

        assertEquals(1, a.calls.get());
        assertEquals(1, b.calls.get());
    }

    @Test
    @DisplayName("invokeEvent does not trigger listeners subscribed to a different event")
    void invokeEventDoesNotTriggerOthers() {
        CountingListener hunting = new CountingListener();
        CountingListener painting = new CountingListener();

        manager.addEventListener(BuildingEvent.HUNTING_EVENT, new Pair<>(peppe, hunting));
        manager.addEventListener(BuildingEvent.PAINTING_EVENT, new Pair<>(peppe, painting));

        manager.invokeEvent(BuildingEvent.HUNTING_EVENT);

        assertEquals(1, hunting.calls.get());
        assertEquals(0, painting.calls.get(), "painting listener must NOT fire on hunting event");
    }

    @Test
    @DisplayName("invokeEvent on an event with no listeners does not throw")
    void invokeEventNoListeners() {
        assertDoesNotThrow(() -> manager.invokeEvent(BuildingEvent.GAME_END));
    }

    @Test
    @DisplayName("a listener can be subscribed to the same event multiple times and fires once per subscription")
    void multipleSubscriptionsTriggerMultipleTimes() {
        CountingListener listener = new CountingListener();
        manager.addEventListener(BuildingEvent.RITUAL_EVENT, new Pair<>(peppe, listener));
        manager.addEventListener(BuildingEvent.RITUAL_EVENT, new Pair<>(peppe, listener));
        manager.addEventListener(BuildingEvent.RITUAL_EVENT, new Pair<>(peppe, listener));

        manager.invokeEvent(BuildingEvent.RITUAL_EVENT);

        assertEquals(3, listener.calls.get(), "the same listener fires once per subscription");
    }

    // --- invokeEventByPlayer: both branches ---

    @Test
    @DisplayName("invokeEventByPlayer fires only the listeners owned by the given player (equals branch)")
    void invokeEventByPlayerFiresOnlyOwned() {
        CountingListener peppeListener = new CountingListener();
        CountingListener wuListener = new CountingListener();

        manager.addEventListener(BuildingEvent.SUSTENANCE_EVENT, new Pair<>(peppe, peppeListener));
        manager.addEventListener(BuildingEvent.SUSTENANCE_EVENT, new Pair<>(wu, wuListener));

        manager.invokeEventByPlayer(peppe, BuildingEvent.SUSTENANCE_EVENT);

        assertEquals(1, peppeListener.calls.get(), "peppe's listener must fire");
        assertEquals(0, wuListener.calls.get(), "wu's listener must NOT fire");
    }

    @Test
    @DisplayName("invokeEventByPlayer with a player that owns nothing is a no-op (false branch only)")
    void invokeEventByPlayerNoOwnership() {
        CountingListener wuListener = new CountingListener();
        manager.addEventListener(BuildingEvent.DRAW_EVENT, new Pair<>(wu, wuListener));

        // peppe owns nothing on this event
        manager.invokeEventByPlayer(peppe, BuildingEvent.DRAW_EVENT);

        assertEquals(0, wuListener.calls.get());
    }

    @Test
    @DisplayName("invokeEventByPlayer on an event with no listeners does not throw")
    void invokeEventByPlayerNoListeners() {
        assertDoesNotThrow(() -> manager.invokeEventByPlayer(peppe, BuildingEvent.TURN_END));
    }

    @Test
    @DisplayName("invokeEventByPlayer with multiple listeners owned by the same player fires all of them")
    void invokeEventByPlayerFiresAllOwned() {
        CountingListener a = new CountingListener();
        CountingListener b = new CountingListener();
        manager.addEventListener(BuildingEvent.ROUND_END, new Pair<>(peppe, a));
        manager.addEventListener(BuildingEvent.ROUND_END, new Pair<>(peppe, b));

        manager.invokeEventByPlayer(peppe, BuildingEvent.ROUND_END);

        assertEquals(1, a.calls.get());
        assertEquals(1, b.calls.get());
    }

    @Test
    @DisplayName("invokeEventByPlayer interleaves: owned + not-owned + owned exercises both branches")
    void invokeEventByPlayerInterleaved() {
        CountingListener a = new CountingListener();
        CountingListener b = new CountingListener();
        CountingListener c = new CountingListener();

        manager.addEventListener(BuildingEvent.HUNTING_EVENT, new Pair<>(peppe, a));
        manager.addEventListener(BuildingEvent.HUNTING_EVENT, new Pair<>(wu, b));
        manager.addEventListener(BuildingEvent.HUNTING_EVENT, new Pair<>(peppe, c));

        manager.invokeEventByPlayer(peppe, BuildingEvent.HUNTING_EVENT);

        assertEquals(1, a.calls.get());
        assertEquals(0, b.calls.get());
        assertEquals(1, c.calls.get());
    }
}
