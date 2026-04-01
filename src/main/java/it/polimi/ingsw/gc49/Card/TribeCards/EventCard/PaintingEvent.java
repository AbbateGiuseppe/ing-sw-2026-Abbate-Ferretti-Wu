package it.polimi.ingsw.gc49.Card.TribeCards.EventCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.Player;

import java.util.List;

public class PaintingEvent extends EventCard {
    // threshold is the minimum number of the Artist cards in order to get plusPoints,otherwise the player gets minusPooints
    private final int threshold;
    private final int plusPoints;
    private final int minusPoints;

    public PaintingEvent(int threshold, int plusPoints, int minusPoints) {
        this.threshold = threshold;
        this.plusPoints = plusPoints;
        this.minusPoints = minusPoints;
    }

    @Override
    public void resolveEvent(List<Player> players) {
        for(Player player : players) {
            player.setFoodToPay(0);
            if (player.data.getCharacterCount(CharacterType.Artist) < threshold) {
                player.setPointsToPay(minusPoints);
            } else {
                player.setPointsToPay(-plusPoints * player.data.getCharacterCount(CharacterType.Artist));
            }
        }
        // Effect num 10
        eventManager.invokeEvent(BuildingEvent.PAINTING_EVENT);

        for(Player player : players) {
            player.confirmToPay();
        }
    }
}
