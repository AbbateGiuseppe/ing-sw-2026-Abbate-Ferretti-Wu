package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SustainDiscountByClassCardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    @Test
    void updateDataBankAddsPoints() {
        SustainDiscountByClassCard card = new SustainDiscountByClassCard(
                CharacterType.Gatherer, BuildingEvent.SUSTENANCE_EVENT, 2, 5, Era.FIRST, 2, null);
        card.updateDataBank(peppe.data);
        assertEquals(2, peppe.data.getNumBuildingPoints());
    }

    @Test
    void onEventEffectReducesFoodToPay() {
        SustainDiscountByClassCard card = new SustainDiscountByClassCard(
                CharacterType.Gatherer, BuildingEvent.SUSTENANCE_EVENT, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        peppe.data.addCharacterCount(CharacterType.Gatherer, 2);
        peppe.setFoodToPay(6);

        card.onEventEffect();

        assertEquals(4, peppe.getFoodToPay());
    }

    @Test
    void onEventEffectNoneOfClass() {
        SustainDiscountByClassCard card = new SustainDiscountByClassCard(
                CharacterType.Gatherer, BuildingEvent.SUSTENANCE_EVENT, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        peppe.setFoodToPay(6);

        card.onEventEffect();

        assertEquals(6, peppe.getFoodToPay());
    }

    @Test
    void simpleString() {
        SustainDiscountByClassCard card = new SustainDiscountByClassCard(
                CharacterType.Gatherer, BuildingEvent.SUSTENANCE_EVENT, 0, 5, Era.FIRST, 2, null);
        assertEquals("EDIFICIO (sconto sostentamento)", card.simpleToString());
    }
}
