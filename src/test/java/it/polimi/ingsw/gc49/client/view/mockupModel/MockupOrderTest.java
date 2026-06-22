package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.Totem;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockupOrderTest {

    private static MockupGame gameWithSinglePlayer() {
        MockupPlayer peppe = new MockupPlayer("Peppe", 0, 0, 0, Totem.BLUE);
        List<MockupPlayer> players = new ArrayList<>();
        players.add(peppe);
        return new MockupGame(players, null, List.of(), List.of(), List.of(), List.of(), List.of(), List.of());
    }

    @Test
    @DisplayName("constructor stores the assigned player index")
    void constructorStoresFields() {
        MockupOrder order = new MockupOrder(2, false, 0, 0, 7);
        assertEquals(7, order.getAssignedPlayerIndex());
    }

    @Test
    @DisplayName("setAssignedPlayerIndex round trip (including null)")
    void setAssignedPlayerIndex() {
        MockupOrder order = new MockupOrder(2, false, 0, 0, null);
        assertNull(order.getAssignedPlayerIndex());
        order.setAssignedPlayerIndex(1);
        assertEquals(1, order.getAssignedPlayerIndex());
        order.setAssignedPlayerIndex(null);
        assertNull(order.getAssignedPlayerIndex());
    }

    @Test
    @DisplayName("rectangle is 1x8 for an unassigned food-gaining slot")
    void rectangleEmptyFoodSlot() {
        MockupOrder order = new MockupOrder(2, false, 0, 0, null);
        RectangleAttributedString rect = order.getRectangleAttributedString();
        assertNotNull(rect);
        assertEquals(1, rect.height);
        assertEquals(8, rect.width);
    }

    @Test
    @DisplayName("rectangle renders for an unassigned no-food slot")
    void rectangleEmptyNoFoodSlot() {
        MockupOrder order = new MockupOrder(0, false, 0, 0, null);
        assertNotNull(order.getRectangleAttributedString());
    }

    @Test
    @DisplayName("rectangle renders for an unassigned pay-food slot")
    void rectangleEmptyPaySlot() {
        MockupOrder order = new MockupOrder(0, true, 1, 2, null);
        assertNotNull(order.getRectangleAttributedString());
    }

    @Test
    @DisplayName("rectangle renders with an assigned player (uses the game lookup for totem colour)")
    void rectangleWithAssignedPlayer() {
        MockupOrder order = new MockupOrder(2, false, 0, 0, 0);
        order.setGame(gameWithSinglePlayer());
        assertNotNull(order.getRectangleAttributedString());
    }
}
