package it.polimi.ingsw.gc49.server.model;

import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Invention;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DataBankTest {

    private Player player;
    private DataBank dataBank;

    @BeforeEach
    void setUp() {
        player = new Player("Peppe", 0);
        dataBank = new DataBank(player);
    }

    @Test
    @DisplayName("Constructor stores the assigned player")
    void constructorStoresPlayer() {
        assertSame(player, dataBank.assignedPlayer);
    }

    @Test
    @DisplayName("Fresh databank has empty/zero state")
    void freshStateIsEmpty() {
        assertEquals(0, dataBank.getNumCharacters());
        assertEquals(0, dataBank.getNumBuildingDiscount());
        assertEquals(0, dataBank.getNumBuildingPoints());
        assertEquals(0, dataBank.getNumSustenanceDiscount());
        assertEquals(0, dataBank.getNumStars());
        assertEquals(0, dataBank.getNumBuilderPoints());
        assertEquals(0, dataBank.getDifferentInventionCount());
        assertTrue(dataBank.getInventions().isEmpty());
        assertEquals(0, dataBank.getCharacterCount(CharacterType.CompleteSet));
    }

    @Test
    @DisplayName("addCharacterCount creates and accumulates counts per type")
    void addCharacterCountAccumulates() {
        dataBank.addCharacterCount(CharacterType.Hunter, 1);
        assertEquals(1, dataBank.getCharacterCount(CharacterType.Hunter));

        dataBank.addCharacterCount(CharacterType.Hunter, 2);
        assertEquals(3, dataBank.getCharacterCount(CharacterType.Hunter));
    }

    @Test
    @DisplayName("getCharacterCount returns 0 for a type never added")
    void unknownTypeReturnsZero() {
        dataBank.addCharacterCount(CharacterType.Hunter, 1);
        assertEquals(0, dataBank.getCharacterCount(CharacterType.Builder));
    }

    @Test
    @DisplayName("getNumCharacters sums across all stored types")
    void numCharactersSumsAllTypes() {
        dataBank.addCharacterCount(CharacterType.Hunter, 2);
        dataBank.addCharacterCount(CharacterType.Builder, 3);
        assertEquals(5, dataBank.getNumCharacters());
    }

    @Test
    @DisplayName("CompleteSet returns the minimum count across stored types")
    void completeSetReturnsMinimum() {
        dataBank.addCharacterCount(CharacterType.Hunter, 3);
        dataBank.addCharacterCount(CharacterType.Builder, 1);
        dataBank.addCharacterCount(CharacterType.Shaman, 2);
        assertEquals(1, dataBank.getCharacterCount(CharacterType.CompleteSet));
    }

    @Test
    @DisplayName("Building discount accumulates")
    void buildingDiscountAccumulates() {
        dataBank.addNumBuildingDiscount(2);
        dataBank.addNumBuildingDiscount(3);
        assertEquals(5, dataBank.getNumBuildingDiscount());
    }

    @Test
    @DisplayName("Sustenance discount accumulates")
    void sustenanceDiscountAccumulates() {
        dataBank.addNumSustenanceDiscount(4);
        assertEquals(4, dataBank.getNumSustenanceDiscount());
    }

    @Test
    @DisplayName("Building points accumulate")
    void buildingPointsAccumulate() {
        dataBank.addNumBuildingPoints(7);
        dataBank.addNumBuildingPoints(1);
        assertEquals(8, dataBank.getNumBuildingPoints());
    }

    @Test
    @DisplayName("Stars accumulate")
    void starsAccumulate() {
        dataBank.addNumStar(2);
        dataBank.addNumStar(5);
        assertEquals(7, dataBank.getNumStars());
    }

    @Test
    @DisplayName("Builder points accumulate and can be overwritten with setter")
    void builderPointsAddAndSet() {
        dataBank.addNumBuilderPoints(3);
        dataBank.addNumBuilderPoints(2);
        assertEquals(5, dataBank.getNumBuilderPoints());

        dataBank.setNumBuilderPoints(10);
        assertEquals(10, dataBank.getNumBuilderPoints());
    }

    @Test
    @DisplayName("addInvention records distinct inventions and counts uniqueness")
    void addInventionTracksDistinct() {
        Invention first = Invention.values()[0];
        Invention second = Invention.values()[1];

        dataBank.addInvention(first);
        assertEquals(1, dataBank.getDifferentInventionCount());
        assertTrue(dataBank.getInventions().contains(first));

        // adding the same invention again does not increase the distinct count
        dataBank.addInvention(first);
        assertEquals(1, dataBank.getDifferentInventionCount());

        dataBank.addInvention(second);
        assertEquals(2, dataBank.getDifferentInventionCount());
    }

    @Test
    @DisplayName("SamePairInventions flag is set only when recording is active and a duplicate is added")
    void samePairInventionsFlag() {
        Invention inv = Invention.values()[0];

        // without recordInventions() the same-pair tracking is inactive
        dataBank.addInvention(inv);
        dataBank.addInvention(inv);
        assertEquals(0, dataBank.getCharacterCount(CharacterType.SamePairInventions));

        // activate recording, then add a duplicate to trigger the flag
        dataBank.recordInventions();
        dataBank.addInvention(inv); // count -> 1
        dataBank.addInvention(inv); // duplicate -> sameInvention = true
        assertEquals(1, dataBank.getCharacterCount(CharacterType.SamePairInventions));
        // the flag is consumed on read
        assertEquals(0, dataBank.getCharacterCount(CharacterType.SamePairInventions));
    }

    @Test
    @DisplayName("recordCharaSet snapshots the current complete-set count")
    void recordCharaSetSnapshot() {
        dataBank.addCharacterCount(CharacterType.Hunter, 2);
        dataBank.addCharacterCount(CharacterType.Builder, 2);
        dataBank.recordCharaSet();
        assertEquals(2, dataBank.getCurrentNumCompleteCharacterSets());
    }

    @Test
    @DisplayName("incrementCurrentNumCompleteCharacterSets increases the recorded snapshot")
    void incrementCompleteSets() {
        dataBank.recordCharaSet(); // starts at 0 (no characters)
        dataBank.incrementCurrentNumCompleteCharacterSets();
        dataBank.incrementCurrentNumCompleteCharacterSets();
        assertEquals(2, dataBank.getCurrentNumCompleteCharacterSets());
    }
}
