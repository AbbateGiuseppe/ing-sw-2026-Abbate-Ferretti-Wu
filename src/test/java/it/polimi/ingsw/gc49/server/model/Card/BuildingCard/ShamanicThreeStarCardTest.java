package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShamanicThreeStarCardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    @Test
    @DisplayName("updateDataBank adds the endgame points and three permanent stars")
    void updateDataBankAddsPointsAndStars() {
        ShamanicThreeStarCard card = new ShamanicThreeStarCard(BuildingEvent.RITUAL_EVENT, 2, 5, Era.FIRST, 2, null);
        card.updateDataBank(peppe.data);
        assertEquals(2, peppe.data.getNumBuildingPoints());
        assertEquals(3, peppe.data.getNumStars());
    }

    @Test
    @DisplayName("onEventEffect is a no-op (the bonus is passive via stars)")
    void onEventEffectIsNoOp() {
        ShamanicThreeStarCard card = new ShamanicThreeStarCard(BuildingEvent.RITUAL_EVENT, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        peppe.setPoints(7);
        peppe.setPointsToPay(4);

        card.onEventEffect();

        assertEquals(7, peppe.getPoints());
        assertEquals(4, peppe.getPointsToPay());
    }

    @Test
    @DisplayName("simpleToString is the localized name")
    void simpleString() {
        ShamanicThreeStarCard card = new ShamanicThreeStarCard(BuildingEvent.RITUAL_EVENT, 0, 5, Era.FIRST, 2, null);
        assertEquals("EDIFICIO (tre stelle)", card.simpleToString());
    }
}
