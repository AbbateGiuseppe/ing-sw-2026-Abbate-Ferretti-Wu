package it.polimi.ingsw.gc49.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.model.BuildingEvent;
import it.polimi.ingsw.gc49.model.CharacterType;
import it.polimi.ingsw.gc49.model.Era;

public class BonusPointsByClassEndGameCard extends BuildingCard {
    private final CharacterType unit;
    private final int pointsPerUnit;

    public BonusPointsByClassEndGameCard( CharacterType unit, int pointsPerUnit, BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers);
        this.unit = unit;
        this.pointsPerUnit = pointsPerUnit;
    }

    @Override
    public void onEventEffect() {
        owner.addPoints(pointsPerUnit * owner.data.getCharacterCount(unit));
    }
}
