package it.polimi.ingsw.gc49.model;

import java.util.*;

public class EventManager {
    private final Map<BuildingEvent, List<BuildingEventListener>> listenersByEvent;
    private final Game game;

    public EventManager( Game game ) {
        this.game = game;
        listenersByEvent = new HashMap<>();
    }

    public void addEventListener(BuildingEvent event, BuildingEventListener listener) {
        if (listenersByEvent.containsKey(event)) {
            listenersByEvent.get(event).add(listener);
        }
        else {
            List<BuildingEventListener> ls = new ArrayList<>();
            ls.add(listener);
            listenersByEvent.put(event,ls);
        }
    }

    public void invokeEvent(BuildingEvent event) {
        for(BuildingEventListener listener : listenersByEvent.get(event)) {
            listener.onEventEffect();
        }
    }
}
