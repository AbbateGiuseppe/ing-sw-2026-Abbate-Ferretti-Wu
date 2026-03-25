package it.polimi.ingsw.gc49;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class DataBank {
    public final Player assignedPlayer;
    private Map<CharacterType,Integer> numCharacterMap;
    private int numStars;
    private int numBuildingDiscount;
    private int numBuilderPoints;
    private int numSustenanceDiscount;
    private EnumSet<Invention> inventions;
    private int numBuildingPoints;

    public DataBank(Player assignedPlayer) {
        this.assignedPlayer = assignedPlayer;
        numCharacterMap = new HashMap<>();
        inventions = EnumSet.noneOf(Invention.class);
    }

    //### update done by considering just the new card being added to the previously saved data
    public void partialUpdate () {

    }

    //### update done by going through every card the player has
    public void fullUpdate () {

    }

    public int getCharacterCount(CharacterType type) {return numCharacterMap.get(type);}
    public void addCharacterCount(CharacterType type,int increment) {numCharacterMap.merge(type, increment, Integer::sum);}
    public int getNumCharacters() { return numCharacterMap.values().stream().mapToInt(Integer::intValue).sum();}
    public int getNumBuildingDiscount() { return numBuildingDiscount; }
    public void addNumBuildingDiscount(int n) { numBuildingDiscount += n; }
    public void addNumBuilderPoints(int n) { numBuilderPoints += n; }
    public void addNumBuildingPoints(int n) { numBuildingPoints += n; }
    public void doubleNumBuilderPoints() { numBuilderPoints *= 2; }
    public int getNumSustenanceDiscount() { return numSustenanceDiscount; }
    public void addNumSustenanceDiscount(int n) { numSustenanceDiscount += n; }
    public void addNumStar(int n) {numStars += n;}
    public void addInvention(Invention invention) {inventions.add(invention);}
}
