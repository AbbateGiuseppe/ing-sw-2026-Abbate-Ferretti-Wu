package it.polimi.ingsw.gc49.server.model.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.server.model.*;

import java.util.List;

public class HuntingEvent extends EventCard {
    private final int pointsPerHunter;

    public HuntingEvent( int pointsPerHunter, EventManager eventManager, Era era, int minNumPlayers ) {
        super(eventManager, era, minNumPlayers);
        this.pointsPerHunter = pointsPerHunter;
    }

    @Override
    public void resolveEvent(List<Player> players) {
        // Setup the initial amount of food and points each player gets during the event
        for(Player player : players) {
            player.setFoodToPay(-player.data.getCharacterCount(CharacterType.Hunter));
            player.setPointsToPay(-player.data.getCharacterCount(CharacterType.Hunter) * pointsPerHunter);
        }
        // Trigger the effect(effect number 8) of the building card,
        // the building card modifies foodToPay and pointsToPay of the player
        eventManager.invokeEvent(BuildingEvent.HUNTING_EVENT);
        // Finalize the change on food and points of the player
        for(Player player : players) {
            player.confirmToPay();
        }
    }

    @Override
    public String simpleToString () {
        return "CACCIA";
    }
}
