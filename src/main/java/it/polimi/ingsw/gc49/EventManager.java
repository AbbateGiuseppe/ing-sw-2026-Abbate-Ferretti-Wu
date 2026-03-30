package it.polimi.ingsw.gc49;

import it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard.CharacterCard;

import java.util.*;

public class EventManager {
    private final Map<BuildingEvent, List<BuildingEventListener>> listenersByEvent;
    private final Model model;

    public EventManager(Model model) {
        this.model = model;
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

    public void invokeEvent(BuildingEvent event, Optional<CharacterCard> drawnCard) {
        for(BuildingEventListener listener : listenersByEvent.get(event)) {
            listener.onEventEffect(model,drawnCard);
        }
    }
}
