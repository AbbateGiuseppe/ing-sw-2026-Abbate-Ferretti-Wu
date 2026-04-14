package it.polimi.ingsw.gc49.model.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterCardTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer", 0);
    }

    @Test
    void testHunter_canGet_alwaysTrue() {
        Hunter hunter = new Hunter(false, Era.FIRST, 2);
        assertTrue(hunter.canGet(player), "Hunter card should always be drawable");
    }

    @Test
    void testHunter_updateDataBank_addsHunterCount() {
        Hunter hunter = new Hunter(false, Era.FIRST, 2);
        hunter.updateDataBank(player.data);

        assertEquals(1, player.data.getCharacterCount(CharacterType.Hunter), "DataBank should have 1 hunter");
    }

    @Test
    void testHunter_onDraw_withoutDrumstick_noFoodAdded() {
        player.data.addCharacterCount(CharacterType.Hunter, 2);
        player.addFood(5);
        Hunter hunter = new Hunter(false, Era.FIRST, 2);

        hunter.onDraw(player);

        assertEquals(5, player.getFood(), "Player food should not change without drumstick");
    }

    @Test
    void testHunter_onDraw_withDrumstick_addsFood() {
        player.data.addCharacterCount(CharacterType.Hunter, 3);
        player.addFood(5);
        Hunter hunter = new Hunter(true, Era.FIRST, 2);

        hunter.onDraw(player);

        assertEquals(8, player.getFood(), "Player should gain food equal to hunter count");
    }

    @Test
    void testArtist_canGet_alwaysTrue() {
        Artist artist = new Artist(Era.SECOND, 3);
        assertTrue(artist.canGet(player), "Artist card should always be drawable");
    }

    @Test
    void testArtist_updateDataBank_addsArtistCount() {
        Artist artist = new Artist(Era.SECOND, 3);
        artist.updateDataBank(player.data);

        assertEquals(1, player.data.getCharacterCount(CharacterType.Artist), "DataBank should have 1 artist");
    }

    @Test
    void testBuilder_canGet_alwaysTrue() {
        Builder builder = new Builder(2, 3, Era.FIRST, 2);
        assertTrue(builder.canGet(player), "Builder card should always be drawable");
    }

    @Test
    void testBuilder_updateDataBank_addsBuilderCount() {
        Builder builder = new Builder(2, 3, Era.FIRST, 2);
        builder.updateDataBank(player.data);

        assertEquals(1, player.data.getCharacterCount(CharacterType.Builder), "DataBank should have 1 builder");
    }

    @Test
    void testBuilder_updateDataBank_addsBuildingDiscount() {
        Builder builder = new Builder(2, 3, Era.FIRST, 2);
        builder.updateDataBank(player.data);

        assertEquals(2, player.data.getNumBuildingDiscount(), "DataBank should have building discount of 2");
    }

    @Test
    void testBuilder_updateDataBank_addsBuilderPoints() {
        Builder builder = new Builder(2, 5, Era.SECOND, 3);
        builder.updateDataBank(player.data);

        assertEquals(5, player.data.getNumBuilderPoints(), "DataBank should have 5 builder points");
    }

    @Test
    void testGatherer_canGet_alwaysTrue() {
        Gatherer gatherer = new Gatherer(Era.FIRST, 2);
        assertTrue(gatherer.canGet(player), "Gatherer card should always be drawable");
    }

    @Test
    void testGatherer_updateDataBank_addsGathererCount() {
        Gatherer gatherer = new Gatherer(Era.FIRST, 2);
        gatherer.updateDataBank(player.data);

        assertEquals(1, player.data.getCharacterCount(CharacterType.Gatherer), "DataBank should have 1 gatherer");
    }

    @Test
    void testGatherer_updateDataBank_addsSustenanceDiscount() {
        Gatherer gatherer = new Gatherer(Era.FIRST, 2);
        gatherer.updateDataBank(player.data);

        assertEquals(3, player.data.getNumSustenanceDiscount(), "DataBank should have sustenance discount of 3");
    }

    @Test
    void testInventor_canGet_alwaysTrue() {
        Inventor inventor = new Inventor(Invention.CANOE, Era.SECOND, 2);
        assertTrue(inventor.canGet(player), "Inventor card should always be drawable");
    }

    @Test
    void testInventor_updateDataBank_addsInventorCount() {
        Inventor inventor = new Inventor(Invention.CANOE, Era.SECOND, 3);
        inventor.updateDataBank(player.data);

        assertEquals(1, player.data.getCharacterCount(CharacterType.Inventor), "DataBank should have 1 inventor");
    }

    @Test
    void testInventor_updateDataBank_addsInvention() {
        Inventor inventor = new Inventor(Invention.BOWL, Era.THIRD_FINAL, 2);
        inventor.updateDataBank(player.data);

        assertTrue(player.data.getInventions().contains(Invention.BOWL), "DataBank should have BOWL invention");
    }

    @Test
    void testInventor_multipleInventions() {
        Inventor inventor1 = new Inventor(Invention.ARROWHEAD, Era.FIRST, 2);
        Inventor inventor2 = new Inventor(Invention.CANOE, Era.SECOND, 2);

        inventor1.updateDataBank(player.data);
        inventor2.updateDataBank(player.data);

        assertTrue(player.data.getInventions().contains(Invention.ARROWHEAD), "DataBank should have ARROWHEAD");
        assertTrue(player.data.getInventions().contains(Invention.CANOE), "DataBank should have CANOE");
        assertEquals(2, player.data.getCharacterCount(CharacterType.Inventor), "DataBank should have 2 inventors");
    }

    @Test
    void testShaman_canGet_alwaysTrue() {
        Shaman shaman = new Shaman(3, Era.FIRST, 2);
        assertTrue(shaman.canGet(player), "Shaman card should always be drawable");
    }

    @Test
    void testShaman_updateDataBank_addsShamanCount() {
        Shaman shaman = new Shaman(2, Era.SECOND, 3);
        shaman.updateDataBank(player.data);

        assertEquals(1, player.data.getCharacterCount(CharacterType.Shaman), "DataBank should have 1 shaman");
    }

    @Test
    void testShaman_updateDataBank_addsStars() {
        Shaman shaman = new Shaman(3, Era.THIRD_FINAL, 2);
        shaman.updateDataBank(player.data);

        assertEquals(3, player.data.getNumStars(), "DataBank should have 3 stars");
    }

    @Test
    void testShaman_multipleCards_accumulateStars() {
        Shaman shaman1 = new Shaman(2, Era.FIRST, 2);
        Shaman shaman2 = new Shaman(3, Era.SECOND, 2);

        shaman1.updateDataBank(player.data);
        shaman2.updateDataBank(player.data);

        assertEquals(5, player.data.getNumStars(), "DataBank should have 5 stars total");
        assertEquals(2, player.data.getCharacterCount(CharacterType.Shaman), "DataBank should have 2 shamans");
    }

    @Test
    void testCharacterCard_eraAndMinPlayers() {
        Hunter hunter = new Hunter(true, Era.THIRD_FINAL, 4);

        assertEquals(Era.THIRD_FINAL, hunter.getEra(), "Card should have correct era");
        assertEquals(4, hunter.getMinNumPlayers(), "Card should have correct min players");
    }

    @Test
    void testMultipleCharacterCards_integration() {
        Hunter hunter = new Hunter(false, Era.FIRST, 2);
        Artist artist = new Artist(Era.FIRST, 2);
        Builder builder = new Builder(1, 2, Era.FIRST, 2);
        Gatherer gatherer = new Gatherer(Era.FIRST, 2);

        hunter.updateDataBank(player.data);
        artist.updateDataBank(player.data);
        builder.updateDataBank(player.data);
        gatherer.updateDataBank(player.data);

        assertEquals(1, player.data.getCharacterCount(CharacterType.Hunter), "Should have 1 hunter");
        assertEquals(1, player.data.getCharacterCount(CharacterType.Artist), "Should have 1 artist");
        assertEquals(1, player.data.getCharacterCount(CharacterType.Builder), "Should have 1 builder");
        assertEquals(1, player.data.getCharacterCount(CharacterType.Gatherer), "Should have 1 gatherer");
        assertEquals(1, player.data.getNumBuildingDiscount(), "Should have building discount");
        assertEquals(3, player.data.getNumSustenanceDiscount(), "Should have sustenance discount");
    }
}
