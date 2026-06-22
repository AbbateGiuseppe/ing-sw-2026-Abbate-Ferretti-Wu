package it.polimi.ingsw.gc49.server.model;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("Massi", 2);
    }


    // ============================================================
    // IN-GAME FEATURES
    // ============================================================
    @Test
    @DisplayName("connection flag can be toggled")
    void connectionToggle() {
        assertTrue(player.isConnected());
        player.setConnected(false);
        assertFalse(player.isConnected());
        player.setConnected(true);
        assertTrue(player.isConnected());
    }

    @Test
    @DisplayName("removedFromTrack flag can be toggled")
    void removedFromTrackToggle() {
        assertFalse(player.isRemovedFromTrack());
        player.setRemovedFromTrack(true);
        assertTrue(player.isRemovedFromTrack());
    }

    @Test
    @DisplayName("choseAnOffer flag can be toggled")
    void chosenOfferToggle() {
        assertFalse(player.hasChosenAnOffer());
        player.setChoseAnOffer(true);
        assertTrue(player.hasChosenAnOffer());
    }

    @Test
    @DisplayName("hasActionsLeft is true while any drawable counter is positive")
    void hasActionsLeft() {
        assertFalse(player.hasActionsLeft(), "no actions when both counters are zero");

        player.setDrawableUpper(1);
        assertTrue(player.hasActionsLeft());

        player.setDrawableUpper(0);
        player.setDrawableLower(1);
        assertTrue(player.hasActionsLeft());

        player.setDrawableUpper(2);
        player.setDrawableLower(3);
        assertTrue(player.hasActionsLeft());
    }

    @Test
    @DisplayName("cleanRemainingActions zeroes both drawable counters")
    void cleanRemainingActions() {
        player.setDrawableUpper(4);
        player.setDrawableLower(5);
        player.cleanRemainingActions();
        assertEquals(0, player.getDrawableUpper());
        assertEquals(0, player.getDrawableLower());
        assertFalse(player.hasActionsLeft());
    }

    @Test
    @DisplayName("foodToPay / pointsToPay / uniqueWinner getters and setters")
    void eventManagementAccessors() {
        player.setFoodToPay(6);
        player.setPointsToPay(4);
        player.setUniqueWinner(true);
        assertEquals(6, player.getFoodToPay());
        assertEquals(4, player.getPointsToPay());
        assertTrue(player.isUniqueWinner());
    }

    @Test
    @DisplayName("confirmToPay subtracts the pending food and points and resets the pending state")
    void confirmToPaySubtractsAndResets() {
        player.setFood(10);
        player.setPoints(10);
        player.setFoodToPay(3);
        player.setPointsToPay(2);
        player.setUniqueWinner(true);

        player.confirmToPay();

        assertEquals(7, player.getFood());
        assertEquals(8, player.getPoints());
        // reset() clears the pending amounts and the winner flag
        assertEquals(0, player.getFoodToPay());
        assertEquals(0, player.getPointsToPay());
        assertFalse(player.isUniqueWinner());
    }

    @Test
    @DisplayName("getNumOfConnectedPlayers-like state: giveMockupPlayer reflects current state")
    void giveMockupPlayerReflectsState() {
        player.setFood(8);
        player.setPoints(3);
        player.setTotem(Totem.YELLOW);

        MockupPlayer mockup = player.giveMockupPlayer();

        assertNotNull(mockup);
        assertEquals("Massi", mockup.getNickname());
        assertEquals(2, mockup.getPlayerIndex());
        assertEquals(8, mockup.getFood());
        assertEquals(3, mockup.getPoints());
        assertEquals(Totem.YELLOW, mockup.getTotem());
    }

    // ============================================================
    // ### GETTERS VERIFICATION
    // ============================================================
    @Test
    @DisplayName("Constructor initializes fields with expected defaults")
    void constructorInitializesDefaults() {
        assertEquals("Massi", player.getNickname());
        assertEquals(2, player.getPlayerIndex());
        assertEquals(0, player.getFood());
        assertEquals(0, player.getPoints());
        assertEquals(0, player.getDrawableUpper());
        assertEquals(0, player.getDrawableLower());
        assertNull(player.getTotem());
        assertNull(player.getAssignedOrderSlot());
        assertTrue(player.isConnected());
        assertFalse(player.isRemovedFromTrack());
        assertFalse(player.hasChosenAnOffer());
        assertNotNull(player.data);
        assertSame(player, player.data.assignedPlayer);
    }


    // ============================================================
    // ### ADDERS VERIFICATION (POINTS AND FOOD)
    // ============================================================
    @Test
    @DisplayName("addFood accumulates food, including negative amounts")
    void addFoodAccumulates() {
        player.addFood(5);
        assertEquals(5, player.getFood());
        player.addFood(-2);
        assertEquals(3, player.getFood());
    }

    @Test
    @DisplayName("addPoints accumulates points, including negative amounts")
    void addPointsAccumulates() {
        player.addPoints(10);
        assertEquals(10, player.getPoints());
        player.addPoints(-4);
        assertEquals(6, player.getPoints());
    }

    @Test
    @DisplayName("setFood and setPoints overwrite the current values")
    void settersOverwrite() {
        player.addFood(5);
        player.setFood(20);
        assertEquals(20, player.getFood());

        player.addPoints(5);
        player.setPoints(7);
        assertEquals(7, player.getPoints());
    }


    // ============================================================
    // ### SETTERS VERIFICATION
    // ============================================================
    @Test
    @DisplayName("setDrawableUpper and setDrawableLower set the drawable counters")
    void setDrawables() {
        player.setDrawableUpper(3);
        player.setDrawableLower(2);
        assertEquals(3, player.getDrawableUpper());
        assertEquals(2, player.getDrawableLower());
    }

    @Test
    @DisplayName("setTotem and getTotem round-trip")
    void totemRoundTrip() {
        player.setTotem(Totem.BLUE);
        assertEquals(Totem.BLUE, player.getTotem());
    }
}
