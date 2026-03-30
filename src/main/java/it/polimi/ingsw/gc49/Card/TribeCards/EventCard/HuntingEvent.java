package it.polimi.ingsw.gc49.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.Model;
import it.polimi.ingsw.gc49.Player;

import java.util.List;

public class HuntingEvent extends EventCard {
    private final int pointsPerHunter;

    public HuntingEvent(int pointsPerHunter) {
        this.pointsPerHunter = pointsPerHunter;
    }

    @Override
    public void resolveEvent(Model model) {
        // Setup the initial amount of food and points each player gets during the event
        for(Player player : model.getPlayers()) {
            player.setFoodToPay(-player.data.getCharacterCount(CharacterType.Hunter));
            player.setPointsToPay(-player.data.getCharacterCount(CharacterType.Hunter) * pointsPerHunter);
        }
        // Trigger the effect(effect number 8) of the building card,
        // the building card modifies foodToPay and pointsToPay of the player
        eventManager.invokeEvent(BuildingEvent.HUNTING_EVENT,null);
        // Finalize the change on food and points of the player
        for(Player player : model.getPlayers()) {
            player.confirmToPay();
        }
    }
}
