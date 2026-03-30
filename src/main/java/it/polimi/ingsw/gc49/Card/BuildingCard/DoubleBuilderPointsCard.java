package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard.CharacterCard;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Model;

import java.util.Optional;

public class DoubleBuilderPointsCard extends BuildingCard {
    public DoubleBuilderPointsCard(BuildingEvent buildingEvent, EventManager manager, int PPReward, int foodPrice) {
        super(buildingEvent, manager, PPReward, foodPrice);
    }

    @Override
    public void onEventEffect(Model model, Optional<CharacterCard> drawnCard) {
        owner.data.doubleNumBuilderPoints();
    }
}
