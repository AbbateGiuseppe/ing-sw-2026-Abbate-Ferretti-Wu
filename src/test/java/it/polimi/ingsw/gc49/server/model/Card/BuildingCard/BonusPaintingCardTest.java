package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BonusPaintingCardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    @Test
    void updateDataBankAddsPoints() {
        BonusPaintingCard card = new BonusPaintingCard(BuildingEvent.PAINTING_EVENT, 2, 5, Era.FIRST, 2, null);
        card.updateDataBank(peppe.data);
        assertEquals(2, peppe.data.getNumBuildingPoints());
    }

    @Test
    void onEventEffectReducesFoodToPay() {
        BonusPaintingCard card = new BonusPaintingCard(BuildingEvent.PAINTING_EVENT, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        peppe.data.addCharacterCount(CharacterType.Artist, 2);
        peppe.setFoodToPay(7);

        card.onEventEffect();

        assertEquals(5, peppe.getFoodToPay());
    }

    @Test
    @DisplayName("onEventEffect leaves food-to-pay unchanged with no artists")
    void onEventEffectNoArtists() {
        BonusPaintingCard card = new BonusPaintingCard(BuildingEvent.PAINTING_EVENT, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);
        peppe.setFoodToPay(7);

        card.onEventEffect();

        assertEquals(7, peppe.getFoodToPay());
    }

    @Test
    @DisplayName("simpleToString is the localized name")
    void simpleString() {
        BonusPaintingCard card = new BonusPaintingCard(BuildingEvent.PAINTING_EVENT, 0, 5, Era.FIRST, 2, null);
        assertEquals("EDIFICIO (strapitture)", card.simpleToString());
    }
}
