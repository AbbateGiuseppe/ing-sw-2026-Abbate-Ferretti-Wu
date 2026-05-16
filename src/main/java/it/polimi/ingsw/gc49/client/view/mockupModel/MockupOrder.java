package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.client.view.Rectangable;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import org.jline.utils.AttributedStringBuilder;

import java.io.Serializable;

public class MockupOrder implements Serializable, Rectangable {
    private MockupGame game;
    /** The player currently assigned to the order slot*/
    private Integer assignedPlayerIndex;
    /** The food gained on non-setup occupation*/
    private final int foodGain;
    /** A boolean to know if this is a pay-demanding order slot.*/
    private final boolean payFood;
    /** The food needed to be paid on non-setup occupation*/
    private final int foodToPay;
    /** The points removed if the food wasn't paid on non-setup occupation*/
    private final int removedPointsOnStarvation;

    public MockupOrder(int foodGain, boolean payFood, int foodToPay, int removedPointsOnStarvation, Integer assignedPlayerIndex) {
        this.foodGain = foodGain;
        this.payFood = payFood;
        this.foodToPay = foodToPay;
        this.removedPointsOnStarvation = removedPointsOnStarvation;
        this.assignedPlayerIndex = assignedPlayerIndex;
    }


    //###setters
    public void setAssignedPlayerIndex(Integer assignedPlayerIndex) {
        this.assignedPlayerIndex = assignedPlayerIndex;
    }
    public void setGame(MockupGame game) { this.game = game; }

    //###getters
    public Integer getAssignedPlayerIndex() {
        return assignedPlayerIndex;
    }

    /**
     * "░ 3♥    "
     * or
     * "░-1♥/-2♦"
     */
    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        int height = 1;
        int width = 8;

        AttributedStringBuilder attributedString = new AttributedStringBuilder();

        //Add the player totem
        if(assignedPlayerIndex == null) {
            attributedString.append("░");
        }else{
            //Adds color based on player's totem color
            attributedString.append(game.getPlayer(assignedPlayerIndex).getTotem().getTotemAttributedString());
        }

        //Add the food to pay and the points to pay
        if(payFood){
            attributedString.append("-").append(String.valueOf(foodToPay)).append("♥/")
                    .append("-").append(String.valueOf(removedPointsOnStarvation)).append("♦");
        }else{
            //Add the food gain
            if(foodGain>0){
                attributedString.append(" ").append(String.valueOf(foodGain)).append("♥").append("    ");
            }
            //Add empty order slot
            else{
                attributedString.append("       ");
            }
        }

        return new RectangleAttributedString(height, width, attributedString.toAttributedString());
    }
}
