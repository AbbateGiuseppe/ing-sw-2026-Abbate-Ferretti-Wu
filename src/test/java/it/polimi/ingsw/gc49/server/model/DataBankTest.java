package it.polimi.ingsw.gc49.server.model;

import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Invention;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unified test class for {@link DataBank}. Targets ≥90% line and branch coverage by
 * exercising every public method and every conditional branch (CompleteSet on empty vs.
 * populated map, SamePairInventions with the flag on/off, addInvention with and without
 * the same-pair tracker active, and so on).
 */
class DataBankTest {

    private DataBank dataBank;
    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
        dataBank = peppe.data; // DataBank is created inside Player
    }

    // --- assignedPlayer ---

    @Test
    @DisplayName("assignedPlayer is the player passed to the constructor")
    void assignedPlayerIsStored() {
        DataBank fresh = new DataBank(peppe);
        assertSame(peppe, fresh.assignedPlayer);
    }

    // --- getCharacterCount: every branch ---

    @Test
    @DisplayName("getCharacterCount(CompleteSet) returns 0 when the map is empty")
    void completeSetEmptyMapIsZero() {
        assertEquals(0, dataBank.getCharacterCount(CharacterType.CompleteSet));
    }

    @Test
    @DisplayName("getCharacterCount(CompleteSet) returns the minimum of the counts when populated")
    void completeSetReturnsMinimum() {
        dataBank.addCharacterCount(CharacterType.Hunter, 3);
        dataBank.addCharacterCount(CharacterType.Builder, 1);
        dataBank.addCharacterCount(CharacterType.Shaman, 2);
        assertEquals(1, dataBank.getCharacterCount(CharacterType.CompleteSet));
    }

    @Test
    @DisplayName("getCharacterCount(SamePairInventions) returns 0 when the flag is off")
    void samePairFlagOff() {
        assertEquals(0, dataBank.getCharacterCount(CharacterType.SamePairInventions));
    }

    @Test
    @DisplayName("getCharacterCount(SamePairInventions) returns 1 when the flag is on, and consumes it")
    void samePairFlagOnConsumed() {
        // arm the flag by recording inventions and adding a duplicate
        dataBank.recordInventions();
        Invention inv = Invention.CANOE;
        dataBank.addInvention(inv);
        dataBank.addInvention(inv);

        assertEquals(1, dataBank.getCharacterCount(CharacterType.SamePairInventions));
        // flag consumed on read
        assertEquals(0, dataBank.getCharacterCount(CharacterType.SamePairInventions));
    }

    @Test
    @DisplayName("getCharacterCount(other) returns 0 when the type was never added")
    void getCharacterCountUnknownType() {
        assertEquals(0, dataBank.getCharacterCount(CharacterType.Hunter));
    }

    @Test
    @DisplayName("getCharacterCount(other) returns the stored count when present")
    void getCharacterCountKnownType() {
        dataBank.addCharacterCount(CharacterType.Hunter, 5);
        assertEquals(5, dataBank.getCharacterCount(CharacterType.Hunter));
    }

    // --- addCharacterCount: both branches ---

    @Test
    @DisplayName("addCharacterCount inserts a new entry when the type is unknown")
    void addCharacterCountFirstTime() {
        dataBank.addCharacterCount(CharacterType.Artist, 2);
        assertEquals(2, dataBank.getCharacterCount(CharacterType.Artist));
    }

    @Test
    @DisplayName("addCharacterCount accumulates when the type is already present")
    void addCharacterCountAccumulates() {
        dataBank.addCharacterCount(CharacterType.Hunter, 2);
        dataBank.addCharacterCount(CharacterType.Hunter, 3);
        assertEquals(5, dataBank.getCharacterCount(CharacterType.Hunter));
    }

    @Test
    @DisplayName("addCharacterCount accepts negative increments")
    void addCharacterCountNegative() {
        dataBank.addCharacterCount(CharacterType.Hunter, 5);
        dataBank.addCharacterCount(CharacterType.Hunter, -2);
        assertEquals(3, dataBank.getCharacterCount(CharacterType.Hunter));
    }

    // --- getNumCharacters ---

    @Test
    @DisplayName("getNumCharacters is zero on a fresh DataBank")
    void numCharactersInitiallyZero() {
        assertEquals(0, dataBank.getNumCharacters());
    }

    @Test
    @DisplayName("getNumCharacters sums every type's count")
    void numCharactersSums() {
        dataBank.addCharacterCount(CharacterType.Hunter, 2);
        dataBank.addCharacterCount(CharacterType.Builder, 1);
        dataBank.addCharacterCount(CharacterType.Shaman, 3);
        dataBank.addCharacterCount(CharacterType.Artist, 1);
        assertEquals(7, dataBank.getNumCharacters());
    }

    // --- numeric accumulators ---

    @Test
    @DisplayName("addNumBuildingDiscount accumulates and returns the running total")
    void buildingDiscountAccumulator() {
        assertEquals(0, dataBank.getNumBuildingDiscount());
        dataBank.addNumBuildingDiscount(2);
        dataBank.addNumBuildingDiscount(3);
        assertEquals(5, dataBank.getNumBuildingDiscount());
    }

    @Test
    @DisplayName("addNumBuilderPoints accumulates and is exposed by getNumBuilderPoints")
    void builderPointsAccumulator() {
        assertEquals(0, dataBank.getNumBuilderPoints());
        dataBank.addNumBuilderPoints(4);
        dataBank.addNumBuilderPoints(1);
        assertEquals(5, dataBank.getNumBuilderPoints());
    }

    @Test
    @DisplayName("setNumBuilderPoints replaces the value")
    void setBuilderPoints() {
        dataBank.addNumBuilderPoints(10);
        dataBank.setNumBuilderPoints(3);
        assertEquals(3, dataBank.getNumBuilderPoints());
    }

    @Test
    @DisplayName("addNumBuildingPoints accumulates and is exposed by getNumBuildingPoints")
    void buildingPointsAccumulator() {
        assertEquals(0, dataBank.getNumBuildingPoints());
        dataBank.addNumBuildingPoints(7);
        dataBank.addNumBuildingPoints(2);
        assertEquals(9, dataBank.getNumBuildingPoints());
    }

    @Test
    @DisplayName("addNumSustenanceDiscount accumulates")
    void sustenanceDiscountAccumulator() {
        assertEquals(0, dataBank.getNumSustenanceDiscount());
        dataBank.addNumSustenanceDiscount(2);
        assertEquals(2, dataBank.getNumSustenanceDiscount());
    }

    @Test
    @DisplayName("addNumStar accumulates")
    void starsAccumulator() {
        assertEquals(0, dataBank.getNumStars());
        dataBank.addNumStar(3);
        dataBank.addNumStar(1);
        assertEquals(4, dataBank.getNumStars());
    }

    // --- inventions ---

    @Test
    @DisplayName("getInventions on a fresh DataBank is an empty EnumSet")
    void inventionsInitiallyEmpty() {
        EnumSet<Invention> inv = dataBank.getInventions();
        assertNotNull(inv);
        assertTrue(inv.isEmpty());
        assertEquals(0, dataBank.getDifferentInventionCount());
    }

    @Test
    @DisplayName("addInvention without recording adds to the set without setting the flag")
    void addInventionWithoutRecording() {
        Invention inv = Invention.CANOE;
        dataBank.addInvention(inv);
        dataBank.addInvention(inv); // duplicate before recordInventions: must not set the flag

        assertEquals(1, dataBank.getDifferentInventionCount());
        assertEquals(0, dataBank.getCharacterCount(CharacterType.SamePairInventions));
    }

    @Test
    @DisplayName("recordInventions activates the same-pair tracker")
    void recordInventionsActivatesTracker() {
        dataBank.recordInventions();
        // first copy: counter -> 1, flag stays off
        dataBank.addInvention(Invention.CANOE);
        assertEquals(0, dataBank.getCharacterCount(CharacterType.SamePairInventions));

        // second copy: pair formed, flag on
        dataBank.addInvention(Invention.CANOE);
        assertEquals(1, dataBank.getCharacterCount(CharacterType.SamePairInventions));
    }

    @Test
    @DisplayName("after a pair triggers the flag, the counter resets so the third copy does not retrigger")
    void samePairResetsAfterTrigger() {
        dataBank.recordInventions();
        dataBank.addInvention(Invention.CANOE);
        dataBank.addInvention(Invention.CANOE);
        assertEquals(1, dataBank.getCharacterCount(CharacterType.SamePairInventions)); // consumes

        // third copy: counter went back to 1, no pair yet
        dataBank.addInvention(Invention.CANOE);
        assertEquals(0, dataBank.getCharacterCount(CharacterType.SamePairInventions));

        // fourth copy: second pair formed
        dataBank.addInvention(Invention.CANOE);
        assertEquals(1, dataBank.getCharacterCount(CharacterType.SamePairInventions));
    }

    @Test
    @DisplayName("getInventions enforces set semantics: duplicates are not stored")
    void inventionsAreASet() {
        dataBank.addInvention(Invention.CANOE);
        dataBank.addInvention(Invention.CANOE);
        dataBank.addInvention(Invention.CANOE);
        assertEquals(1, dataBank.getInventions().size());
        assertEquals(1, dataBank.getDifferentInventionCount());
    }

    @Test
    @DisplayName("addInvention covers every invention in the rulebook")
    void allInventionsCounted() {
        for (Invention inv : Invention.values()) {
            dataBank.addInvention(inv);
        }
        assertEquals(Invention.values().length, dataBank.getDifferentInventionCount());
    }

    // --- complete character sets snapshot ---

    @Test
    @DisplayName("getCurrentNumCompleteCharacterSets starts at zero")
    void completeCharacterSetsInitiallyZero() {
        assertEquals(0, dataBank.getCurrentNumCompleteCharacterSets());
    }

    @Test
    @DisplayName("recordCharaSet snapshots zero when no characters are present")
    void recordCharaSetOnEmpty() {
        dataBank.recordCharaSet();
        assertEquals(0, dataBank.getCurrentNumCompleteCharacterSets());
    }

    @Test
    @DisplayName("recordCharaSet snapshots the current CompleteSet count")
    void recordCharaSetCapturesCurrent() {
        dataBank.addCharacterCount(CharacterType.Hunter, 2);
        dataBank.addCharacterCount(CharacterType.Builder, 2);
        dataBank.recordCharaSet();
        assertEquals(2, dataBank.getCurrentNumCompleteCharacterSets());
    }

    @Test
    @DisplayName("incrementCurrentNumCompleteCharacterSets increases the snapshot")
    void incrementCompleteSets() {
        dataBank.recordCharaSet(); // 0
        dataBank.incrementCurrentNumCompleteCharacterSets();
        dataBank.incrementCurrentNumCompleteCharacterSets();
        assertEquals(2, dataBank.getCurrentNumCompleteCharacterSets());
    }

    @Test
    @DisplayName("DataBank is Serializable")
    void isSerializable() {
        assertInstanceOf(java.io.Serializable.class, dataBank);
    }
}
