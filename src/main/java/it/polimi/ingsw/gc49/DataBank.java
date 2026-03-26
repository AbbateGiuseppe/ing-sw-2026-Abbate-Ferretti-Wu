package it.polimi.ingsw.gc49;

import java.util.*;

public class DataBank {
    public final Player assignedPlayer;
    private Map<CharacterType,Integer> numCharacterMap;
    private int currentNumCompleteCharacterSets;
    private int[] numInventions;
    private int sameInvention;
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
//    public void partialUpdate (Card addedCard) {
//        addedCard.updateDataBank(this);
//    }

    //### update done by going through every card the player has
//    public void fullUpdate () {
//
//    }


    public int getCharacterCount(CharacterType type) {
        if (!numCharacterMap.containsKey(type)) return 0;
        if (type == CharacterType.Set) {return Collections.min(numCharacterMap.values());}
        if (type == CharacterType.SameInventions) {
            if (sameInvention == 1){sameInvention = 0;return 1;}
            return 0;
        }
        return numCharacterMap.get(type);
    }
    public void addCharacterCount(CharacterType type,int increment) {
        if (numCharacterMap.containsKey(type)) {numCharacterMap.merge(type, increment, Integer::sum);}
        else {numCharacterMap.put(type,increment);}
    }
    public int getNumCharacters() { return numCharacterMap.values().stream().mapToInt(Integer::intValue).sum();}
    public int getNumBuildingDiscount() { return numBuildingDiscount; }
    public void addNumBuildingDiscount(int n) { numBuildingDiscount += n; }
    public void addNumBuilderPoints(int n) { numBuilderPoints += n; }
    public void addNumBuildingPoints(int n) { numBuildingPoints += n; }
    public void doubleNumBuilderPoints() { numBuilderPoints *= 2; }
    public int getNumSustenanceDiscount() { return numSustenanceDiscount; }
    public void addNumSustenanceDiscount(int n) { numSustenanceDiscount += n; }
    public void addNumStar(int n) {numStars += n;}
    public void addInvention(Invention invention) {
        if (numInventions != null) {
            if (numInventions[invention.ordinal()] == 1) {
                sameInvention = 1;
                numInventions[invention.ordinal()] = 0;
            } else {
                numInventions[invention.ordinal()] += 1;
            }
        }
        inventions.add(invention);}
    public void recordCharaSet() {currentNumCompleteCharacterSets = getCharacterCount(CharacterType.Set);}
    public void recordInventions() {numInventions = new int[10];}
    public int getCurrentNumCompleteCharacterSets() {return currentNumCompleteCharacterSets;}
    public void addCurrentNumCompleteCharacterSets(int n) {currentNumCompleteCharacterSets +=n;}
    public int getNumStars() {
        return numStars;
    }
}
