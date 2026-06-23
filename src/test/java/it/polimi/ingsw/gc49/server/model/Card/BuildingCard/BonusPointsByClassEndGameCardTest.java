package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BonusPointsByClassEndGameCardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    @Test
    void updateDataBankAddsPoints() {
        BonusPointsByClassEndGameCard card = new BonusPointsByClassEndGameCard(
                CharacterType.Hunter, 2, BuildingEvent.GAME_END, 4, 5, Era.FIRST, 2, null);
        card.updateDataBank(peppe.data);
        assertEquals(4, peppe.data.getNumBuildingPoints());
    }

    @Test
    void onEventEffectAwardsPerClass() {
        BonusPointsByClassEndGameCard card = new BonusPointsByClassEndGameCard(
                CharacterType.Hunter, 2, BuildingEvent.GAME_END, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        peppe.data.addCharacterCount(CharacterType.Hunter, 3);

        card.onEventEffect();

        assertEquals(6, peppe.getPoints()); // 2 * 3
    }

    @Test
    void onEventEffectNoneOfClass() {
        BonusPointsByClassEndGameCard card = new BonusPointsByClassEndGameCard(
                CharacterType.Shaman, 5, BuildingEvent.GAME_END, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);

        card.onEventEffect();

        assertEquals(0, peppe.getPoints());
    }

    @Test
    void simpleString() {
        BonusPointsByClassEndGameCard card = new BonusPointsByClassEndGameCard(
                CharacterType.Hunter, 1, BuildingEvent.GAME_END, 0, 5, Era.FIRST, 2, null);
        assertEquals("EDIFICIO (strapunti da classe)", card.simpleToString());
    }
}
