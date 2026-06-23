package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GathererTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Massi", 0);
    }

    @Test
    void constructorValues() {
        Gatherer gatherer = new Gatherer(Era.SECOND, 3, null);
        assertEquals(Era.SECOND, gatherer.getEra());
        assertEquals(3, gatherer.getMinNumPlayers());
    }

    @Test
    void commonFlags() {
        Gatherer gatherer = new Gatherer(Era.FIRST, 2, null);
        assertTrue(gatherer.canGet(player));
        assertTrue(gatherer.isLowerLineOnSetup());
    }

    @Test
    void updateDataBank() {
        new Gatherer(Era.FIRST, 2, null).updateDataBank(player.data);
        assertEquals(1, player.data.getCharacterCount(CharacterType.Gatherer));
        assertEquals(3, player.data.getNumSustenanceDiscount());
    }

    @Test
    void discountStacks() {
        new Gatherer(Era.FIRST, 2, null).updateDataBank(player.data);
        new Gatherer(Era.FIRST, 2, null).updateDataBank(player.data);
        assertEquals(2, player.data.getCharacterCount(CharacterType.Gatherer));
        assertEquals(6, player.data.getNumSustenanceDiscount());
    }

    @Test
    void simpleString() {
        assertEquals("RACCOGLITORE", new Gatherer(Era.FIRST, 2, null).simpleToString());
    }
}
