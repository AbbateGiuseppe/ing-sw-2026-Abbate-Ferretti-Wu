package it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.DataBank;
import it.polimi.ingsw.gc49.Player;

public class Builder extends CharacterCard {
    private final int buildingDiscount;
    private final int numPoints;

    public Builder(int buildingDiscount, int numPoints) {
        this.buildingDiscount = buildingDiscount;
        this.numPoints = numPoints;
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
        dataBank.addCharacterCount(CharacterType.Builder,1);
        dataBank.addNumBuildingDiscount(buildingDiscount);
        dataBank.addNumBuilderPoints(numPoints);
    }
}
