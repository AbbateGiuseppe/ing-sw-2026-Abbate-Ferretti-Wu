package it.polimi.ingsw.gc49;

import java.util.*;

public class DataBank {
    public final Player assignedPlayer;
    private Map<CharacterType,Integer> numCharacterMap;
    private int numStars;
    private int numBuildingDiscount;
    private int numBuilderPoints;
    private int numSustenanceDiscount;
    private EnumSet<Invention> inventions;
    private int numBuildingPoints;

    // Event Management
    // the number of complete sets of character cards at the acquisition of the building card with effect 1
    private int currentNumCompleteCharacterSets;
    private int[] numInventions;
    // the number of same pair inventions(used for effect 3)
    private boolean sameInvention;

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


    // Returns the number of character cards of the given type
    public int getCharacterCount(CharacterType type) {
        // Returns the number of complete sets of character cards
        if (type == CharacterType.CompleteSet) {
            if (numCharacterMap.isEmpty()) {return 0;}
            return Collections.min(numCharacterMap.values());
        }
        // Returns the number of same pair inventions(used for effect 5)
        if (type == CharacterType.SamePairInventions) {
            if (sameInvention){sameInvention = false;return 1;}
            return 0;
        }
        if (!numCharacterMap.containsKey(type)) return 0;
        return numCharacterMap.get(type);
    }

    // Increase the count of the character cards of the given type
    public void addCharacterCount(CharacterType type,int increment) {
        // if the count already exists in the map,then accumulate the count by increment
        if (numCharacterMap.containsKey(type)) {numCharacterMap.merge(type, increment, Integer::sum);}
        else {numCharacterMap.put(type,increment);}
    }

    // Returns the total number of character cards
    public int getNumCharacters() { return numCharacterMap.values().stream().mapToInt(Integer::intValue).sum();}
    public int getNumBuildingDiscount() { return numBuildingDiscount; }
    public void addNumBuildingDiscount(int n) { numBuildingDiscount += n; }
    public void addNumBuilderPoints(int n) { numBuilderPoints += n; }
    public void addNumBuildingPoints(int n) { numBuildingPoints += n; }
    public void doubleNumBuilderPoints() { numBuilderPoints *= 2; }
    public int getNumSustenanceDiscount() { return numSustenanceDiscount; }
    public void addNumSustenanceDiscount(int n) { numSustenanceDiscount += n; }
    public void addNumStar(int n) {numStars += n;}
    public int getNumStars() {return numStars;}
    public int getNumBuilderPoints() {return numBuilderPoints;}
    public EnumSet<Invention> getInventions() {return inventions;}
    public int getDifferentInventionCount() { return inventions.size(); }

    // Event Management
    public void addInvention(Invention invention) {
        if (numInventions != null) {
            if (numInventions[invention.ordinal()] == 1) {
                sameInvention = true;
                numInventions[invention.ordinal()] = 0;
            } else {
                numInventions[invention.ordinal()] += 1;
            }
        }
        inventions.add(invention);}
    public void recordCharaSet() {currentNumCompleteCharacterSets = getCharacterCount(CharacterType.CompleteSet);}
    public void recordInventions() {numInventions = new int[10];}
    public int getCurrentNumCompleteCharacterSets() {return currentNumCompleteCharacterSets;}
    public void incrementCurrentNumCompleteCharacterSets() {currentNumCompleteCharacterSets++;}
}
