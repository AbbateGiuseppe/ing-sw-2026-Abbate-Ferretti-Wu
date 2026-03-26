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
            player.setFoodToPay(-player.data.getCharacterCount(CharacterType.Hunter));
            player.setPointsToPay(-player.data.getCharacterCount(CharacterType.Hunter) * pointsPerHunter);
            eventManager.invokeEvent(BuildingEvent.HUNTING_EVENT,player.getTotem());
            player.confirmToPay();
        }
    }
}
