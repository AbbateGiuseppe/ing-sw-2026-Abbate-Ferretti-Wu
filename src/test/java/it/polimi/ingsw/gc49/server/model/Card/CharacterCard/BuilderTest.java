package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuilderTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Massi", 0);
    }

    @Test
    void constructorValues() {
        Builder builder = new Builder(2, 5, Era.THIRD, 4, null);
        assertEquals(Era.THIRD, builder.getEra());
        assertEquals(4, builder.getMinNumPlayers());
    }

    @Test
    void commonFlags() {
        Builder builder = new Builder(1, 1, Era.FIRST, 2, null);
        assertTrue(builder.canGet(player));
        assertTrue(builder.isLowerLineOnSetup());
    }

    @Test
    void updateDataBank() {
        new Builder(2, 5, Era.FIRST, 2, null).updateDataBank(player.data);
        assertEquals(1, player.data.getCharacterCount(CharacterType.Builder));
        assertEquals(2, player.data.getNumBuildingDiscount());
        assertEquals(5, player.data.getNumBuilderPoints());
    }

    @Test
    void valuesStack() {
        new Builder(2, 3, Era.FIRST, 2, null).updateDataBank(player.data);
        new Builder(1, 4, Era.FIRST, 2, null).updateDataBank(player.data);
        assertEquals(2, player.data.getCharacterCount(CharacterType.Builder));
        assertEquals(3, player.data.getNumBuildingDiscount());
        assertEquals(7, player.data.getNumBuilderPoints());
    }

    @Test
    void simpleString() {
        assertEquals("COSTRUTTORE", new Builder(0, 0, Era.FIRST, 2, null).simpleToString());
    }
}
