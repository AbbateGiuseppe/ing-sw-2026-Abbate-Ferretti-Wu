package it.polimi.ingsw.gc49.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.*;
import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.BuildingCard;
import it.polimi.ingsw.gc49.server.model.Card.BuildingCard.SamePairInventionsCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SamePairInventionsCardTest {
    @Test
    void test() {
        EventManager eventManager = new EventManager();
        Player p = new Player(null,0);

        int PPReward = 0;
        int foodPrice = 0;
        BuildingCard bcard = new SamePairInventionsCard(BuildingEvent.DRAW_EVENT,PPReward,foodPrice, Era.FIRST,3);
        bcard.updateDataBank(p.data);
        bcard.onDraw(p);
        bcard.addBuildingToManager(p, eventManager);

        p.data.addInvention(Invention.ARROWHEAD);
        int oldFood = p.getFood();
        eventManager.invokeEvent(BuildingEvent.DRAW_EVENT);
        assertEquals(oldFood,p.getFood());

        p.data.addInvention(Invention.BOWL);
        eventManager.invokeEvent(BuildingEvent.DRAW_EVENT);
        assertEquals(oldFood,p.getFood());

        p.data.addInvention(Invention.ARROWHEAD);
        eventManager.invokeEvent(BuildingEvent.DRAW_EVENT);
        assertEquals(oldFood + 3,p.getFood());
    }

}
