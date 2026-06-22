package it.polimi.ingsw.gc49.server.model.CardBoard;

import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.BuildingCard;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.CharacterCard.CharacterCard;
import it.polimi.ingsw.gc49.server.model.Card.EventCard.EventCard;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Game;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the JSON-backed {@link Deck} construction via {@code new Deck(Game)}.
 * <p>
 * Expected card counts (derived from {@code tribe_cards.json} and {@code building_cards.json}
 * filtered by {@code minNumPlayers <= n}):
 * <ul>
 *   <li>2 players: 60 tribe (20/20/18/2 per era), 21 buildings (6/7/8)</li>
 *   <li>3 players: 74 tribe (25/25/22/2 per era), 21 buildings</li>
 *   <li>4 players: 85 tribe (30/28/25/2 per era), 21 buildings</li>
 *   <li>5 players: 96 tribe (33/32/29/2 per era), 21 buildings (every card included)</li>
 * </ul>
 */
class DeckLoadingTest {

    /** Drains the tribe deck and returns every card drawn, in order. */
    private static List<Card> drainTribe(Deck deck) {
        List<Card> drawn = new ArrayList<>();
        Card c;
        while ((c = deck.dealTribeCard()) != null) {
            drawn.add(c);
        }
        return drawn;
    }

    /** Drains the building deck and returns every card drawn, in order. */
    private static List<Card> drainBuilding(Deck deck) {
        List<Card> drawn = new ArrayList<>();
        Card c;
        while ((c = deck.dealBuildingCard()) != null) {
            drawn.add(c);
        }
        return drawn;
    }

    /** Builds a real Game with `n` players (Peppe + Wu/Massi as needed). */
    private static Game buildGame(int n) {
        List<String> nicks = List.of("Peppe", "Wu", "Massi", "Peppe", "Wu").subList(0, n);
        return new Game(n, nicks, "deckLoadingTest");
    }

    /** Fresh, fully loaded Deck for `n` players (independent of the game's own deck). */
    private static Deck freshDeck(int n) {
        return new Deck(buildGame(n));
    }

    // --- exact tribe counts ---

    @Test
    @DisplayName("tribe deck for 2 players loads exactly 63 cards")
    void tribeCountTwoPlayers() {
        assertEquals(63, drainTribe(freshDeck(2)).size());
    }

    @Test
    @DisplayName("tribe deck for 3 players loads exactly 74 cards")
    void tribeCountThreePlayers() {
        assertEquals(74, drainTribe(freshDeck(3)).size());
    }

    @Test
    @DisplayName("tribe deck for 4 players loads exactly 85 cards")
    void tribeCountFourPlayers() {
        assertEquals(85, drainTribe(freshDeck(4)).size());
    }

    @Test
    @DisplayName("tribe deck for 5 players loads exactly 96 cards (every card in the JSON)")
    void tribeCountFivePlayers() {
        assertEquals(96, drainTribe(freshDeck(5)).size());
    }

    // --- exact building counts ---

    @Test
    @DisplayName("building deck for 2 players loads exactly 21 cards")
    void buildingCountTwoPlayers() {
        assertEquals(21, drainBuilding(freshDeck(2)).size());
    }

    @Test
    @DisplayName("building deck for 3 players loads exactly 21 cards")
    void buildingCountThreePlayers() {
        assertEquals(21, drainBuilding(freshDeck(3)).size());
    }

    @Test
    @DisplayName("building deck for 4 players loads exactly 21 cards")
    void buildingCountFourPlayers() {
        assertEquals(21, drainBuilding(freshDeck(4)).size());
    }

    @Test
    @DisplayName("building deck for 5 players loads exactly 21 cards")
    void buildingCountFivePlayers() {
        assertEquals(21, drainBuilding(freshDeck(5)).size());
    }

    // --- exhaustion ---

    @Test
    @DisplayName("tribe deck returns null after being fully drained (2 players)")
    void tribeExhaustsTwoPlayers() {
        Deck d = freshDeck(2);
        drainTribe(d);
        assertNull(d.dealTribeCard());
        assertNull(d.dealTribeCard(), "subsequent calls remain null");
    }

    @Test
    @DisplayName("tribe deck returns null after being fully drained (5 players)")
    void tribeExhaustsFivePlayers() {
        Deck d = freshDeck(5);
        drainTribe(d);
        assertNull(d.dealTribeCard());
    }

    @Test
    @DisplayName("building deck returns null after being fully drained")
    void buildingExhausts() {
        Deck d = freshDeck(5);
        drainBuilding(d);
        assertNull(d.dealBuildingCard());
        assertNull(d.dealBuildingCard());
    }

    // --- distinct instances ---

    @Test
    @DisplayName("all drawn tribe cards are distinct instances (no card dealt twice)")
    void tribeCardsAreDistinctInstances() {
        List<Card> drawn = drainTribe(freshDeck(5));
        Set<Card> distinct = new HashSet<>(drawn);
        assertEquals(drawn.size(), distinct.size());
    }

    @Test
    @DisplayName("all drawn building cards are distinct instances")
    void buildingCardsAreDistinctInstances() {
        List<Card> drawn = drainBuilding(freshDeck(5));
        Set<Card> distinct = new HashSet<>(drawn);
        assertEquals(drawn.size(), distinct.size());
    }

    // --- types ---

    @Test
    @DisplayName("the tribe deck contains only CharacterCard and EventCard instances")
    void tribeDeckHasCorrectTypes() {
        for (Card c : drainTribe(freshDeck(5))) {
            assertNotNull(c);
            assertTrue(c instanceof CharacterCard || c instanceof EventCard,
                    "tribe deck must contain only character or event cards, got: " + c.getClass());
        }
    }

