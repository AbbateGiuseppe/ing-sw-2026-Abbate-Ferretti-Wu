package it.polimi.ingsw.gc49.server.model.Card.EventCard;

import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.EventManager;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SustenanceEventTest {

    private Player peppe;
    private EventManager manager;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
        manager = new EventManager();
    }

    @Test
    void enoughFoodPaysOnePerCharacter() {
        peppe.data.addCharacterCount(CharacterType.Hunter, 3); // 3 characters
        peppe.setFood(10);
        SustenanceEvent event = new SustenanceEvent(2, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe));

        assertEquals(7, peppe.getFood());   // 10 - 3
        assertEquals(0, peppe.getPoints()); // no starvation
    }

    @Test
    void gathererDiscountReducesCost() {
        peppe.data.addCharacterCount(CharacterType.Gatherer, 1);
        peppe.data.addCharacterCount(CharacterType.Hunter, 4); // 5 characters
        peppe.data.addNumSustenanceDiscount(3);                // gatherer's discount
        peppe.setFood(10);
        SustenanceEvent event = new SustenanceEvent(2, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe));

        assertEquals(8, peppe.getFood()); // 10 - (5 - 3)
        assertEquals(0, peppe.getPoints());
    }

    @Test
    void discountClampedAtZero() {
        peppe.data.addCharacterCount(CharacterType.Gatherer, 1);
        peppe.data.addNumSustenanceDiscount(10); // big discount, only one character
        peppe.setFood(5);
        SustenanceEvent event = new SustenanceEvent(2, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe));

        assertEquals(5, peppe.getFood());
        assertEquals(0, peppe.getPoints());
    }

    @Test
    void starvationPenalty() {
        peppe.data.addCharacterCount(CharacterType.Hunter, 5); // owes 5 food
        peppe.setFood(2);
        peppe.setPoints(10);
        SustenanceEvent event = new SustenanceEvent(3, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe));

        assertEquals(0, peppe.getFood());      // all food paid
        // 3 characters unfed * 3 minusPoints = 9 lost
        assertEquals(1, peppe.getPoints());    // 10 - 9
    }

    @Test
    void noCharactersNoEffect() {
        peppe.setFood(5);
        peppe.setPoints(5);
        SustenanceEvent event = new SustenanceEvent(2, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe));

        assertEquals(5, peppe.getFood());
        assertEquals(5, peppe.getPoints());
    }

    @Test
    void eventCardFlags() {
        SustenanceEvent event = new SustenanceEvent(2, manager, Era.FIRST, 2, null);
        assertFalse(event.canGet(peppe));
        assertFalse(event.isLowerLineOnSetup());
    }

    @Test
    void simpleString() {
        SustenanceEvent event = new SustenanceEvent(2, manager, Era.FIRST, 2, null);
        assertEquals("SOSTENTAMENTO", event.simpleToString());
    }
}
