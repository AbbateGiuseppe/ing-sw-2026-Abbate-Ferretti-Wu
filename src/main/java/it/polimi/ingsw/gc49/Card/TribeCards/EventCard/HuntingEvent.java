package it.polimi.ingsw.gc49.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.Player;

import java.util.List;

public class HuntingEvent extends EventCard {
    private final int pointsPerHunter;

    public HuntingEvent(int pointsPerHunter) {
        this.pointsPerHunter = pointsPerHunter;
    }

    @Override
    public void resolveEvent(List<Player> players) {
        for(Player player : players) {
            // Setup the initial amount of food and points each player gets during the event
            player.setFoodToPay(-player.data.getCharacterCount(CharacterType.Hunter));
            player.setPointsToPay(-player.data.getCharacterCount(CharacterType.Hunter) * pointsPerHunter);
            // Trigger the effect(effect number 8) of the building card if the player has any,
            // the building card modifies foodToPay and pointsToPay of the player
            eventManager.invokeEvent(BuildingEvent.HUNTING_EVENT,player.getTotem());
            // Finalize the change on food and points of the player
            player.confirmToPay();
        }
    }
}
