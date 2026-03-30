package it.polimi.ingsw.gc49;

import it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard.CharacterCard;

import java.util.EventListener;
import java.util.Optional;

public interface BuildingEventListener extends EventListener {
    /**
     *
     * @param drawnCard is used for effect 1 and 5
     */
    public void onEventEffect(Model model, Optional<CharacterCard> drawnCard);
}
