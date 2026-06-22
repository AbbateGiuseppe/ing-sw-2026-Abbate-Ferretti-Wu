package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HunterTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Massi", 0);
    }

    @Test
    @DisplayName("getEra and getMinNumPlayers return the constructor values")
    void constructorValues() {
        Hunter hunter = new Hunter(false, Era.SECOND, 3, null);
        assertEquals(Era.SECOND, hunter.getEra());
        assertEquals(3, hunter.getMinNumPlayers());
    }

    @Test
    @DisplayName("canGet is always true and isLowerLineOnSetup is always true")
    void commonFlags() {
        Hunter hunter = new Hunter(false, Era.FIRST, 2, null);
        assertTrue(hunter.canGet(player));
        assertTrue(hunter.isLowerLineOnSetup());
    }

    @Test
    @DisplayName("updateDataBank increments the Hunter counter")
    void updateDataBank() {
        new Hunter(false, Era.FIRST, 2, null).updateDataBank(player.data);
        assertEquals(1, player.data.getCharacterCount(CharacterType.Hunter));
    }

    @Test
    @DisplayName("non-drumstick Hunter grants no food on draw")
    void noFoodWithoutDrumstick() {
        player.data.addCharacterCount(CharacterType.Hunter, 1);
        new Hunter(false, Era.FIRST, 2, null).onDraw(player);
        assertEquals(0, player.getFood());
    }

    @Test
    @DisplayName("drumstick Hunter grants food equal to the owned Hunters on draw")
    void foodWithDrumstick() {
        player.data.addCharacterCount(CharacterType.Hunter, 3);
        new Hunter(true, Era.FIRST, 2, null).onDraw(player);
        assertEquals(3, player.getFood());
    }

    @Test
    @DisplayName("addCharacterCard wires updateDataBank then onDraw")
    void integrationThroughPlayer() {
        player.addCharacterCard(new Hunter(false, Era.FIRST, 2, null));
        assertEquals(1, player.data.getCharacterCount(CharacterType.Hunter));
        assertEquals(0, player.getFood());

        player.addCharacterCard(new Hunter(true, Era.FIRST, 2, null));
        assertEquals(2, player.data.getCharacterCount(CharacterType.Hunter));
        assertEquals(2, player.getFood());
    }

    @Test
    @DisplayName("simpleToString is the localized name")
    void simpleString() {
        assertEquals("CACCIATORE", new Hunter(false, Era.FIRST, 2, null).simpleToString());
    }
}
