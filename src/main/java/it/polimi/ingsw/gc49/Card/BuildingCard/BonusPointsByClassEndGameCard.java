package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.Era;
import it.polimi.ingsw.gc49.EventManager;

public class BonusPointsByClassEndGameCard extends BuildingCard {
    private final CharacterType unit;
    private final int pointsPerUnit;

    public BonusPointsByClassEndGameCard( CharacterType unit, int pointsPerUnit, BuildingEvent buildingEvent, EventManager manager, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(buildingEvent, manager, pointsEndgame, foodPrice, era, minNumPlayers);
        this.unit = unit;
        this.pointsPerUnit = pointsPerUnit;
    }

    @Override
    public void onEventEffect() {
        owner.addPoints(pointsPerUnit * owner.data.getCharacterCount(unit));
    }
}
