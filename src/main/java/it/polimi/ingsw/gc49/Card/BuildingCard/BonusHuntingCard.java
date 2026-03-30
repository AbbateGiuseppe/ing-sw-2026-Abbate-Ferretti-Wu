package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard.CharacterCard;
import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Model;

import java.util.Optional;

public class BonusHuntingCard extends BuildingCard {
    public BonusHuntingCard(BuildingEvent buildingEvent, EventManager manager, int PPReward, int foodPrice) {
        super(buildingEvent, manager, PPReward, foodPrice);
    }

    @Override
    public void onEventEffect(Model model, Optional<CharacterCard> drawnCard) {
        owner.setFoodToPay(owner.getFoodToPay() - owner.data.getCharacterCount(CharacterType.Hunter));
        owner.setPointsToPay(owner.getPointsToPay() - owner.data.getCharacterCount(CharacterType.Hunter));
    }
}
