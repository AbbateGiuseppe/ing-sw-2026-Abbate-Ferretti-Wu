package it.polimi.ingsw.gc49.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.*;
import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BuildingCardTest {

    private Player player;
    private EventManager eventManager;

    @BeforeEach
    void setUp() {
        player = new Player("TestPlayer", 0);
        eventManager = new EventManager();
    }

    @Test
    void testBonusHuntingCard_canGet_withEnoughFood() {
        player.addFood(10);
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 5, 3, Era.FIRST, 2);

        assertTrue(card.canGet(player), "Player with enough food should be able to get the card");
    }

    @Test
    void testBonusHuntingCard_cannotGet_withoutEnoughFood() {
        player.addFood(2);
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 5, 5, Era.FIRST, 2);

        assertFalse(card.canGet(player), "Player without enough food should not be able to get the card");
    }

    @Test
    void testBonusHuntingCard_canGet_withBuildingDiscount() {
        player.addFood(5);
        player.data.addNumBuildingDiscount(2);
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 5, 7, Era.FIRST, 2);

        assertTrue(card.canGet(player), "Player with discount should be able to get the card");
    }

    @Test
    void testBonusHuntingCard_updateDataBank_addsPoints() {
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 10, 3, Era.FIRST, 2);

        card.updateDataBank(player.data);

        assertEquals(10, player.data.getNumBuildingPoints(), "DataBank should have 10 building points");
    }

    @Test
    void testBonusHuntingCard_onDraw_subtractsFood() {
        player.addFood(10);
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 5, 3, Era.FIRST, 2);

        card.onDraw(player);

        assertEquals(7, player.getFood(), "Player food should be reduced by card price");
    }

    @Test
    void testBonusHuntingCard_onDraw_setsOwner() {
        player.addFood(10);
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 5, 3, Era.FIRST, 2);

        card.onDraw(player);

        assertEquals(player, card.getOwner(), "Card owner should be set to the player");
    }

    @Test
    void testBonusHuntingCard_onDraw_withDiscount() {
        player.addFood(10);
        player.data.addNumBuildingDiscount(2);
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 5, 5, Era.FIRST, 2);

        card.onDraw(player);

        assertEquals(7, player.getFood(), "Player food should be reduced by price minus discount");
    }

    @Test
    void testBonusHuntingCard_onEventEffect_reducesHunterBasedCosts() {
        player.addFood(10);
        player.data.addCharacterCount(CharacterType.Hunter, 3);
        player.setFoodToPay(10);
        player.setPointsToPay(10);

        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 5, 3, Era.FIRST, 2);
        card.onDraw(player);
        card.onEventEffect();

        assertEquals(7, player.getFoodToPay(), "Food to pay should be reduced by hunter count");
        assertEquals(7, player.getPointsToPay(), "Points to pay should be reduced by hunter count");
    }

    @Test
    void testBonusFoodEndTurnCard_onEventEffect() {
        player.addFood(5);
        player.data.addCharacterCount(CharacterType.Gatherer, 4);

        BonusFoodEndTurnCard card = new BonusFoodEndTurnCard(BuildingEvent.TURN_END, 8, 6, Era.SECOND, 2);
        card.onDraw(player);
        card.onEventEffect();

        assertEquals(9, player.getFood(), "Player should gain food equal to gatherer count");
    }

    @Test
    void testBonusPaintingCard_onEventEffect() {
        player.data.addCharacterCount(CharacterType.Artist, 2);

        BonusPaintingCard card = new BonusPaintingCard(BuildingEvent.PAINTING_EVENT, 10, 4, Era.SECOND, 2);
        card.onDraw(player);
        card.onEventEffect();

        assertEquals(-2, player.getFoodToPay(), "Points to pay should be reduced by artist count");
    }

    @Test
    void testOneMoreCardCard_onEventEffect() {
        OneMoreCardCard card = new OneMoreCardCard(BuildingEvent.TURN_END, 7, 5, Era.FIRST, 2);
        card.onDraw(player);

        assertDoesNotThrow(() -> {
            card.onEventEffect();
        }, "OneMoreCardCard effect should execute without errors");
    }

    @Test
    void testShamanicImmunityCard_onEventEffect() {
        player.setPointsToPay(10);
        player.data.addCharacterCount(CharacterType.Shaman, 2);

        ShamanicImmunityCard card = new ShamanicImmunityCard(BuildingEvent.RITUAL_EVENT, 6, 4, Era.SECOND, 3);
        card.onDraw(player);
        card.onEventEffect();

        assertEquals(0, player.getPointsToPay(), "Points to pay should be reduced to 0");
    }



    @Test
    void testDoubleBuilderPointsCard_onEventEffect() {
        player.data.addCharacterCount(CharacterType.Builder, 4);
        player.setPointsToPay(-8);

        DoubleBuilderPointsCard card = new DoubleBuilderPointsCard(BuildingEvent.GAME_END, 15, 8, Era.THIRD_FINAL, 3);
        card.onDraw(player);
        card.onEventEffect();

        assertEquals(-8, player.getPointsToPay(), "Points to pay should be doubled (4 builders * 2 - 8 = 0 becomes -8)");
    }

    @Test
    void testDoubleShamanPointsCard_onEventEffect() {
        player.data.addCharacterCount(CharacterType.Shaman, 2);
        player.setPointsToPay(-4);

        DoubleShamanPointsCard card = new DoubleShamanPointsCard(BuildingEvent.GAME_END, 13, 7, Era.THIRD_FINAL, 2);
        card.onDraw(player);
        card.onEventEffect();

        assertEquals(-4, player.getPointsToPay(), "Points to pay should be doubled");
    }

    @Test
    void testCardEraAndMinPlayers() {
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 5, 3, Era.SECOND, 3);

        assertEquals(Era.SECOND, card.getEra(), "Card should have correct era");
        assertEquals(3, card.getMinNumPlayers(), "Card should have correct min players");
    }

    @Test
    void testFoodPriceGetter() {
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 5, 7, Era.FIRST, 2);

        assertEquals(7, card.getFoodPrice(), "Card should return correct food price");
    }

    @Test
    void testAddBuildingToManager() {
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 5, 3, Era.FIRST, 2);
        card.onDraw(player);

        assertDoesNotThrow(() -> {
            card.addBuildingToManager(player, eventManager);
        }, "Adding building to manager should not throw exception");
    }
}
