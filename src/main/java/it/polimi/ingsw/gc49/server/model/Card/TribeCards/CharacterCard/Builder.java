package it.polimi.ingsw.gc49.server.model.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.DataBank;
import it.polimi.ingsw.gc49.server.model.Era;

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

    @Override
    public String toString() {
        return "Builder{" +
                "era=" +  era +
                "buildingDiscount=" + buildingDiscount +
                ", numPoints=" + numPoints +
                '}';
    }

    @Override
    public String simpleToString () {
        return "COSTRUTTORE";
    }
}
