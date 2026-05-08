package it.polimi.ingsw.gc49.model.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.server.model.Card.TribeCards.CharacterCard.*;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Invention;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CharacterCardTest2 {
    private Player player;
    private Era era = Era.FIRST;
    private int minNumPlayers = 3;

    @BeforeEach
    void setUp() {
        player = new Player(null,0);
    }

    @Test
    public void testArtist() {
        CharacterCard card = new Artist(era,minNumPlayers);
        int oldCount = player.data.getCharacterCount(CharacterType.Artist);
        card.updateDataBank(player.data);
        int newCount = player.data.getCharacterCount(CharacterType.Artist);
        assertEquals(oldCount + 1, newCount);
    }

    @Test
    public void testBuilder() {
        int buildingDiscount = 2;
        int numPoints = 3;
        CharacterCard card = new Builder(buildingDiscount,numPoints,era,minNumPlayers);
        int oldCount = player.data.getCharacterCount(CharacterType.Builder);
        int oldBuildingDiscount = player.data.getNumBuildingDiscount();
        int oldBuilderPoints = player.data.getNumBuilderPoints();
        card.updateDataBank(player.data);
        int newCount = player.data.getCharacterCount(CharacterType.Builder);
        int newDiscount = player.data.getNumBuildingDiscount();
        int newPoints = player.data.getNumBuilderPoints();
        assertEquals(oldCount + 1, newCount);
        assertEquals(oldBuildingDiscount + buildingDiscount, newDiscount);
        assertEquals(oldBuilderPoints + numPoints, newPoints);
    }

    @Test
    public void testGatherer() {
        CharacterCard card = new Gatherer(era,minNumPlayers);
        int oldCount = player.data.getCharacterCount(CharacterType.Gatherer);
        int oldSustenanceDiscount = player.data.getNumSustenanceDiscount();
        card.updateDataBank(player.data);
        int newCount = player.data.getCharacterCount(CharacterType.Gatherer);
        int newSustenanceDiscount = player.data.getNumSustenanceDiscount();
        assertEquals(oldCount + 1, newCount);
        assertEquals(oldSustenanceDiscount + 3, newSustenanceDiscount);
    }

    @Test
    public void testHunterWithoutDrumstick() {
        CharacterCard card = new Hunter(false,era,minNumPlayers);
        int oldCount = player.data.getCharacterCount(CharacterType.Hunter);
        card.updateDataBank(player.data);
        int newCount = player.data.getCharacterCount(CharacterType.Hunter);
        assertEquals(oldCount + 1, newCount);
    }

    @Test
    public void testHunterWithDrumstick() {
        int initialCount = 9;
        player.data.addCharacterCount(CharacterType.Hunter,initialCount);
        CharacterCard card = new Hunter(true,era,minNumPlayers);
        int oldCount = player.data.getCharacterCount(CharacterType.Hunter);
        int oldFood = player.getFood();
        card.updateDataBank(player.data);
        card.onDraw(player);
        int newCount = player.data.getCharacterCount(CharacterType.Hunter);
        int newFood = player.getFood();
        assertEquals(oldCount + 1, newCount);
        assertEquals(oldFood + newCount, newFood);
    }

    @Test
    public void testInventor() {
        CharacterCard card = new Inventor(Invention.ARROWHEAD,era,minNumPlayers);
        int oldCount = player.data.getCharacterCount(CharacterType.Inventor);
        assertFalse(player.data.getInventions().contains(Invention.ARROWHEAD));
        card.updateDataBank(player.data);
        int newCount = player.data.getCharacterCount(CharacterType.Inventor);
        assertEquals(oldCount + 1, newCount);
        assertTrue(player.data.getInventions().contains(Invention.ARROWHEAD));
    }

    @Test
    public void testShaman() {
        int numStars = 3;
        CharacterCard card = new Shaman(numStars,era,minNumPlayers);
        int oldCount = player.data.getCharacterCount(CharacterType.Shaman);
        int oldNumStars = player.data.getNumStars();
        card.updateDataBank(player.data);
        int newCount = player.data.getCharacterCount(CharacterType.Shaman);
        int newNumStars = player.data.getNumStars();
        assertEquals(oldCount + 1, newCount);
        assertEquals(oldNumStars + numStars, newNumStars);
    }
}