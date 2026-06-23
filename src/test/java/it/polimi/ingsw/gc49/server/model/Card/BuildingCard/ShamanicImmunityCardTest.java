package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShamanicImmunityCardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    @Test
    void updateDataBankAddsPoints() {
        ShamanicImmunityCard card = new ShamanicImmunityCard(BuildingEvent.RITUAL_EVENT, 2, 5, Era.FIRST, 2, null);
        card.updateDataBank(peppe.data);
        assertEquals(2, peppe.data.getNumBuildingPoints());
    }

    @Test
    void onEventEffectCancelsPenalty() {
        ShamanicImmunityCard card = new ShamanicImmunityCard(BuildingEvent.RITUAL_EVENT, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        peppe.setPointsToPay(5); // penalty to be lost during the ritual

        card.onEventEffect();

        assertEquals(0, peppe.getPointsToPay());
    }

    @Test
    void onEventEffectNonPositiveUnchanged() {
        ShamanicImmunityCard card = new ShamanicImmunityCard(BuildingEvent.RITUAL_EVENT, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        peppe.setPointsToPay(-3); // a reward, not a penalty

        card.onEventEffect();

        assertEquals(-3, peppe.getPointsToPay());
    }

    @Test
    void simpleString() {
        ShamanicImmunityCard card = new ShamanicImmunityCard(BuildingEvent.RITUAL_EVENT, 0, 5, Era.FIRST, 2, null);
        assertEquals("EDIFICIO (immunità sciamanica)", card.simpleToString());
    }
}
