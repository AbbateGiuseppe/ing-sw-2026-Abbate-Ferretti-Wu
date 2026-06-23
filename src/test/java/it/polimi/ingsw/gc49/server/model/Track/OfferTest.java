package it.polimi.ingsw.gc49.server.model.Track;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOffer;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.playerExceptions.NotValidOfferException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OfferTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    @Test
    @DisplayName("a fresh Offer has no assigned player")
    void freshOfferHasNoPlayer() {
        Offer offer = new Offer(1, 2, 0);
        assertNull(offer.getAssignedPlayer());
    }

    @Test
    @DisplayName("assignPlayer stores the given player")
    void assignPlayerStoresPlayer() throws NotValidOfferException {
        Offer offer = new Offer(0, 1, 1);
        offer.assignPlayer(peppe);
        assertSame(peppe, offer.getAssignedPlayer());
    }

    @Test
    @DisplayName("assignPlayer on an already occupied offer throws NotValidOfferException")
    void assignPlayerOnOccupiedThrows() throws NotValidOfferException {
        Offer offer = new Offer(0, 1, 1);
        offer.assignPlayer(peppe);

        Player wu = new Player("Wu", 1);
        assertThrows(NotValidOfferException.class, () -> offer.assignPlayer(wu));
        // and the original player must still be there
        assertSame(peppe, offer.getAssignedPlayer());
    }

    @Test
    @DisplayName("deassignPlayer clears the assigned player and resets their remaining actions")
    void deassignPlayerClearsAndResets() throws NotValidOfferException {
        Offer offer = new Offer(0, 2, 1);
        peppe.setDrawableUpper(3);
        peppe.setDrawableLower(2);
        offer.assignPlayer(peppe);

        offer.deassignPlayer();

        assertNull(offer.getAssignedPlayer());
        assertEquals(0, peppe.getDrawableUpper(), "cleanRemainingActions zeroes the upper counter");
        assertEquals(0, peppe.getDrawableLower(), "cleanRemainingActions zeroes the lower counter");
    }

    @Test
    @DisplayName("activate grants food and sets the upper/lower draw counters on the assigned player")
    void activateAppliesEffects() throws NotValidOfferException {
        Offer offer = new Offer(3, 1, 2);
        offer.assignPlayer(peppe);

        offer.activate();

        assertEquals(3, peppe.getFood());
        assertEquals(1, peppe.getDrawableUpper());
        assertEquals(2, peppe.getDrawableLower());
    }

    @Test
    @DisplayName("activate overwrites the draw counters (does not stack)")
    void activateOverwritesDrawCounters() throws NotValidOfferException {
        Offer offer = new Offer(0, 1, 1);
        peppe.setDrawableUpper(5);
        peppe.setDrawableLower(5);
        offer.assignPlayer(peppe);

        offer.activate();

        assertEquals(1, peppe.getDrawableUpper());
        assertEquals(1, peppe.getDrawableLower());
    }

    @Test
    @DisplayName("activate adds food (does not overwrite the player's current food)")
    void activateAddsFood() throws NotValidOfferException {
        Offer offer = new Offer(2, 0, 0);
        peppe.setFood(5);
        offer.assignPlayer(peppe);

        offer.activate();

        assertEquals(7, peppe.getFood());
    }

    @Test
    @DisplayName("giveOfferMockup returns a mockup whose ownerless when no player is assigned")
    void mockupWithoutPlayer() {
        Offer offer = new Offer(0, 1, 1);
        MockupOffer mockup = offer.giveOfferMockup();
        assertNotNull(mockup);
    }

    @Test
    @DisplayName("giveOfferMockup carries the assigned player's index when present")
    void mockupWithAssignedPlayer() throws NotValidOfferException {
        Offer offer = new Offer(0, 1, 1);
        offer.assignPlayer(peppe);
        MockupOffer mockup = offer.giveOfferMockup();
        assertNotNull(mockup);
    }
}
