package it.polimi.ingsw.gc49.server.model.Track;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOffer;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOrder;
import it.polimi.ingsw.gc49.server.model.Player;

import java.io.Serializable;

public class OrderSlot implements Serializable {
    /** The player currently assigned to the order slot*/
    private Player assignedPlayer;
    /** The food gained on non-setup occupation*/
    private final int foodGain;
    /** A boolean to know if this is a pay-demanding order slot. See {@link #effectOnOccupation()} for further details.*/
    private final boolean payFood;
    /** The food needed to be paid on non-setup occupation*/
    private final int foodToPay;
    /** The points removed if the food wasn't paid on non-setup occupation*/
    private final int removedPointsOnStarvation;

    /**
     * Constructor for empty order slot.
     */
    public OrderSlot () {
        foodGain = 0;
        payFood = false;
        foodToPay = 0;
        removedPointsOnStarvation = 0;
    }
    /**
     * Constructor for food-gaining order slot.
     * @param foodGain the food gained on occupation;
     */
    public OrderSlot ( int foodGain ) {
        this.foodGain = foodGain;
        payFood = false;
        foodToPay = 0;
        removedPointsOnStarvation = 0;
    }
    /**
     * Constructor for food-paying order slot.
     * @param foodToPay the food needed to be paid on occupation;
     * @param removedPointsOnStarvation the points removed if the food wasn't paid;
     */
    public OrderSlot ( int foodToPay, int removedPointsOnStarvation ) {
        this.foodGain = 0;
        payFood = true;
        this.foodToPay = foodToPay;
        this.removedPointsOnStarvation = removedPointsOnStarvation;
    }

    //### assignment and effect on an assignment ###
    /**
     * Assigns the player to the order slot,
     * can also deassign the currently assigned player by passing a null.
     * @param assignedPlayer the newly assigned player;
     */
    public void assignPlayer ( Player assignedPlayer ) {
        this.assignedPlayer = assignedPlayer;
        if(assignedPlayer != null) {
            assignedPlayer.setAssignedOrderSlot(this);
        }
    }

    /**
     * applies the order slot effects on its current occupant (assignedPlayer).
     */
    public void effectOnOccupation () {
        assignedPlayer.addFood(foodGain);

        if(payFood){
            if(assignedPlayer.getFood() < foodToPay){
                assignedPlayer.addPoints( -removedPointsOnStarvation );
            } else {
                assignedPlayer.addFood( -foodToPay );
            }
        }
    }

    //### getters
    public Player getAssignedPlayer() {
        return assignedPlayer;
    }
    public int getFoodGain() { return foodGain; }

    public MockupOrder giveOrderMockup() {
        if(assignedPlayer != null) {
            return new MockupOrder(foodGain, payFood, foodToPay, removedPointsOnStarvation, assignedPlayer.giveMockupPlayer());
        }else{
            return new MockupOrder(foodGain, payFood, foodToPay, removedPointsOnStarvation, null);
        }
    }
}
