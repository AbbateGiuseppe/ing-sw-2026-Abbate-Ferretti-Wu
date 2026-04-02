package it.polimi.ingsw.gc49.model.Track;

import it.polimi.ingsw.gc49.model.Player;

public class Offer {
    /** The player currently assigned to the offer.*/
    private Player assignedPlayer;
    /** The food gained on effect activation.*/
    private final int foodGain;
     /** The upper line drawable cards gained on effect activation.*/
    private final int upperDraw;
     /** The lower line drawable cards on effect activation.*/
    private final int lowerDraw;

    /**
     * Constructor which adds the effects the offer gives.
     * @param foodGain food gained on effect activation;
     * @param upperDraw upper line drawable cards on effect activation;
     * @param lowerDraw lower line drawable cards on effect activation;
     */
    public Offer ( int foodGain, int upperDraw, int lowerDraw ) {
        this.foodGain = foodGain;
        this.upperDraw = upperDraw;
        this.lowerDraw = lowerDraw;
    }

    /**
     * Assigns the player to the offer,
     * can also deassign the currently assigned player by passing a null.
     * @param assignedPlayer the newly assigned player;
     */
    public void assignPlayer ( Player assignedPlayer ) {
        this.assignedPlayer = assignedPlayer;
    }

    /**
     * Gives the player, who is assigned to the offer, all the effects contained
     */
    public void activate () {
        assignedPlayer.addFood(foodGain);
        assignedPlayer.setDrawableUpper(upperDraw);
        assignedPlayer.setDrawableLower(lowerDraw);
    }

    public Player getAssignedPlayer() {
        return assignedPlayer;
    }
}
