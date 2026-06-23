package it.polimi.ingsw.gc49.server.model.Card.EventCard;

import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.EventManager;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RitualEventTest {

    private Player peppe;
    private Player Wu;
    private Player Massi;
    private EventManager manager;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
        Wu = new Player("Wu", 1);
        Massi = new Player("Massi", 2);
        manager = new EventManager();
    }

    @Test
    void uniqueWinnerAndLoser() {
        peppe.data.addNumStar(5);       // top stars -> winner
        Wu.data.addNumStar(3);    // middle
        Massi.data.addNumStar(1);  // bottom -> loser
        RitualEvent event = new RitualEvent(4, 2, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe, Wu, Massi));

        assertEquals(4, peppe.getPoints(), "winner gains plusPoints");
        assertEquals(0, Wu.getPoints(), "middle player untouched");
        assertEquals(-2, Massi.getPoints(), "loser loses minusPoints");
        assertTrue(peppe.isUniqueWinner() == false, "uniqueWinner flag is reset by confirmToPay");
    }

    @Test
    void tiedWinnersAllGain() {
        peppe.data.addNumStar(5);
        Wu.data.addNumStar(5);
        Massi.data.addNumStar(1);
        RitualEvent event = new RitualEvent(4, 2, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe, Wu, Massi));

        assertEquals(4, peppe.getPoints());
        assertEquals(4, Wu.getPoints());
        assertEquals(-2, Massi.getPoints());
    }

    @Test
    void tiedLosersAllLose() {
        peppe.data.addNumStar(5);
        Wu.data.addNumStar(1);
        Massi.data.addNumStar(1);
        RitualEvent event = new RitualEvent(4, 2, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe, Wu, Massi));

        assertEquals(4, peppe.getPoints());
        assertEquals(-2, Wu.getPoints());
        assertEquals(-2, Massi.getPoints());
    }

    @Test
    void allTiedEveryoneAffected() {
        // all same stars: winners == losers == all players
        // code path: losers set pointsToPay=minusPoints, then winners overwrite pointsToPay=-plusPoints
        RitualEvent event = new RitualEvent(4, 2, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe, Wu, Massi));

        // every player both qualifies as winner and loser; the winner branch runs after the loser branch
        // so pointsToPay ends up at -plusPoints and confirmToPay -> +plusPoints
        assertEquals(4, peppe.getPoints());
        assertEquals(4, Wu.getPoints());
        assertEquals(4, Massi.getPoints());
    }

    @Test
    void eventCardFlags() {
        RitualEvent event = new RitualEvent(4, 2, manager, Era.FIRST, 2, null);
        assertFalse(event.canGet(peppe));
        assertFalse(event.isLowerLineOnSetup());
    }

    @Test
    void simpleString() {
        RitualEvent event = new RitualEvent(4, 2, manager, Era.FIRST, 2, null);
        assertEquals("RITUALE SCIAMANICO", event.simpleToString());
    }
}
