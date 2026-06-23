package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShamanTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Massi", 0);
    }

    @Test
    void constructorValues() {
        Shaman shaman = new Shaman(2, Era.SECOND, 3, null);
        assertEquals(Era.SECOND, shaman.getEra());
        assertEquals(3, shaman.getMinNumPlayers());
    }

    @Test
    void commonFlags() {
        Shaman shaman = new Shaman(1, Era.FIRST, 2, null);
        assertTrue(shaman.canGet(player));
        assertTrue(shaman.isLowerLineOnSetup());
    }

    @Test
    void updateDataBank() {
        new Shaman(3, Era.FIRST, 2, null).updateDataBank(player.data);
        assertEquals(3, player.data.getNumStars());
        assertEquals(1, player.data.getCharacterCount(CharacterType.Shaman));
    }

    @Test
    void starsStack() {
        new Shaman(2, Era.FIRST, 2, null).updateDataBank(player.data);
        new Shaman(3, Era.FIRST, 2, null).updateDataBank(player.data);
        assertEquals(5, player.data.getNumStars());
        assertEquals(2, player.data.getCharacterCount(CharacterType.Shaman));
    }

    @Test
    void simpleString() {
        assertEquals("SCIAMANO", new Shaman(1, Era.FIRST, 2, null).simpleToString());
    }
}
