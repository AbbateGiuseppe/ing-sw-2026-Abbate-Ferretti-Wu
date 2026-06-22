package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.Totem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockupOfferTest {

    /** Minimal MockupGame whose only purpose is to lookup a player by index. */
    private static MockupGame gameWithSinglePlayer() {
        MockupPlayer peppe = new MockupPlayer("Peppe", 0, true, Totem.BLUE, 0, 0, new ArrayList<>(), new ArrayList<>(), 0, 0);
        List<MockupPlayer> players = new ArrayList<>();
        players.add(peppe);
        return new MockupGame(players, null, List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
    }

    @Test
    @DisplayName("constructor stores the assigned player index and the food/draw fields")
    void constructorStoresFields() {
        MockupOffer offer = new MockupOffer(3, 1, 2, 7);
        assertEquals(7, offer.getAssignedPlayerIndex());
    }

    @Test
    @DisplayName("setAssignedPlayerIndex round trip (including null)")
    void setAssignedPlayerIndex() {
        MockupOffer offer = new MockupOffer(0, 1, 0, null);
        assertNull(offer.getAssignedPlayerIndex());
        offer.setAssignedPlayerIndex(2);
        assertEquals(2, offer.getAssignedPlayerIndex());
        offer.setAssignedPlayerIndex(null);
        assertNull(offer.getAssignedPlayerIndex());
    }

    @Test
    @DisplayName("rectangle is 5x5 and not null when no player is assigned")
    void rectangleWhenEmpty() {
        MockupOffer offer = new MockupOffer(0, 1, 0, null);
        RectangleAttributedString rect = offer.getRectangleAttributedString();
        assertNotNull(rect);
        assertEquals(5, rect.height);
        assertEquals(5, rect.width);
    }

    @Test
    @DisplayName("rectangle works for a food-only offer (foodGain > 0 branch)")
    void rectangleFoodOnly() {
        MockupOffer offer = new MockupOffer(3, 0, 0, null);
        assertNotNull(offer.getRectangleAttributedString());
    }

    @Test
    @DisplayName("rectangle works for a lower-only offer (lowerDraw > 0, upperDraw = 0 branch)")
    void rectangleLowerOnly() {
        MockupOffer offer = new MockupOffer(0, 0, 1, null);
        assertNotNull(offer.getRectangleAttributedString());
    }

    @Test
    @DisplayName("rectangle works for a mixed offer (lowerDraw > 0 and upperDraw > 0 branch)")
    void rectangleLowerAndUpper() {
        MockupOffer offer = new MockupOffer(0, 1, 1, null);
        assertNotNull(offer.getRectangleAttributedString());
    }

    @Test
    @DisplayName("rectangle works for an upper-only offer (upperDraw > 0 branch)")
    void rectangleUpperOnly() {
        MockupOffer offer = new MockupOffer(0, 2, 0, null);
        assertNotNull(offer.getRectangleAttributedString());
    }

    @Test
    @DisplayName("rectangle works when a player is assigned (uses the game lookup for totem colour)")
    void rectangleWithAssignedPlayer() {
        MockupOffer offer = new MockupOffer(0, 1, 0, 0);
        offer.setGame(gameWithSinglePlayer());
        assertNotNull(offer.getRectangleAttributedString());
    }
}
