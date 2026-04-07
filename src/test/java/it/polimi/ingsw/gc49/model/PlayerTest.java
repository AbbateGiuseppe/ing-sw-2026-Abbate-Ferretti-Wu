package it.polimi.ingsw.gc49.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    private Player player;

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer", 0);
    }

    @Test
    void testPlayerInitialization() {
        assertEquals("TestPlayer", player.getNickname());
        assertEquals(0, player.getPlayerIndex());
        assertEquals(0, player.getFood());
        assertEquals(0, player.getPoints());
        assertTrue(player.isConnected());
        assertFalse(player.isRemovedFromTrack());
        assertNull(player.getTotem());
    }

    @Test
    void testAddFood() {
        player.addFood(5);
        assertEquals(5, player.getFood());

        player.addFood(3);
        assertEquals(8, player.getFood());
    }

    @Test
    void testAddPoints() {
        player.addPoints(10);
        assertEquals(10, player.getPoints());

        player.addPoints(5);
        assertEquals(15, player.getPoints());
    }

    @Test
    void testSetFood() {
        player.setFood(20);
        assertEquals(20, player.getFood());
    }

    @Test
    void testSetPoints() {
        player.setPoints(50);
        assertEquals(50, player.getPoints());
    }

    @Test
    void testSetTotem() {
        player.setTotem(Totem.EAGLE);
        assertEquals(Totem.EAGLE, player.getTotem());
    }

    @Test
    void testDrawableUpperAndLower() {
        assertEquals(0, player.getDrawableUpper());
        assertEquals(0, player.getDrawableLower());

        player.setDrawableUpper(2);
        player.setDrawableLower(1);

        assertEquals(2, player.getDrawableUpper());
        assertEquals(1, player.getDrawableLower());
    }

    @Test
    void testHasActionsLeft_noActions() {
        player.setDrawableUpper(0);
        player.setDrawableLower(0);
        assertFalse(player.hasActionsLeft());
    }

    @Test
    void testHasActionsLeft_withUpperActions() {
        player.setDrawableUpper(1);
        player.setDrawableLower(0);
        assertTrue(player.hasActionsLeft());
    }

    @Test
    void testHasActionsLeft_withLowerActions() {
        player.setDrawableUpper(0);
        player.setDrawableLower(1);
        assertTrue(player.hasActionsLeft());
    }

    @Test
    void testHasActionsLeft_withBothActions() {
        player.setDrawableUpper(2);
        player.setDrawableLower(1);
        assertTrue(player.hasActionsLeft());
    }

    @Test
    void testCleanRemainingActions() {
        player.setDrawableUpper(3);
        player.setDrawableLower(2);

        player.cleanRemainingActions();

        assertEquals(0, player.getDrawableUpper());
        assertEquals(0, player.getDrawableLower());
        assertFalse(player.hasActionsLeft());
    }

    @Test
    void testConnectedStatus() {
        assertTrue(player.isConnected());

        player.setConnected(false);
        assertFalse(player.isConnected());

        player.setConnected(true);
        assertTrue(player.isConnected());
    }

    @Test
    void testRemovedFromTrackStatus() {
        assertFalse(player.isRemovedFromTrack());

        player.setRemovedFromTrack(true);
        assertTrue(player.isRemovedFromTrack());

        player.setRemovedFromTrack(false);
        assertFalse(player.isRemovedFromTrack());
    }

    @Test
    void testChoseAnOffer() {
        assertFalse(player.hasChosenAnOffer());

        player.setChoseAnOffer(true);
        assertTrue(player.hasChosenAnOffer());

        player.setChoseAnOffer(false);
        assertFalse(player.hasChosenAnOffer());
    }

    @Test
    void testFoodToPay() {
        assertEquals(0, player.getFoodToPay());

        player.setFoodToPay(5);
        assertEquals(5, player.getFoodToPay());
    }

    @Test
    void testPointsToPay() {
        assertEquals(0, player.getPointsToPay());

        player.setPointsToPay(10);
        assertEquals(10, player.getPointsToPay());
    }

    @Test
    void testUniqueWinner() {
        assertFalse(player.isUniqueWinner());

        player.setUniqueWinner(true);
        assertTrue(player.isUniqueWinner());
    }

    @Test
    void testConfirmToPay() {
        player.setFood(20);
        player.setPoints(50);
        player.setFoodToPay(5);
        player.setPointsToPay(10);
        player.setUniqueWinner(true);

        player.confirmToPay();

        assertEquals(15, player.getFood(), "Food should be reduced by foodToPay");
        assertEquals(40, player.getPoints(), "Points should be reduced by pointsToPay");
        assertEquals(0, player.getFoodToPay(), "FoodToPay should be reset to 0");
        assertEquals(0, player.getPointsToPay(), "PointsToPay should be reset to 0");
        assertFalse(player.isUniqueWinner(), "UniqueWinner should be reset to false");
    }

    @Test
    void testDataBankInitialization() {
        assertNotNull(player.data, "DataBank should be initialized");
    }

    @Test
    void testMultiplePlayerInstances() {
        Player player1 = new Player("Player1", 0);
        Player player2 = new Player("Player2", 1);
        Player player3 = new Player("Player3", 2);

        assertEquals("Player1", player1.getNickname());
        assertEquals("Player2", player2.getNickname());
        assertEquals("Player3", player3.getNickname());

        assertEquals(0, player1.getPlayerIndex());
        assertEquals(1, player2.getPlayerIndex());
        assertEquals(2, player3.getPlayerIndex());
    }
}
