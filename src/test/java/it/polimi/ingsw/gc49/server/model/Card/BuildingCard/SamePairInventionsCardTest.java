package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Invention;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Inventor;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SamePairInventionsCardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    @Test
    void updateDataBankAddsPoints() {
        SamePairInventionsCard card = new SamePairInventionsCard(BuildingEvent.DRAW_EVENT, 2, 5, Era.FIRST, 2, null);
        card.updateDataBank(peppe.data);
        assertEquals(2, peppe.data.getNumBuildingPoints());
    }

    @Test
    void onEventEffectGrantsFoodOnSamePair() {
        SamePairInventionsCard card = new SamePairInventionsCard(BuildingEvent.DRAW_EVENT, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe); // onDraw -> setOwner -> recordInventions() activates tracking

        // two identical inventions after acquisition form a same-pair
        peppe.addCharacterCard(new Inventor(Invention.CANOE, Era.FIRST, 2, null));
        peppe.addCharacterCard(new Inventor(Invention.CANOE, Era.FIRST, 2, null));

        card.onEventEffect();

        assertEquals(8, peppe.getFood()); // 5 + 3*1
    }

    @Test
    void onEventEffectNoSamePair() {
        SamePairInventionsCard card = new SamePairInventionsCard(BuildingEvent.DRAW_EVENT, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);

        peppe.addCharacterCard(new Inventor(Invention.CANOE, Era.FIRST, 2, null));
        peppe.addCharacterCard(new Inventor(Invention.BREAD, Era.FIRST, 2, null));

        card.onEventEffect();

        assertEquals(5, peppe.getFood());
    }

    @Test
    void simpleString() {
        SamePairInventionsCard card = new SamePairInventionsCard(BuildingEvent.DRAW_EVENT, 0, 5, Era.FIRST, 2, null);
        assertEquals("EDIFICIO (stracibo da inventori)", card.simpleToString());
    }
}
