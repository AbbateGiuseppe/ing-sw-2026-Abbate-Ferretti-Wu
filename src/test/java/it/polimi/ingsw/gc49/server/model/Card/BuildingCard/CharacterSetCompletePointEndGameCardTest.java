package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CharacterSetCompletePointEndGameCardTest {

    private Player peppe;

    @BeforeEach
    void setUp() {
        peppe = new Player("Peppe", 0);
    }

    @Test
    @DisplayName("updateDataBank adds the endgame points")
    void updateDataBankAddsPoints() {
        CharacterSetCompletePointEndGameCard card =
                new CharacterSetCompletePointEndGameCard(BuildingEvent.GAME_END, 3, 5, Era.FIRST, 2, null);
        card.updateDataBank(peppe.data);
        assertEquals(3, peppe.data.getNumBuildingPoints());
    }

    @Test
    @DisplayName("onEventEffect awards 6 points per complete character set")
    void onEventEffectAwardsPerSet() {
        //TODO: da rifare
        CharacterSetCompletePointEndGameCard card =
                new CharacterSetCompletePointEndGameCard(BuildingEvent.GAME_END, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);

        peppe.data.addCharacterCount(CharacterType.Hunter, 1);
        peppe.data.addCharacterCount(CharacterType.Builder, 1);
        peppe.data.addCharacterCount(CharacterType.Gatherer, 1);
        peppe.data.addCharacterCount(CharacterType.Inventor, 1);
        peppe.data.addCharacterCount(CharacterType.Shaman, 1);
        peppe.data.addCharacterCount(CharacterType.Artist, 1);



        card.onEventEffect();
        assertEquals(1, peppe.data.getCurrentNumCompleteCharacterSets());
        assertEquals(16, peppe.getPoints());
    }

    @Test
    @DisplayName("onEventEffect awards nothing when no set is complete")
    void onEventEffectNoSet() {
        CharacterSetCompletePointEndGameCard card =
                new CharacterSetCompletePointEndGameCard(BuildingEvent.GAME_END, 0, 5, Era.FIRST, 2, null);
        peppe.setFood(10);
        card.onDraw(peppe);

        card.onEventEffect();

        assertEquals(0, peppe.getPoints());
    }

    @Test
    @DisplayName("simpleToString is the localized name")
    void simpleString() {
        CharacterSetCompletePointEndGameCard card =
                new CharacterSetCompletePointEndGameCard(BuildingEvent.GAME_END, 0, 5, Era.FIRST, 2, null);
        assertEquals("EDIFICIO (strapunti da set)", card.simpleToString());
    }
}
