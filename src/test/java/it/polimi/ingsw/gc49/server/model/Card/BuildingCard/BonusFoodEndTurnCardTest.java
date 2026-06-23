package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.Track.OrderSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BonusFoodEndTurnCardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    @Test
    void updateDataBankAddsPoints() {
        BonusFoodEndTurnCard card = new BonusFoodEndTurnCard(BuildingEvent.TURN_END, 1, 5, Era.FIRST, 2, null);
        card.updateDataBank(peppe.data);
        assertEquals(1, peppe.data.getNumBuildingPoints());
    }

    @Test
    void onEventEffectGrantsFoodOnFoodSlot() {
        BonusFoodEndTurnCard card = new BonusFoodEndTurnCard(BuildingEvent.TURN_END, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        new OrderSlot(1).assignPlayer(peppe); // food-gaining slot
        card.onEventEffect();
        assertEquals(6, peppe.getFood());
    }

    @Test
    void onEventEffectNoFoodOnPlainSlot() {
        BonusFoodEndTurnCard card = new BonusFoodEndTurnCard(BuildingEvent.TURN_END, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        new OrderSlot().assignPlayer(peppe); // foodGain = 0

        card.onEventEffect();

        assertEquals(5, peppe.getFood());
    }

    @Test
    void simpleString() {
        BonusFoodEndTurnCard card = new BonusFoodEndTurnCard(BuildingEvent.TURN_END, 0, 5, Era.FIRST, 2, null);
        assertEquals("EDIFICIO (cibo da piazzamento)", card.simpleToString());
    }
}
