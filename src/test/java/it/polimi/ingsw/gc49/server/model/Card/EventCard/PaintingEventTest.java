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

class PaintingEventTest {

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
    @DisplayName("below threshold: player loses a flat minusPoints penalty")
    void belowThresholdLosesFlatPenalty() {
        peppe.data.addCharacterCount(CharacterType.Artist, 1); // threshold = 2
        PaintingEvent event = new PaintingEvent(2, 3, 4, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe));

        assertEquals(-4, peppe.getPoints());
    }

    @Test
    @DisplayName("at threshold: player gains plusPoints per artist")
    void atThresholdGainsPerArtist() {
        peppe.data.addCharacterCount(CharacterType.Artist, 2); // exactly the threshold
        PaintingEvent event = new PaintingEvent(2, 3, 4, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe));

        assertEquals(6, peppe.getPoints()); // 3 * 2 artists
    }

    @Test
    @DisplayName("above threshold: gain scales linearly with the artist count")
    void aboveThresholdScales() {
        peppe.data.addCharacterCount(CharacterType.Artist, 4);
        PaintingEvent event = new PaintingEvent(2, 3, 4, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe));

        assertEquals(12, peppe.getPoints()); // 3 * 4 artists
    }

    @Test
    @DisplayName("zero artists below threshold means the flat penalty applies")
    void zeroArtistsLosesPenalty() {
        PaintingEvent event = new PaintingEvent(1, 3, 4, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe));

        assertEquals(-4, peppe.getPoints());
    }

    @Test
    @DisplayName("the event handles winners and losers independently in one call")
    void mixedOutcomesInOneCall() {
        peppe.data.addCharacterCount(CharacterType.Artist, 3);    // success
        peppeTwo.data.addCharacterCount(CharacterType.Artist, 0); // failure
        PaintingEvent event = new PaintingEvent(2, 3, 4, manager, Era.FIRST, 2, null);

        event.resolveEvent(List.of(peppe, peppeTwo));

        assertEquals(9, peppe.getPoints());     // 3 * 3
        assertEquals(-4, peppeTwo.getPoints()); // flat penalty
    }

    @Test
    @DisplayName("canGet is false and isLowerLineOnSetup is false")
    void eventCardFlags() {
        PaintingEvent event = new PaintingEvent(2, 3, 4, manager, Era.FIRST, 2, null);
        assertFalse(event.canGet(peppe));
        assertFalse(event.isLowerLineOnSetup());
    }

    @Test
    @DisplayName("simpleToString is the localized name")
    void simpleString() {
        PaintingEvent event = new PaintingEvent(2, 3, 4, manager, Era.FIRST, 2, null);
        assertEquals("PITTURE RUPESTRI", event.simpleToString());
    }
}
