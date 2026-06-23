package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleBuilderPointsCardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    @Test
    void updateDataBankAddsPoints() {
        DoubleBuilderPointsCard card = new DoubleBuilderPointsCard(BuildingEvent.GAME_END, 4, 5, Era.FIRST, 2, null);
        card.updateDataBank(peppe.data);
        assertEquals(4, peppe.data.getNumBuildingPoints());
    }

    @Test
    void onEventEffectDoublesBuilderPoints() {
        DoubleBuilderPointsCard card = new DoubleBuilderPointsCard(BuildingEvent.GAME_END, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe); // owner = peppe
        peppe.data.addNumBuilderPoints(6);

        card.onEventEffect();

        assertEquals(12, peppe.data.getNumBuilderPoints());
    }

    @Test
    void onEventEffectZeroStaysZero() {
        DoubleBuilderPointsCard card = new DoubleBuilderPointsCard(BuildingEvent.GAME_END, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);

        card.onEventEffect();

        assertEquals(0, peppe.data.getNumBuilderPoints());
    }

    @Test
    void simpleString() {
        DoubleBuilderPointsCard card = new DoubleBuilderPointsCard(BuildingEvent.GAME_END, 0, 5, Era.FIRST, 2, null);
        assertEquals("EDIFICIO (doppipunti da costruttori)", card.simpleToString());
    }

    @Test
    void onEventEffectNotifiesQueue() {
        FakeQueueUpdatable queue = new FakeQueueUpdatable();
        DoubleBuilderPointsCard card = new DoubleBuilderPointsCard(BuildingEvent.GAME_END, 0, 5, Era.FIRST, 2, queue);
        peppe.setFood(10);
        card.onDraw(peppe);
        peppe.data.addNumBuilderPoints(3);

        card.onEventEffect();

        assertEquals(6, peppe.data.getNumBuilderPoints());
        assertEquals(1, queue.queued.size(), "a model update must be queued when an updater is present");
    }

    @Test
    void rectangleAttributedString() {
        DoubleBuilderPointsCard card = new DoubleBuilderPointsCard(BuildingEvent.GAME_END, 4, 6, Era.FIRST, 2, null);
        var rect = card.getRectangleAttributedString();
        assertNotNull(rect);
        assertEquals(2, rect.height);
        assertEquals(10, rect.width);
    }

    @Test
    void itaEngRectangleAttributedString() {
        DoubleBuilderPointsCard card = new DoubleBuilderPointsCard(BuildingEvent.GAME_END, 4, 6, Era.FIRST, 2, null);
        var itaEng = card.getItaEngRectangleAttributedString();
        assertNotNull(itaEng);
        assertNotNull(itaEng.itaRectangleAttributedString);
        assertNotNull(itaEng.engRectangleAttributedString);
        assertEquals(2, itaEng.itaRectangleAttributedString.height);
        assertEquals(10, itaEng.engRectangleAttributedString.width);
    }

    @Test
    void toStringNotEmpty() {
        DoubleBuilderPointsCard card = new DoubleBuilderPointsCard(BuildingEvent.GAME_END, 4, 6, Era.FIRST, 2, null);
        String s = card.toString();
        assertNotNull(s);
        assertFalse(s.isBlank());
    }
}
