package it.polimi.ingsw.gc49.server.model;

import javafx.util.Pair;

import java.io.Serializable;
import java.util.*;

/**
 * Manages the subscription and invocation of building-related events in the game.
 * <p>
 * This class acts as a central dispatcher. It maps specific {@link BuildingEvent}s
 * to a list of listeners (the buildings). Each listener is associated with the {@link Player}
 * who owns it, allowing events to be triggered globally or on a per-player basis.
 */


public class EventManager implements Serializable {

    /**
     * A map associating each {@link BuildingEvent} with a list of subscribed listeners.
     * <p>
     * Each element in the list is a {@link Pair} containing:
     * <ul>
     * <li><b>Key:</b> The {@link Player} who owns the building.</li>
     * <li><b>Value:</b> The {@link BuildingEventListener} (the building itself) to be triggered.</li>
     * </ul>
     */
    private final Map<BuildingEvent, List<Pair<Player, BuildingEventListener>>> listenersByEvent;


    /**
     * Constructs a new {@code EventManager} and initializes empty listener lists
     * for every possible {@link BuildingEvent}.
     */
    public EventManager () {
        listenersByEvent = new HashMap<>();
        for(BuildingEvent event : BuildingEvent.values()) {
            listenersByEvent.put(event, new ArrayList<>());
        }
    }

    /**
     * Subscribes a new building to a specific event.
     *
     * @param event    the {@link BuildingEvent} to subscribe to
     * @param listener a {@link Pair} containing the {@link Player} owner and the {@link BuildingEventListener}
     */
    public void addEventListener ( BuildingEvent event, Pair<Player, BuildingEventListener> listener ) {
        listenersByEvent.get(event).add(listener);
    }

    /**
     * Invokes all the active buildings of the selected event type.
     * @param event the type of event being invoked;
     */
    public void invokeEvent ( BuildingEvent event ) {
        for (Pair<Player, BuildingEventListener> listener : listenersByEvent.get(event)) {
            listener.getValue().onEventEffect();
        }
    }

    /**
     * Invokes all the active buildings of the selected event type if and only if they are assigned to the selected player.
     * @param owner the player that must be assigned to the invoked building cards;
     * @param event the type of event being invoked;
     */
    public void invokeEventByPlayer ( Player owner, BuildingEvent event ) {
        for (Pair<Player, BuildingEventListener> listener : listenersByEvent.get(event)) {
            if(listener.getKey().equals(owner)) {
                listener.getValue().onEventEffect();
            }
        }
    }
}
