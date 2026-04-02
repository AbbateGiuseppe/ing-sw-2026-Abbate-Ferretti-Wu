package it.polimi.ingsw.gc49.model.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.model.CharacterType;
import it.polimi.ingsw.gc49.model.DataBank;
import it.polimi.ingsw.gc49.model.Era;

public class Builder extends CharacterCard {
    private final int buildingDiscount;
    private final int numPoints;

    public Builder(int buildingDiscount, int numPoints, Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
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
