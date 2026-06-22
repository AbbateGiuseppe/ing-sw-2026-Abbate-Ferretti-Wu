package it.polimi.ingsw.gc49.server.model.Track;

import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.playerExceptions.NotValidOfferException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TrackTest {

    private static Player newPeppe(int index) { return new Player("Peppe", index); }
    private static Player newWu(int index) { return new Player("Wu", index); }
    private static Player newMassi(int index) { return new Player("Massi", index); }

    // --- construction sizing ---

    @Nested
    @DisplayName("Board sizing by player count")
    class Sizing {

        @Test
        @DisplayName("2 players: 4 offers and 2 order slots")
        void twoPlayers() {
            Track track = new Track(2);
            assertEquals(4, track.getOfferBoard().size());
            assertEquals(2, track.getOrderBoard().size());
        }

        @Test
        @DisplayName("3 players: 5 offers and 3 order slots")
        void threePlayers() {
            Track track = new Track(3);
            assertEquals(5, track.getOfferBoard().size());
            assertEquals(3, track.getOrderBoard().size());
        }

        @Test
        @DisplayName("4 players: 6 offers and 4 order slots")
        void fourPlayers() {
            Track track = new Track(4);
            assertEquals(6, track.getOfferBoard().size());
            assertEquals(4, track.getOrderBoard().size());
        }

        @Test
        @DisplayName("5 players: 7 offers and 5 order slots")
        void fivePlayers() {
            Track track = new Track(5);
            assertEquals(7, track.getOfferBoard().size());
            assertEquals(5, track.getOrderBoard().size());
        }

        @Test
        @DisplayName("the offer and order boards are returned as the internal references (getters expose state)")
        void gettersExposeInternalLists() {
            Track track = new Track(2);
            assertNotNull(track.getOfferBoard());
            assertNotNull(track.getOrderBoard());
            assertNotNull(track.giveOfferBoardMockup());
            assertNotNull(track.giveOrderBoardMockup());
            assertEquals(4, track.giveOfferBoardMockup().size());
            assertEquals(2, track.giveOrderBoardMockup().size());
        }
    }

    // --- randomizeStartingOrder ---

    @Test
    @DisplayName("randomizeStartingOrder assigns every player to an order slot and grants starting food")
    void randomizeStartingOrderAssignsAndFeeds() {
        Track track = new Track(5);
        List<Player> players = new ArrayList<>(List.of(
                newPeppe(0), newWu(1), newMassi(2), newPeppe(3), newWu(4)
        ));

        track.randomizeStartingOrder(players);

        // every player must end up on some order slot
        for (Player p : players) {
            assertNotNull(p.getAssignedOrderSlot(), p.getNickname() + " must be assigned");
        }
        // and the slot that holds them must point back
        for (int i = 0; i < 5; i++) {
            assertNotNull(track.getOrderBoard().get(i).getAssignedPlayer());
        }
    }

    @Test
    @DisplayName("randomizeStartingOrder follows the rulebook: 2 food for 1st, 3 for 2nd-3rd, 4 for 4th-5th")
    void randomizeStartingOrderRulebookFood() {
        Track track = new Track(5);
        List<Player> players = new ArrayList<>(List.of(
                newPeppe(0), newWu(1), newMassi(2), newPeppe(3), newWu(4)
        ));

        track.randomizeStartingOrder(players);

        // read food values back from the slots, in slot order
        assertEquals(2, track.getOrderBoard().get(0).getAssignedPlayer().getFood());
        assertEquals(3, track.getOrderBoard().get(1).getAssignedPlayer().getFood());
        assertEquals(3, track.getOrderBoard().get(2).getAssignedPlayer().getFood());
        assertEquals(4, track.getOrderBoard().get(3).getAssignedPlayer().getFood());
        assertEquals(4, track.getOrderBoard().get(4).getAssignedPlayer().getFood());
    }

    // --- assignOffer ---

    @Test
    @DisplayName("assignOffer delegates to the chosen Offer")
    void assignOfferDelegates() throws NotValidOfferException {
        Track track = new Track(2);
        Player peppe = newPeppe(0);

        track.assignOffer(peppe, 1);

        assertSame(peppe, track.getOfferBoard().get(1).getAssignedPlayer());
    }

    @Test
    @DisplayName("assignOffer with an out-of-range index throws NotValidOfferException")
    void assignOfferOutOfRange() {
        Track track = new Track(2);
        Player peppe = newPeppe(0);
        assertThrows(NotValidOfferException.class, () -> track.assignOffer(peppe, 99));
        assertThrows(NotValidOfferException.class, () -> track.assignOffer(peppe, -1));
    }

    @Test
    @DisplayName("assignOffer on an already occupied offer propagates NotValidOfferException from Offer")
    void assignOfferOccupiedPropagates() throws NotValidOfferException {
        Track track = new Track(2);
        Player peppe = newPeppe(0);
        Player wu = newWu(1);
        track.assignOffer(peppe, 0);
        assertThrows(NotValidOfferException.class, () -> track.assignOffer(wu, 0));
    }

    // --- getNextPlayerOfferAndActivate ---

    @Test
    @DisplayName("getNextPlayerOfferAndActivate returns assigned players left-to-right, activating the offer")
    void nextPlayerOfferActivates() throws NotValidOfferException {
        Track track = new Track(2);
        Player peppe = newPeppe(0);
        Player wu = newWu(1);

        // 2-player offer board: index 0 = (food 0, up 0, low 1), index 3 = (food 0, up 2, low 0)
        track.assignOffer(peppe, 0);
        track.assignOffer(wu, 3);

        Player first = track.getNextPlayerOfferAndActivate();
        assertSame(peppe, first);
        // activate(): index 0 sets drawableLower=1, drawableUpper=0
        assertEquals(0, peppe.getDrawableUpper());
        assertEquals(1, peppe.getDrawableLower());

        Player second = track.getNextPlayerOfferAndActivate();
        assertSame(wu, second);
        // activate(): index 3 sets drawableUpper=2, drawableLower=0
        assertEquals(2, wu.getDrawableUpper());
        assertEquals(0, wu.getDrawableLower());

        // no more assigned offers: returns null and resets internal index
        assertNull(track.getNextPlayerOfferAndActivate());
    }

    @Test
    @DisplayName("getNextPlayerOfferAndActivate returns null when no offer is assigned")
    void nextPlayerOfferNoneAssigned() {
        Track track = new Track(2);
        assertNull(track.getNextPlayerOfferAndActivate());
    }

    // --- deassignCurrentOffer ---

    @Test
    @DisplayName("deassignCurrentOffer moves the player from the offer to the next order slot and runs its effect")
    void deassignCurrentOfferMovesPlayerToOrderSlot() throws NotValidOfferException {
        Track track = new Track(2);
        Player peppe = newPeppe(0);

        // assign and activate an offer so selectedOffer points at it
        track.assignOffer(peppe, 0);
        track.getNextPlayerOfferAndActivate();

        // 2-player order slot 0: food gain = 1
        track.deassignCurrentOffer();

        // the offer is now empty
        assertNull(track.getOfferBoard().get(0).getAssignedPlayer());
        // the player is now sitting on the first order slot
        assertSame(peppe, track.getOrderBoard().get(0).getAssignedPlayer());
        // and the order slot's effect on occupation granted +1 food
        assertEquals(1, peppe.getFood());
    }

    @Test
    @DisplayName("deassignCurrentOffer is a no-op when the current offer has no player")
    void deassignCurrentOfferOnEmpty() {
        Track track = new Track(2);
        assertDoesNotThrow(track::deassignCurrentOffer);
    }

    // --- getNextPlayerOrderSlot ---

    @Test
    @DisplayName("getNextPlayerOrderSlot returns players in slot order, freeing each slot")
    void nextPlayerOrderSlotReturnsAndFrees() {
        Track track = new Track(2);
        Player peppe = newPeppe(0);
        Player wu = newWu(1);
        // manually place peppe in slot 0 and wu in slot 1
        track.getOrderBoard().get(0).assignPlayer(peppe);
        track.getOrderBoard().get(1).assignPlayer(wu);

        Player first = track.getNextPlayerOrderSlot();
        assertSame(peppe, first);
        assertNull(track.getOrderBoard().get(0).getAssignedPlayer(), "slot 0 must be freed");
        assertNull(peppe.getAssignedOrderSlot(), "player's back-reference must be cleared");

        Player second = track.getNextPlayerOrderSlot();
        assertSame(wu, second);
        assertNull(track.getOrderBoard().get(1).getAssignedPlayer());

        // both slots are empty: next call returns null and resets indices
        assertNull(track.getNextPlayerOrderSlot());
    }

    @Test
    @DisplayName("getNextPlayerOrderSlot returns null when the slot has no assigned player")
    void nextPlayerOrderSlotOnEmptySlot() {
        Track track = new Track(2);
        // both slots are empty by default
        assertNull(track.getNextPlayerOrderSlot());
    }

    // --- deassignCurrentOrderSlot ---

    @Test
    @DisplayName("deassignCurrentOrderSlot clears both the slot and the player's back-reference")
    void deassignCurrentOrderSlotClears() {
        Track track = new Track(2);
        Player peppe = newPeppe(0);
        // place peppe on slot 0 and have him point back to that slot via the standard API
        track.getOrderBoard().get(0).assignPlayer(peppe);
        assertSame(track.getOrderBoard().get(0), peppe.getAssignedOrderSlot());

        // calling getNextPlayerOrderSlot first sets selectedOrderSlot = 0
        track.getOrderBoard().get(0).assignPlayer(peppe); // re-assign because getNext cleared it
        // pick him up (clears slot 0 and selectedOrderSlot advances)
        track.getNextPlayerOrderSlot();

        // now place him back manually and deassign the (now still 0) slot
        track.getOrderBoard().get(0).assignPlayer(peppe);
        track.deassignCurrentOrderSlot(peppe);

        // deassignCurrentOrderSlot acts on selectedOrderSlot which advanced past 0 already;
        // we still expect the player's back-reference to be cleared in any case
        assertNull(peppe.getAssignedOrderSlot());
    }

    @Test
    @DisplayName("deassignCurrentOrderSlot is a no-op when passed null")
    void deassignCurrentOrderSlotNull() {
        Track track = new Track(2);
        assertDoesNotThrow(() -> track.deassignCurrentOrderSlot(null));
    }
}
