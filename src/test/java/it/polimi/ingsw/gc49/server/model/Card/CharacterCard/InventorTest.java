package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventorTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Massi", 0);
    }

    @Test
    @DisplayName("getEra and getMinNumPlayers return the constructor values")
    void constructorValues() {
        Inventor inventor = new Inventor(Invention.CANOE, Era.SECOND, 3, null);
        assertEquals(Era.SECOND, inventor.getEra());
        assertEquals(3, inventor.getMinNumPlayers());
    }

    @Test
    @DisplayName("canGet is always true and isLowerLineOnSetup is always true")
    void commonFlags() {
        Inventor inventor = new Inventor(Invention.CANOE, Era.FIRST, 2, null);
        assertTrue(inventor.canGet(player));
        assertTrue(inventor.isLowerLineOnSetup());
    }

    @Test
    @DisplayName("updateDataBank increments the counter and records the invention")
    void updateDataBank() {
        new Inventor(Invention.CANOE, Era.FIRST, 2, null).updateDataBank(player.data);
        assertEquals(1, player.data.getCharacterCount(CharacterType.Inventor));
        assertEquals(1, player.data.getDifferentInventionCount());
        assertTrue(player.data.getInventions().contains(Invention.CANOE));
    }

    @Test
    @DisplayName("two Inventors with different inventions yield two distinct inventions")
    void distinctInventions() {
        new Inventor(Invention.CANOE, Era.FIRST, 2, null).updateDataBank(player.data);
        new Inventor(Invention.BREAD, Era.FIRST, 2, null).updateDataBank(player.data);
        assertEquals(2, player.data.getCharacterCount(CharacterType.Inventor));
        assertEquals(2, player.data.getDifferentInventionCount());
    }

    @Test
    @DisplayName("two Inventors with the same invention count once as distinct")
    void duplicateInvention() {
        new Inventor(Invention.CANOE, Era.FIRST, 2, null).updateDataBank(player.data);
        new Inventor(Invention.CANOE, Era.FIRST, 2, null).updateDataBank(player.data);
        assertEquals(2, player.data.getCharacterCount(CharacterType.Inventor));
        assertEquals(1, player.data.getDifferentInventionCount());
    }

    @Test
    @DisplayName("simpleToString is the localized name")
    void simpleString() {
        assertEquals("INVENTORE", new Inventor(Invention.CANOE, Era.FIRST, 2, null).simpleToString());
    }
}
