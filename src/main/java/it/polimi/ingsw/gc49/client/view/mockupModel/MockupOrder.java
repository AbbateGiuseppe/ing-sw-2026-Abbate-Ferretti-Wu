package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.client.view.Rectangable;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import org.jline.utils.AttributedStringBuilder;

import java.io.Serializable;


/**
 * The {@code MockupOrder} class represents a single slot on the turn order track
 * within the game's client-side mockup.
 * It tracks which player is occupying the slot and visually represents the
 * rewards (food gain) or penalties (food to pay / points lost on starvation)
 * associated with taking this specific turn order.
 * It implements {@link Rectangable} to allow the text-based user interface to render it as a styled ASCII string.
 */
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


    /**
     * Constructs a new {@code MockupOrder} with the specified costs, rewards, and initial state.
     *
     * @param foodGain                  the amount of food provided by the slot.
     * @param payFood                   {@code true} if occupying this slot requires paying food.
     * @param foodToPay                 the amount of food to pay (if applicable).
     * @param removedPointsOnStarvation the points lost if the player fails to pay the food cost.
     * @param assignedPlayerIndex       the index of the player assigned to this slot (can be {@code null}).
     */
    public MockupOrder(int foodGain, boolean payFood, int foodToPay, int removedPointsOnStarvation, Integer assignedPlayerIndex) {
        this.foodGain = foodGain;
        this.payFood = payFood;
        this.foodToPay = foodToPay;
        this.removedPointsOnStarvation = removedPointsOnStarvation;
        this.assignedPlayerIndex = assignedPlayerIndex;
    }


    // ============================================================
    // ### SETTERS
    // ============================================================

    /**
     * Updates the player assigned to this order slot.
     *
     * @param assignedPlayerIndex the index of the new player, or {@code null} to unassign.
     */
    public void setAssignedPlayerIndex(Integer assignedPlayerIndex) {
        this.assignedPlayerIndex = assignedPlayerIndex;
    }
    /**
     * Sets the reference to the parent game mockup.
     * Essential for retrieving the assigned player's totem color during rendering.
     *
     * @param game the {@link MockupGame} instance holding this order slot.
     */
    public void setGame(MockupGame game) { this.game = game; }

    // ============================================================
    // ### GETTERS
    // ============================================================

    /**
     * Retrieves the index of the player currently assigned to this order slot.
     *
     * @return the player's index, or {@code null} if the slot is vacant.
     */
    public Integer getAssignedPlayerIndex() {
        return assignedPlayerIndex;
    }

    // ============================================================
    // ### RENDERING
    // ============================================================

    /**
     * Generates a compact, 1-line string representation of this order slot using ASCII characters and ANSI colors.
     * The resulting string is always exactly 8 characters wide (height: 1, width: 8).
     * <p>
     * Examples of output formats:
     * <ul>
     * <li>Empty with food gain: {@code "░ 3♥    "}</li>
     * <li>Player occupied with starvation penalty: {@code "[Color]-1♥/-2♦"}</li>
     * <li>Empty slot with no effects: {@code "░       "}</li>
     * </ul>
     *
     * @return a {@link RectangleAttributedString} containing the stylized 1x8 string.
     */

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
