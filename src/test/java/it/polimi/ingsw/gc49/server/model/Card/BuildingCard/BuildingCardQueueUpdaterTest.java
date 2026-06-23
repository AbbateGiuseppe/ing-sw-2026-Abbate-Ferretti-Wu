package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Covers the {@code queueUpdater != null} branch of {@code onEventEffect} for every concrete
 * {@link BuildingCard} that emits a model update. Each card is built with a
 * {@link FakeQueueUpdatable}, acquired by the owner via {@code onDraw}, then its effect is
 * fired; the test asserts that at least one update was queued.
 * <p>
 * {@code ShamanicThreeStarCard} is intentionally excluded: its {@code onEventEffect} is a
 * no-op (the bonus is passive through permanent stars), so it never queues anything.
 */
class BuildingCardQueueUpdaterTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
        peppe.setFood(50); // enough to afford any building's onDraw price
    }

    /** Acquires the card (onDraw sets the owner) and fires its effect. */
    private void acquireAndFire(BuildingCard card) {
        card.onDraw(peppe);
        card.onEventEffect();
    }

    @Test
    void bonusFoodEndTurn() {
        FakeQueueUpdatable q = new FakeQueueUpdatable();
        BonusFoodEndTurnCard card = new BonusFoodEndTurnCard(BuildingEvent.TURN_END, 1, 5, Era.FIRST, 2, q);
        card.onDraw(peppe);
        // the effect reads owner.getAssignedOrderSlot(): seat the owner on a food-gaining slot
        new it.polimi.ingsw.gc49.server.model.Track.OrderSlot(2).assignPlayer(peppe);
        card.onEventEffect();
        assertEquals(1, q.queued.size());
    }

    @Test
    void bonusHunting() {
        FakeQueueUpdatable q = new FakeQueueUpdatable();
        BonusHuntingCard card = new BonusHuntingCard(BuildingEvent.HUNTING_EVENT, 3, 5, Era.SECOND, 2, q);
        peppe.data.addCharacterCount(CharacterType.Hunter, 2);
        peppe.setFoodToPay(5);
        peppe.setPointsToPay(5);
        acquireAndFire(card);
        assertEquals(1, q.queued.size());
    }

    @Test
    void bonusPainting() {
        FakeQueueUpdatable q = new FakeQueueUpdatable();
        BonusPaintingCard card = new BonusPaintingCard(BuildingEvent.PAINTING_EVENT, 6, 5, Era.SECOND, 2, q);
        peppe.data.addCharacterCount(CharacterType.Artist, 2);
        peppe.setFoodToPay(5);
        acquireAndFire(card);
        assertEquals(1, q.queued.size());
    }

    @Test
    void bonusPointsByClass() {
        FakeQueueUpdatable q = new FakeQueueUpdatable();
        BonusPointsByClassEndGameCard card =
                new BonusPointsByClassEndGameCard(CharacterType.Hunter, 3, BuildingEvent.GAME_END, 8, 8, Era.THIRD, 2, q);
        peppe.data.addCharacterCount(CharacterType.Hunter, 2);
        acquireAndFire(card);
        assertEquals(1, q.queued.size());
    }

    @Test
    void characterSetCompleteFood() {
        FakeQueueUpdatable q = new FakeQueueUpdatable();
        CharacterSetCompleteFoodCard card =
                new CharacterSetCompleteFoodCard(BuildingEvent.DRAW_EVENT, 3, 4, Era.FIRST, 2, q);
        card.onDraw(peppe); // baseline 0 complete sets
        peppe.data.addCharacterCount(CharacterType.Hunter, 1);
        peppe.data.addCharacterCount(CharacterType.Builder, 1); // completes a set
        card.onEventEffect();
        assertEquals(1, q.queued.size());
    }

    @Test
    void characterSetCompletePoint() {
        FakeQueueUpdatable q = new FakeQueueUpdatable();
        CharacterSetCompletePointEndGameCard card =
                new CharacterSetCompletePointEndGameCard(BuildingEvent.GAME_END, 6, 5, Era.SECOND, 2, q);
        peppe.data.addCharacterCount(CharacterType.Hunter, 1);
        peppe.data.addCharacterCount(CharacterType.Builder, 1);
        acquireAndFire(card);
        assertEquals(1, q.queued.size());
    }

    @Test
    void doubleShamanPoints() {
        FakeQueueUpdatable q = new FakeQueueUpdatable();
        DoubleShamanPointsCard card = new DoubleShamanPointsCard(BuildingEvent.RITUAL_EVENT, 0, 7, Era.SECOND, 2, q);
        card.onDraw(peppe);
        peppe.setUniqueWinner(true);
        peppe.setPointsToPay(-4);
        card.onEventEffect();
        assertEquals(1, q.queued.size());
    }

    @Test
    void oneMoreCard() {
        FakeQueueUpdatable q = new FakeQueueUpdatable();
        OneMoreCardCard card = new OneMoreCardCard(BuildingEvent.DRAW_EVENT, 3, 9, Era.THIRD, 2, q);
        acquireAndFire(card);
        assertEquals(1, q.queued.size());
    }

    @Test
    void samePairInventions() {
        FakeQueueUpdatable q = new FakeQueueUpdatable();
        SamePairInventionsCard card = new SamePairInventionsCard(BuildingEvent.DRAW_EVENT, 4, 3, Era.FIRST, 2, q);
        card.onDraw(peppe);
        // form a same pair after acquisition
        peppe.data.addInvention(it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Invention.CANOE);
        peppe.data.addInvention(it.polimi.ingsw.gc49.server.model.Card.CharacterCard.Invention.CANOE);
        card.onEventEffect();
        assertEquals(1, q.queued.size());
    }

    @Test
    void shamanicImmunity() {
        FakeQueueUpdatable q = new FakeQueueUpdatable();
        ShamanicImmunityCard card = new ShamanicImmunityCard(BuildingEvent.RITUAL_EVENT, 2, 5, Era.FIRST, 2, q);
        card.onDraw(peppe);
        peppe.setPointsToPay(5);
        card.onEventEffect();
        assertEquals(1, q.queued.size());
    }

    @Test
    void sustainDiscount() {
        FakeQueueUpdatable q = new FakeQueueUpdatable();
        SustainDiscountByClassCard card =
                new SustainDiscountByClassCard(CharacterType.Gatherer, BuildingEvent.SUSTENANCE_EVENT, 4, 4, Era.FIRST, 2, q);
        peppe.data.addCharacterCount(CharacterType.Gatherer, 2);
        peppe.setFoodToPay(6);
        acquireAndFire(card);
        assertEquals(1, q.queued.size());
    }

    @Test
    void twentyFive() {
        FakeQueueUpdatable q = new FakeQueueUpdatable();
        TwentyFiveBonusPointsEndGame card = new TwentyFiveBonusPointsEndGame(BuildingEvent.GAME_END, 25, 10, Era.THIRD, 2, q);
        acquireAndFire(card);
        assertEquals(1, q.queued.size());
    }
}
