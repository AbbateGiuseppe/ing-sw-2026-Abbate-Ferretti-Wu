package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterSetCompleteFoodCardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    @Test
    void onDrawRecordsBaseline() {
        peppe.data.addCharacterCount(CharacterType.Hunter, 1);
        peppe.data.addCharacterCount(CharacterType.Builder, 1);
        peppe.data.addCharacterCount(CharacterType.Gatherer, 1);
        peppe.data.addCharacterCount(CharacterType.Inventor, 1);
        peppe.data.addCharacterCount(CharacterType.Shaman, 1);
        peppe.data.addCharacterCount(CharacterType.Artist, 1);

        CharacterSetCompleteFoodCard card =
                new CharacterSetCompleteFoodCard(BuildingEvent.DRAW_EVENT, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);

        assertEquals(1, peppe.data.getCurrentNumCompleteCharacterSets());
    }

    @Test
    void onEventEffectGrantsFoodOnNewSet() {
        CharacterSetCompleteFoodCard card =
                new CharacterSetCompleteFoodCard(BuildingEvent.DRAW_EVENT, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe); // baseline = 0

        // complete one set after acquiring the card
        peppe.data.addCharacterCount(CharacterType.Hunter, 1);
        peppe.data.addCharacterCount(CharacterType.Builder, 1);
        peppe.data.addCharacterCount(CharacterType.Gatherer, 1);
        peppe.data.addCharacterCount(CharacterType.Inventor, 1);
        peppe.data.addCharacterCount(CharacterType.Shaman, 1);
        peppe.data.addCharacterCount(CharacterType.Artist, 1);

        card.onEventEffect();

        assertEquals(10, peppe.getFood());
        assertEquals(1, peppe.data.getCurrentNumCompleteCharacterSets());
    }

    @Test
    void onEventEffectNoNewSet() {
        CharacterSetCompleteFoodCard card =
                new CharacterSetCompleteFoodCard(BuildingEvent.DRAW_EVENT, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe); // baseline = 0, still 0 complete sets

        card.onEventEffect();

        assertEquals(5, peppe.getFood());
    }

    @Test
    void simpleString() {
        CharacterSetCompleteFoodCard card =
                new CharacterSetCompleteFoodCard(BuildingEvent.DRAW_EVENT, 0, 5, Era.FIRST, 2, null);
        assertEquals("EDIFICIO (stracibo da set)", card.simpleToString());
    }
}
