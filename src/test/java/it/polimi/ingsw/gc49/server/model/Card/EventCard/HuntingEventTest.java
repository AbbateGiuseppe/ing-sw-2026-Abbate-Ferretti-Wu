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

class HuntingEventTest {

    private Player peppe;
    private Player peppeTwo;
    private EventManager manager;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
        peppeTwo = new Player("Peppe", 1);
        manager = new EventManager();
    }

    @Test
    void resolveAwardsFoodAndPoints() {
        peppe.data.addCharacterCount(CharacterType.Hunter, 3);
        HuntingEvent event = new HuntingEvent(2, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe));

        assertEquals(3, peppe.getFood());   // 1 * 3 hunters
        assertEquals(6, peppe.getPoints()); // 2 * 3 hunters
    }

    @Test
    void noHuntersNoReward() {
        HuntingEvent event = new HuntingEvent(2, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe));

        assertEquals(0, peppe.getFood());
        assertEquals(0, peppe.getPoints());
    }

    @Test
    void independentRewards() {
        peppe.data.addCharacterCount(CharacterType.Hunter, 2);
        peppeTwo.data.addCharacterCount(CharacterType.Hunter, 1);
        HuntingEvent event = new HuntingEvent(3, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe, peppeTwo));

        assertEquals(2, peppe.getFood());
        assertEquals(6, peppe.getPoints());
        assertEquals(1, peppeTwo.getFood());
        assertEquals(3, peppeTwo.getPoints());
    }

    @Test
    void resetsPendingState() {
        peppe.data.addCharacterCount(CharacterType.Hunter, 1);
        HuntingEvent event = new HuntingEvent(2, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe));

        assertEquals(0, peppe.getFoodToPay());
        assertEquals(0, peppe.getPointsToPay());
    }

    @Test
    void eventCardFlags() {
        HuntingEvent event = new HuntingEvent(2, manager, Era.FIRST, 2, null);
        assertFalse(event.canGet(peppe));
        assertFalse(event.isLowerLineOnSetup());
    }

    @Test
    void simpleString() {
        HuntingEvent event = new HuntingEvent(2, manager, Era.FIRST, 2, null);
        assertEquals("CACCIA", event.simpleToString());
    }
}
