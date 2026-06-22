package it.polimi.ingsw.gc49.server.model.Track;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOrder;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderSlotTest {

    @Test
    @DisplayName("Empty constructor produces a no-effect slot")
    void emptyConstructor() {
        OrderSlot slot = new OrderSlot();
        assertEquals(0, slot.getFoodGain());
        assertNull(slot.getAssignedPlayer());
    }

    @Test
    @DisplayName("Food-gaining constructor stores the food gain")
    void foodGainConstructor() {
        OrderSlot slot = new OrderSlot(4);
        assertEquals(4, slot.getFoodGain());
    }

    @Test
    @DisplayName("assignPlayer links the slot to the player and back")
    void assignPlayerLinksBothWays() {
        OrderSlot slot = new OrderSlot(2);
        Player player = new Player("Peppe", 1);

        slot.assignPlayer(player);

        assertSame(player, slot.getAssignedPlayer());
        assertSame(slot, player.getAssignedOrderSlot());
    }

    @Test
    @DisplayName("assignPlayer(null) deassigns the current occupant")
    void assignNullDeassigns() {
        OrderSlot slot = new OrderSlot(2);
        Player player = new Player("Peppe", 1);
        slot.assignPlayer(player);

        slot.assignPlayer(null);

        assertNull(slot.getAssignedPlayer());
    }

    @Test
    @DisplayName("effectOnOccupation on a food-gaining slot adds food to the occupant")
    void effectFoodGain() {
        OrderSlot slot = new OrderSlot(3);
        Player player = new Player("Peppe", 1);
        slot.assignPlayer(player);

        slot.effectOnOccupation();

        assertEquals(3, player.getFood());
    }

    @Test
    @DisplayName("effectOnOccupation on a paying slot deducts food when the occupant can afford it")
    void effectPayWhenAffordable() {
        OrderSlot slot = new OrderSlot(2, 5); // foodToPay=2, removedPointsOnStarvation=5
        Player player = new Player("Peppe", 1);
        player.setFood(10);
        player.setPoints(8);
        slot.assignPlayer(player);

        slot.effectOnOccupation();

        assertEquals(8, player.getFood(), "food reduced by the price");
        assertEquals(8, player.getPoints(), "points untouched when payment succeeds");
    }

    @Test
    @DisplayName("effectOnOccupation on a paying slot removes points when the occupant starves")
    void effectStarvationRemovesPoints() {
        OrderSlot slot = new OrderSlot(5, 3); // foodToPay=5, removedPointsOnStarvation=3
        Player player = new Player("Peppe", 1);
        player.setFood(2); // cannot afford 5
        player.setPoints(10);
        slot.assignPlayer(player);

        slot.effectOnOccupation();

        assertEquals(2, player.getFood(), "food untouched when starving");
        assertEquals(7, player.getPoints(), "points reduced by the starvation penalty");
    }

    @Test
    @DisplayName("giveOrderMockup carries the player index when a player is assigned")
    void mockupWithAssignedPlayer() {
        OrderSlot slot = new OrderSlot(3);
        Player player = new Player("Peppe", 4);
        slot.assignPlayer(player);

        MockupOrder mockup = slot.giveOrderMockup();
        assertNotNull(mockup);
    }

    @Test
    @DisplayName("giveOrderMockup works with no player assigned")
    void mockupWithoutPlayer() {
        OrderSlot slot = new OrderSlot(3);
        assertNotNull(slot.giveOrderMockup());
    }
}
