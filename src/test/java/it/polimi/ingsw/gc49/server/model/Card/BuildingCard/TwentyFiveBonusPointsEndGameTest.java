package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TwentyFiveBonusPointsEndGameTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    @Test
    void updateDataBankAddsPoints() {
        TwentyFiveBonusPointsEndGame card = new TwentyFiveBonusPointsEndGame(BuildingEvent.GAME_END, 3, 5, Era.FIRST, 2, null);
        card.updateDataBank(peppe.data);
        assertEquals(3, peppe.data.getNumBuildingPoints());
    }

    @Test
    void onEventEffectGrants25() {
        TwentyFiveBonusPointsEndGame card = new TwentyFiveBonusPointsEndGame(BuildingEvent.GAME_END, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        peppe.setPoints(4);

        card.onEventEffect();

        assertEquals(29, peppe.getPoints());
    }

    @Test
    void simpleString() {
        TwentyFiveBonusPointsEndGame card = new TwentyFiveBonusPointsEndGame(BuildingEvent.GAME_END, 0, 5, Era.FIRST, 2, null);
        assertEquals("EDIFICIO (25 punti finali)", card.simpleToString());
    }
}
