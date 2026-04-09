package it.polimi.ingsw.gc49.model.Track;

import it.polimi.ingsw.gc49.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OrderSlotTest {

    @Test
    void testEffectOnOccupation () {
        int FOODGAIN = 3;
        int PAYMENT_PENALTY = 2;
        OrderSlot emptyOrderSlot = new OrderSlot();
        OrderSlot gainingOrderSlot = new OrderSlot(FOODGAIN);
        OrderSlot payingOrderSlot = new OrderSlot(FOODGAIN, PAYMENT_PENALTY);

        Player player = new Player("pippo", 0);

        emptyOrderSlot.assignPlayer(player);
        gainingOrderSlot.assignPlayer(player);
        payingOrderSlot.assignPlayer(player);

        //has initialized correctly?
        assertEquals(player.getFood(), 0);
        assertEquals(player.getPoints(), 0);

        //does nothing?
        emptyOrderSlot.effectOnOccupation();
        emptyOrderSlot.effectOnOccupation();
        assertEquals(player.getFood(), 0);
        assertEquals(player.getPoints(), 0);

        //gains food?
        gainingOrderSlot.effectOnOccupation();
        gainingOrderSlot.effectOnOccupation();
        assertEquals(player.getFood(), FOODGAIN * 2);

        //pays food? pays points if there's no food?
        payingOrderSlot.effectOnOccupation();
        payingOrderSlot.effectOnOccupation();
        assertEquals(player.getFood(), 0);
        assertEquals(player.getPoints(), 0);
        payingOrderSlot.effectOnOccupation();
        assertEquals(player.getFood(), 0);
        assertEquals(player.getPoints(), -PAYMENT_PENALTY);
        //even if there is an insufficient small amount of food?
        player.addFood(1);
        payingOrderSlot.effectOnOccupation();
        assertEquals(player.getFood(), 1);
        assertEquals(player.getPoints(), -PAYMENT_PENALTY * 2);
    }
}