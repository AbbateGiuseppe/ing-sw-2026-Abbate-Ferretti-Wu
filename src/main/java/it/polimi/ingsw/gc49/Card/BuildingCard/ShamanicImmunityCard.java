package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard.CharacterCard;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Model;

import java.util.Optional;

public class ShamanicImmunityCard extends BuildingCard {
    public ShamanicImmunityCard(BuildingEvent buildingEvent, EventManager manager, int PPReward, int foodPrice) {
        super(buildingEvent, manager, PPReward, foodPrice);
    }

    @Override
    public void onEventEffect(Model model, Optional<CharacterCard> drawnCard) {
        if (owner.getPointsToPay() > 0) {
            owner.setPointsToPay(0);
        }
    }
}
