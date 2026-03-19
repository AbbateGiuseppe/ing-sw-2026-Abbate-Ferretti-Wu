package it.polimi.ingsw.gc49;

import java.util.EnumSet;

public class DataBank {
    public final Player assignedPlayer;
    private int numStars;
    private int numShamans;
    private int numBuildingDiscount;
    private int numBuilderPoints;
    private int numBuilders;
    private int numArtist;
    private int numSustenanceDiscount;
    private int numGatherers;
    private int numHunters;
    private int numInventors;
    private EnumSet<Invention> inventions;
    private int numBuildingPoints;

    public DataBank(Player assignedPlayer) {
        this.assignedPlayer = assignedPlayer;
    }

    //### update done by considering just the new card being added to the previously saved data
    public void partialUpdate () {

    }

    //### update done by going through every card the player has
    public void fullUpdate () {

    }
}
