package it.polimi.ingsw.gc49;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventManager {
    private final Map<BuildingEvent, List<BuildingEventListener>> listenersByEvent;

    public EventManager() {
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

    public void invokeEvent(BuildingEvent event, Totem totem) {
        for(BuildingEventListener listener : listenersByEvent.get(event)) {
            listener.onEventEffect(totem);
        }
    }

}
