package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the concrete {@link CharacterCard} implementations.
 * <p>
 * The {@code queueUpdater} dependency is passed as {@code null}; each card guards
 * against a null updater, so behaviour can be exercised in isolation.
 */
class CharacterCardTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Peppe", 0);
    }

    @Nested
    @DisplayName("Behaviour shared by every CharacterCard")
    class SharedBehaviour {

        @Test
        @DisplayName("canGet is always true and isLowerLineOnSetup is always true")
        void commonFlags() {
            Artist artist = new Artist(Era.FIRST, 2, null);
            assertTrue(artist.canGet(player));
            assertTrue(artist.isLowerLineOnSetup());

            Hunter hunter = new Hunter(false, Era.SECOND, 3, null);
            assertTrue(hunter.canGet(player));
            assertTrue(hunter.isLowerLineOnSetup());
        }

        @Test
        @DisplayName("getEra and getMinNumPlayers return the constructor values")
        void eraAndMinPlayers() {
            Builder builder = new Builder(2, 5, Era.THIRD, 4, null);
            assertEquals(Era.THIRD, builder.getEra());
            assertEquals(4, builder.getMinNumPlayers());
        }
    }

    @Nested
    @DisplayName("Hunter")
    class HunterTest {

        @Test
        @DisplayName("updateDataBank increments the Hunter counter")
        void updateDataBank() {
            Hunter hunter = new Hunter(false, Era.FIRST, 2, null);
            hunter.updateDataBank(player.data);
            assertEquals(1, player.data.getCharacterCount(CharacterType.Hunter));
        }

        @Test
        @DisplayName("non-drumstick Hunter grants no food on draw")
        void noFoodWithoutDrumstick() {
            // simulate the player already owning a Hunter
            player.data.addCharacterCount(CharacterType.Hunter, 1);
            Hunter hunter = new Hunter(false, Era.FIRST, 2, null);
            hunter.onDraw(player);
            assertEquals(0, player.getFood());
        }

        @Test
        @DisplayName("drumstick Hunter grants food equal to the owned Hunters on draw")
        void foodWithDrumstick() {
            player.data.addCharacterCount(CharacterType.Hunter, 3);
            Hunter hunter = new Hunter(true, Era.FIRST, 2, null);
            hunter.onDraw(player);
            assertEquals(3, player.getFood());
        }

        @Test
        @DisplayName("addCharacterCard wires updateDataBank then onDraw for a drumstick Hunter")
        void integrationThroughPlayer() {
            // first add a plain Hunter: count -> 1, no food
            player.addCharacterCard(new Hunter(false, Era.FIRST, 2, null));
            assertEquals(1, player.data.getCharacterCount(CharacterType.Hunter));
            assertEquals(0, player.getFood());

            // then add a drumstick Hunter: count -> 2, food += 2
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

    @Nested
    @DisplayName("Builder")
    class BuilderTest {

        @Test
        @DisplayName("updateDataBank adds counter, building discount and builder points")
        void updateDataBank() {
            Builder builder = new Builder(2, 5, Era.FIRST, 2, null);
            builder.updateDataBank(player.data);
            assertEquals(1, player.data.getCharacterCount(CharacterType.Builder));
            assertEquals(2, player.data.getNumBuildingDiscount());
            assertEquals(5, player.data.getNumBuilderPoints());
        }

        @Test
        @DisplayName("simpleToString is the localized name")
        void simpleString() {
            assertEquals("COSTRUTTORE", new Builder(0, 0, Era.FIRST, 2, null).simpleToString());
        }
    }

    @Nested
    @DisplayName("Shaman")
    class ShamanTest {

        @Test
        @DisplayName("updateDataBank adds stars and increments the Shaman counter")
        void updateDataBank() {
            Shaman shaman = new Shaman(3, Era.FIRST, 2, null);
            shaman.updateDataBank(player.data);
            assertEquals(3, player.data.getNumStars());
            assertEquals(1, player.data.getCharacterCount(CharacterType.Shaman));
        }

        @Test
        @DisplayName("simpleToString is the localized name")
        void simpleString() {
            assertEquals("SCIAMANO", new Shaman(1, Era.FIRST, 2, null).simpleToString());
        }
    }

    @Nested
    @DisplayName("Artist")
    class ArtistTest {

        @Test
        @DisplayName("updateDataBank increments the Artist counter only")
        void updateDataBank() {
            Artist artist = new Artist(Era.FIRST, 2, null);
            artist.updateDataBank(player.data);
            assertEquals(1, player.data.getCharacterCount(CharacterType.Artist));
            assertEquals(0, player.data.getNumStars());
        }

        @Test
        @DisplayName("simpleToString is the localized name")
        void simpleString() {
            assertEquals("ARTISTA", new Artist(Era.FIRST, 2, null).simpleToString());
        }
    }

    @Nested
    @DisplayName("Gatherer")
    class GathererTest {

        @Test
        @DisplayName("updateDataBank increments the counter and adds a fixed sustenance discount of 3")
        void updateDataBank() {
            Gatherer gatherer = new Gatherer(Era.FIRST, 2, null);
            gatherer.updateDataBank(player.data);
            assertEquals(1, player.data.getCharacterCount(CharacterType.Gatherer));
            assertEquals(3, player.data.getNumSustenanceDiscount());
        }

        @Test
        @DisplayName("two Gatherers stack their discount")
        void discountStacks() {
            new Gatherer(Era.FIRST, 2, null).updateDataBank(player.data);
            new Gatherer(Era.FIRST, 2, null).updateDataBank(player.data);
            assertEquals(6, player.data.getNumSustenanceDiscount());
        }

        @Test
        @DisplayName("simpleToString is the localized name")
        void simpleString() {
            assertEquals("RACCOGLITORE", new Gatherer(Era.FIRST, 2, null).simpleToString());
        }
    }

    @Nested
    @DisplayName("Inventor")
    class InventorTest {

        @Test
        @DisplayName("updateDataBank increments the counter and records the invention")
        void updateDataBank() {
            Inventor inventor = new Inventor(Invention.CANOE, Era.FIRST, 2, null);
            inventor.updateDataBank(player.data);
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
}
