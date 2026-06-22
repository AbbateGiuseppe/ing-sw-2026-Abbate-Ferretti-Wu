package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BonusHuntingCardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    // --- behaviour inherited from the abstract BuildingCard ---

    @Test
    @DisplayName("canGet is true when food covers the price")
    void canGetWhenEnoughFood() {
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 3, 5, Era.FIRST, 2, null);
        peppe.setFood(5);
        assertTrue(card.canGet(peppe));
    }

    @Test
    @DisplayName("canGet is false when food is below the price")
    void cannotGetWhenNotEnoughFood() {
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 3, 5, Era.FIRST, 2, null);
        peppe.setFood(4);
        assertFalse(card.canGet(peppe));
    }

    @Test
    @DisplayName("canGet takes the building discount into account")
    void canGetWithDiscount() {
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 3, 5, Era.FIRST, 2, null);
        peppe.setFood(3);
        peppe.data.addNumBuildingDiscount(2); // effective price = 5 - 2 = 3
        assertTrue(card.canGet(peppe));
    }

    @Test
    @DisplayName("updateDataBank adds the endgame points")
    void updateDataBankAddsPoints() {
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 7, 5, Era.FIRST, 2, null);
        card.updateDataBank(peppe.data);
        assertEquals(7, peppe.data.getNumBuildingPoints());
    }

    @Test
    @DisplayName("onDraw charges the discounted price and sets the owner")
    void onDrawChargesAndSetsOwner() {
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 3, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        assertEquals(5, peppe.getFood(), "food reduced by the full price");
        assertSame(peppe, card.getOwner());
    }

    @Test
    @DisplayName("onDraw never rewards food when discount exceeds the price")
    void onDrawDiscountCappedAtZero() {
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 3, 2, Era.FIRST, 2, null);
        peppe.setFood(10);
        peppe.data.addNumBuildingDiscount(5); // discount > price
        card.onDraw(peppe);
        assertEquals(10, peppe.getFood(), "no extra food granted");
    }

    @Test
    @DisplayName("getFoodPrice and isLowerLineOnSetup")
    void simpleGetters() {
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 3, 6, Era.SECOND, 3, null);
        assertEquals(6, card.getFoodPrice());
        assertEquals(Era.SECOND, card.getEra());
        assertEquals(3, card.getMinNumPlayers());
        assertTrue(card.isLowerLineOnSetup());
    }

    // --- the card's own effect ---

    @Test
    @DisplayName("onEventEffect reduces food-to-pay and points-to-pay by the number of hunters")
    void onEventEffectReducesPayments() {
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 3, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe); // sets owner = peppe
        peppe.data.addCharacterCount(CharacterType.Hunter, 3);
        peppe.setFoodToPay(8);
        peppe.setPointsToPay(6);

        card.onEventEffect();

        assertEquals(5, peppe.getFoodToPay());
        assertEquals(3, peppe.getPointsToPay());
    }

    @Test
    @DisplayName("simpleToString is the localized name")
    void simpleString() {
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 3, 5, Era.FIRST, 2, null);
        assertEquals("EDIFICIO (stracaccia)", card.simpleToString());
    }
}