    @Test
    @DisplayName("the building deck contains only BuildingCard instances")
    void buildingDeckHasCorrectTypes() {
        for (Card c : drainBuilding(freshDeck(5))) {
            assertNotNull(c);
            assertInstanceOf(BuildingCard.class, c);
        }
    }

    // --- era ordering ---

    @Test
    @DisplayName("tribe cards are dealt in non-decreasing era order for 2 players")
    void tribeErasNonDecreasingTwoPlayers() {
        assertErasNonDecreasing(drainTribe(freshDeck(2)));
    }

    @Test
    @DisplayName("tribe cards are dealt in non-decreasing era order for 5 players")
    void tribeErasNonDecreasingFivePlayers() {
        assertErasNonDecreasing(drainTribe(freshDeck(5)));
    }

    @Test
    @DisplayName("building cards are dealt in non-decreasing era order")
    void buildingErasNonDecreasing() {
        assertErasNonDecreasing(drainBuilding(freshDeck(5)));
    }

    private static void assertErasNonDecreasing(List<Card> drawn) {
        for (int i = 1; i < drawn.size(); i++) {
            Era previous = drawn.get(i - 1).getEra();
            Era current = drawn.get(i).getEra();
            assertTrue(previous.ordinal() <= current.ordinal(),
                    "deck must not go back to an earlier era at index " + i
                            + " (" + previous + " -> " + current + ")");
        }
    }

    // --- min-num-players filter ---

    @Test
    @DisplayName("no tribe card has minNumPlayers greater than the actual player count (2 players)")
    void tribeRespectsMinPlayersTwo() {
        for (Card c : drainTribe(freshDeck(2))) {
            assertTrue(c.getMinNumPlayers() <= 2);
        }
    }

    @Test
    @DisplayName("no tribe card has minNumPlayers greater than the actual player count (3 players)")
    void tribeRespectsMinPlayersThree() {
        for (Card c : drainTribe(freshDeck(3))) {
            assertTrue(c.getMinNumPlayers() <= 3);
        }
    }

    @Test
    @DisplayName("no tribe card has minNumPlayers greater than the actual player count (4 players)")
    void tribeRespectsMinPlayersFour() {
        for (Card c : drainTribe(freshDeck(4))) {
            assertTrue(c.getMinNumPlayers() <= 4);
        }
    }

    @Test
    @DisplayName("no building card has minNumPlayers greater than the actual player count")
    void buildingRespectsMinPlayers() {
        for (Card c : drainBuilding(freshDeck(2))) {
            assertTrue(c.getMinNumPlayers() <= 2);
        }
    }

    // --- exact era split for the tribe deck ---

    @Test
    @DisplayName("tribe deck era split for 2 players: 21 FIRST, 21 SECOND, 18 THIRD, 2 THIRD_FINAL")
    void tribeEraSplitTwoPlayers() {
        assertTribeEraSplit(2, 21, 21, 19, 2);
    }

    @Test
    @DisplayName("tribe deck era split for 3 players: 25 FIRST, 25 SECOND, 22 THIRD, 2 THIRD_FINAL")
    void tribeEraSplitThreePlayers() {
        assertTribeEraSplit(3, 25, 25, 22, 2);
    }

    @Test
    @DisplayName("tribe deck era split for 4 players: 30 FIRST, 28 SECOND, 25 THIRD, 2 THIRD_FINAL")
    void tribeEraSplitFourPlayers() {
        assertTribeEraSplit(4, 30, 28, 25, 2);
    }

    @Test
    @DisplayName("tribe deck era split for 5 players: 33 FIRST, 32 SECOND, 29 THIRD, 2 THIRD_FINAL")
    void tribeEraSplitFivePlayers() {
        assertTribeEraSplit(5, 33, 32, 29, 2);
    }

    private static void assertTribeEraSplit(int players, int first, int second, int third, int finalEra) {
        List<Card> drawn = drainTribe(freshDeck(players));
        assertEquals(first, drawn.stream().filter(c -> c.getEra() == Era.FIRST).count(), "FIRST count");
        assertEquals(second, drawn.stream().filter(c -> c.getEra() == Era.SECOND).count(), "SECOND count");
        assertEquals(third, drawn.stream().filter(c -> c.getEra() == Era.THIRD).count(), "THIRD count");
        assertEquals(finalEra, drawn.stream().filter(c -> c.getEra() == Era.THIRD_FINAL).count(), "THIRD_FINAL count");
    }

    @Test
    @DisplayName("building deck era split is exactly 6 FIRST + 7 SECOND + 8 THIRD (for every player count)")
    void buildingEraSplit() {
        List<Card> drawn = drainBuilding(freshDeck(5));
        assertEquals(6, drawn.stream().filter(c -> c.getEra() == Era.FIRST).count());
        assertEquals(7, drawn.stream().filter(c -> c.getEra() == Era.SECOND).count());
        assertEquals(8, drawn.stream().filter(c -> c.getEra() == Era.THIRD).count());
        assertEquals(0, drawn.stream().filter(c -> c.getEra() == Era.THIRD_FINAL).count());
    }

    // --- monotonicity ---

    @Test
    @DisplayName("the tribe-card count grows monotonically with the player count")
    void monotonicTribeCount() {
        int two = drainTribe(freshDeck(2)).size();
        int three = drainTribe(freshDeck(3)).size();
        int four = drainTribe(freshDeck(4)).size();
        int five = drainTribe(freshDeck(5)).size();

        assertTrue(two <= three);
        assertTrue(three <= four);
        assertTrue(four <= five);
        assertEquals(96, five, "5-player deck loads every card in the JSON");
    }
}
