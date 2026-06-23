package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OneMoreCardCardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    @Test
    void updateDataBankAddsPoints() {
        OneMoreCardCard card = new OneMoreCardCard(BuildingEvent.ROUND_END, 2, 5, Era.FIRST, 2, null);
        card.updateDataBank(peppe.data);
        assertEquals(2, peppe.data.getNumBuildingPoints());
    }

    @Test
    void onEventEffectAddsUpperDraw() {
        OneMoreCardCard card = new OneMoreCardCard(BuildingEvent.ROUND_END, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        peppe.setDrawableUpper(1);

        card.onEventEffect();

        assertEquals(2, peppe.getDrawableUpper());
    }

    @Test
    void simpleString() {
        OneMoreCardCard card = new OneMoreCardCard(BuildingEvent.ROUND_END, 0, 5, Era.FIRST, 2, null);
        assertEquals("EDIFICIO (ulteriore carta)", card.simpleToString());
    }
}
