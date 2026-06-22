package it.polimi.ingsw.gc49.server.model.Card.EventCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.EventManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Covers the terminal-rendering methods and {@code toString} for every concrete
 * {@link EventCard}. Each event has up to three era variants — the cards used here cover
 * every era for every event so any era-specific rendering branches are reached.
 */
class EventCardRenderingTest {

    private static final EventManager MGR = new EventManager();

    private static List<Supplier<EventCard>> allCards() {
        return List.of(
                () -> new HuntingEvent(1, MGR, Era.FIRST, 3, null),
                () -> new HuntingEvent(2, MGR, Era.SECOND, 3, null),
                () -> new HuntingEvent(3, MGR, Era.THIRD, 3, null),
                () -> new RitualEvent(5, 3, MGR, Era.FIRST, 2, null),
                () -> new RitualEvent(10, 5, MGR, Era.SECOND, 2, null),
                () -> new RitualEvent(15, 7, MGR, Era.THIRD_FINAL, 2, null),
                () -> new PaintingEvent(1, 1, 2, MGR, Era.FIRST, 2, null),
                () -> new PaintingEvent(2, 2, 2, MGR, Era.SECOND, 2, null),
                () -> new PaintingEvent(3, 3, 2, MGR, Era.THIRD, 2, null),
                () -> new SustenanceEvent(1, MGR, Era.FIRST, 2, null),
                () -> new SustenanceEvent(2, MGR, Era.SECOND, 2, null),
                () -> new SustenanceEvent(3, MGR, Era.THIRD_FINAL, 2, null)
        );
    }

    @Test
    @DisplayName("getRectangleAttributedString returns a 4x5 drawing for every event")
    void rectangleForEveryEvent() {
        for (Supplier<EventCard> supplier : allCards()) {
            EventCard card = supplier.get();
            RectangleAttributedString rect = card.getRectangleAttributedString();
            assertNotNull(rect, card.getClass().getSimpleName() + " rectangle must not be null");
            assertEquals(4, rect.height);
            assertEquals(5, rect.width);
        }
    }

    @Test
    @DisplayName("getItaEngRectangleAttributedString returns ITA and ENG drawings for every event")
    void itaEngForEveryEvent() {
        for (Supplier<EventCard> supplier : allCards()) {
            EventCard card = supplier.get();
            ItaEngRectangleAttributedString itaEng = card.getItaEngRectangleAttributedString();
            assertNotNull(itaEng);
            assertNotNull(itaEng.itaRectangleAttributedString);
            assertNotNull(itaEng.engRectangleAttributedString);
            assertEquals(4, itaEng.itaRectangleAttributedString.height);
            assertEquals(5, itaEng.itaRectangleAttributedString.width);
            assertEquals(4, itaEng.engRectangleAttributedString.height);
            assertEquals(5, itaEng.engRectangleAttributedString.width);
        }
    }

    @Test
    @DisplayName("toString is non-blank for every event")
    void toStringForEveryEvent() {
        for (Supplier<EventCard> supplier : allCards()) {
            EventCard card = supplier.get();
            String s = card.toString();
            assertNotNull(s);
            assertFalse(s.isBlank());
        }
    }

    @Test
    @DisplayName("simpleToString is non-blank for every event")
    void simpleToStringForEveryEvent() {
        for (Supplier<EventCard> supplier : allCards()) {
            EventCard card = supplier.get();
            String s = card.simpleToString();
            assertNotNull(s);
            assertFalse(s.isBlank());
        }
    }
}
