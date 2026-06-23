package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DoubleShamanPointsCardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    @Test
    void updateDataBankAddsPoints() {
        DoubleShamanPointsCard card = new DoubleShamanPointsCard(BuildingEvent.RITUAL_EVENT, 2, 5, Era.FIRST, 2, null);
        card.updateDataBank(peppe.data);
        assertEquals(2, peppe.data.getNumBuildingPoints());
    }

    @Test
    void onEventEffectDoublesForUniqueWinner() {
        DoubleShamanPointsCard card = new DoubleShamanPointsCard(BuildingEvent.RITUAL_EVENT, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        peppe.setUniqueWinner(true);
        peppe.setPointsToPay(-4); // negative = reward gained during ritual

        card.onEventEffect();

        assertEquals(-8, peppe.getPointsToPay());
    }

    @Test
    void onEventEffectNotUniqueWinner() {
        DoubleShamanPointsCard card = new DoubleShamanPointsCard(BuildingEvent.RITUAL_EVENT, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        peppe.setUniqueWinner(false);
        peppe.setPointsToPay(-4);

        card.onEventEffect();

        assertEquals(-4, peppe.getPointsToPay());
    }

    @Test
    void onEventEffectNonNegative() {
        DoubleShamanPointsCard card = new DoubleShamanPointsCard(BuildingEvent.RITUAL_EVENT, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        peppe.setUniqueWinner(true);
        peppe.setPointsToPay(3);

        card.onEventEffect();

        assertEquals(3, peppe.getPointsToPay());
    }

    @Test
    void simpleString() {
        DoubleShamanPointsCard card = new DoubleShamanPointsCard(BuildingEvent.RITUAL_EVENT, 0, 5, Era.FIRST, 2, null);
        assertEquals("EDIFICIO (doppie stelle)", card.simpleToString());
    }
}
