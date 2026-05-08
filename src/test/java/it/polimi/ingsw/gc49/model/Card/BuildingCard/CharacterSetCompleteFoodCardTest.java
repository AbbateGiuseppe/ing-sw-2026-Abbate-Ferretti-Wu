package it.polimi.ingsw.gc49.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.*;
import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.BuildingCard;
import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.CharacterSetCompleteFoodCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CharacterSetCompleteFoodCardTest {
    @Test
    void test() {
        EventManager eventManager = new EventManager();
        Player p = new Player(null, 0);
        p.data.addCharacterCount(CharacterType.Hunter,2);
        p.data.addCharacterCount(CharacterType.Artist,3);
        p.data.addCharacterCount(CharacterType.Gatherer,4);
        p.data.addCharacterCount(CharacterType.Shaman,7);
        p.data.addCharacterCount(CharacterType.Builder,9);
        p.data.addCharacterCount(CharacterType.Inventor,3);
        int oldFood = p.getFood();

        int PPReward = 0;
        int foodPrice = 0;

        BuildingCard bcard = new CharacterSetCompleteFoodCard(BuildingEvent.DRAW_EVENT, PPReward, foodPrice, Era.FIRST,3);
        bcard.updateDataBank(p.data);
        bcard.onDraw(p);
        p.data.addCharacterCount(CharacterType.Builder,1);
        eventManager.invokeEvent(BuildingEvent.DRAW_EVENT);
        assertEquals(oldFood,p.getFood());
        p.data.addCharacterCount(CharacterType.Hunter,1);
        eventManager.invokeEvent(BuildingEvent.DRAW_EVENT);
        assertEquals(oldFood + 5,p.getFood());
    }
}
