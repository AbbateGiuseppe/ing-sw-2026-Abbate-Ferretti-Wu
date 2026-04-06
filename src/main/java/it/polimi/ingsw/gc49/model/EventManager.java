package it.polimi.ingsw.gc49.model;

import javafx.util.Pair;

import java.util.*;

public class EventManager {
    private final Map<BuildingEvent, List<Pair<Player, BuildingEventListener>>> listenersByEvent;

    public EventManager () {
        listenersByEvent = new HashMap<>();
    }

    public void addEventListener ( BuildingEvent event, Pair<Player, BuildingEventListener> listener ) {
        if (listenersByEvent.containsKey(event)) {
            listenersByEvent.get(event).add(listener);
        } else {
            List<Pair<Player, BuildingEventListener>> ls = new ArrayList<>();
            ls.add(listener);
            listenersByEvent.put(event, ls);
        }
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
