package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Hunter;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Totem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MockupGameTest {

    private MockupGame game;
    private MockupPlayer peppe;
    private MockupPlayer wu;
    private MockupOffer offerA;
    private MockupOffer offerB;
    private MockupOrder orderA;
    private MockupOrder orderB;

    @BeforeEach
    void setUp() {
        peppe = new MockupPlayer("Peppe", 0, true, Totem.BLUE, 2, 0, new ArrayList<>(), new ArrayList<>(), 0, 0);
        wu = new MockupPlayer("Wu", 1, true, Totem.YELLOW, 3, 0, new ArrayList<>(), new ArrayList<>(), 0, 0);

        offerA = new MockupOffer(0, 1, 0, null);
        offerB = new MockupOffer(2, 0, 0, null);
        orderA = new MockupOrder(2, false, 0, 0, null);
        orderB = new MockupOrder(0, true, 1, 2, null);

        List<MockupPlayer> players = new ArrayList<>(List.of(peppe, wu));
        List<MockupOffer> offers = new ArrayList<>(List.of(offerA, offerB));
        List<MockupOrder> orders = new ArrayList<>(List.of(orderA, orderB));

        game = new MockupGame(
                players, Era.FIRST,
                new ArrayList<>(), new ArrayList<>(),
                new ArrayList<>(), new ArrayList<>(),
                offers, orders);
    }

    @Test
    @DisplayName("constructor wires offers and orders back to the game (they can render with player totems)")
    void constructorWiresOffersAndOrders() {
        // assigning a player to the offer and rendering must succeed -> the back-reference is set
        offerA.setAssignedPlayerIndex(0);
        assertDoesNotThrow(() -> offerA.getRectangleAttributedString());
        orderA.setAssignedPlayerIndex(1);
        assertDoesNotThrow(() -> orderA.getRectangleAttributedString());
    }

    @Test
    @DisplayName("getters expose immutable views of the boards")
    void boardsAreUnmodifiable() {
        assertThrows(UnsupportedOperationException.class,
                () -> game.getPlayers().add(peppe));
        assertThrows(UnsupportedOperationException.class,
                () -> game.getOfferBoard().add(offerA));
        assertThrows(UnsupportedOperationException.class,
                () -> game.getOrderBoard().add(orderA));
        assertThrows(UnsupportedOperationException.class,
                () -> game.getUpperLine().add(new Hunter(false, Era.FIRST, 2, null)));
        assertThrows(UnsupportedOperationException.class,
                () -> game.getDiscards().add(new Hunter(false, Era.FIRST, 2, null)));
    }

    @Test
    @DisplayName("getPlayer(i) returns the player at the given index")
    void getPlayerByIndex() {
        assertSame(peppe, game.getPlayer(0));
        assertSame(wu, game.getPlayer(1));
    }

    @Test
    @DisplayName("setCurrentPlayerIndex updates the ofTurn flag on both the old and new player")
    void setCurrentPlayerToggleOfTurn() {
        // initial: currentPlayerIndex = 0 (Peppe)
        // first toggle: Peppe -> off, but Peppe was never set to true, so just ensure no exception
        // and the new current is on
        game.setCurrentPlayerIndex(1);
        assertFalse(peppe.isOfTurn());
        assertTrue(wu.isOfTurn());

        game.setCurrentPlayerIndex(0);
        assertTrue(peppe.isOfTurn());
        assertFalse(wu.isOfTurn());

        assertEquals(0, game.getCurrentPlayerIndex());
    }

    @Test
    @DisplayName("setPlayers replaces the player list")
    void setPlayersReplacesList() {
        MockupPlayer massi = new MockupPlayer("Massi", 0, true, null, 0, 0, new ArrayList<>(), new ArrayList<>(), 0, 0);
        game.setPlayers(List.of(massi));
        assertSame(massi, game.getPlayer(0));
        assertEquals(1, game.getPlayers().size());
    }

    @Test
    @DisplayName("setDeckTopEra round trip")
    void setDeckTopEra() {
        game.setDeckTopEra(Era.THIRD);
        assertEquals(Era.THIRD, game.getDeckTopEra());
    }

    @Test
    @DisplayName("setUpperLine / setLowerLine / setUpperBuilding / setLowerBuilding replace the rows")
    void replaceRows() {
        List<Card> cards = List.of(new Hunter(false, Era.FIRST, 2, null));
        game.setUpperLine(cards);
        game.setLowerLine(cards);
        game.setUpperBuilding(cards);
        game.setLowerBuilding(cards);
        assertEquals(1, game.getUpperLine().size());
        assertEquals(1, game.getLowerLine().size());
        assertEquals(1, game.getUpperBuilding().size());
        assertEquals(1, game.getLowerBuilding().size());
    }

    @Test
    @DisplayName("setOfferPlayerIndex delegates to the offer at the given index")
    void setOfferPlayerIndexDelegates() {
        game.setOfferPlayerIndex(0, 1);
        assertEquals(1, offerA.getAssignedPlayerIndex());
        game.setOfferPlayerIndex(0, null);
        assertNull(offerA.getAssignedPlayerIndex());
    }

    @Test
    @DisplayName("setOrderPlayerIndex delegates to the order slot at the given index")
    void setOrderPlayerIndexDelegates() {
        game.setOrderPlayerIndex(1, 0);
        assertEquals(0, orderB.getAssignedPlayerIndex());
        game.setOrderPlayerIndex(1, null);
        assertNull(orderB.getAssignedPlayerIndex());
    }

    @Test
    @DisplayName("addDiscards appends cards to the discard pile")
    void addDiscardsAppends() {
        game.addDiscards(List.of(
                new Hunter(false, Era.FIRST, 2, null),
                new Hunter(true, Era.FIRST, 2, null)));
        assertEquals(2, game.getDiscards().size());

        game.addDiscards(List.of(new Hunter(false, Era.FIRST, 2, null)));
        assertEquals(3, game.getDiscards().size());
    }

    @Test
    @DisplayName("getOfferBoardRectangleStrings returns one rectangle per offer")
    void offerBoardRectangles() {
        assertEquals(2, game.getOfferBoardRectangleStrings().size());
        for (var r : game.getOfferBoardRectangleStrings()) {
            assertNotNull(r);
        }
    }

    @Test
    @DisplayName("getOrderBoardRectangleStrings returns one rectangle per order slot")
    void orderBoardRectangles() {
        assertEquals(2, game.getOrderBoardRectangleStrings().size());
        for (var r : game.getOrderBoardRectangleStrings()) {
            assertNotNull(r);
        }
    }
}
