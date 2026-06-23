package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ArtistTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Massi", 0);
    }

    @Test
    void constructorValues() {
        Artist artist = new Artist(Era.THIRD, 4, null);
        assertEquals(Era.THIRD, artist.getEra());
        assertEquals(4, artist.getMinNumPlayers());
    }

    @Test
    void commonFlags() {
        Artist artist = new Artist(Era.FIRST, 2, null);
        assertTrue(artist.canGet(player));
        assertTrue(artist.isLowerLineOnSetup());
    }

    @Test
    void updateDataBank() {
        new Artist(Era.FIRST, 2, null).updateDataBank(player.data);
        assertEquals(1, player.data.getCharacterCount(CharacterType.Artist));
        assertEquals(0, player.data.getNumStars());
        assertEquals(0, player.data.getNumBuildingDiscount());
    }

    @Test
    void counterStacks() {
        new Artist(Era.FIRST, 2, null).updateDataBank(player.data);
        new Artist(Era.FIRST, 2, null).updateDataBank(player.data);
        assertEquals(2, player.data.getCharacterCount(CharacterType.Artist));
    }

    @Test
    void simpleString() {
        assertEquals("ARTISTA", new Artist(Era.FIRST, 2, null).simpleToString());
    }
}
