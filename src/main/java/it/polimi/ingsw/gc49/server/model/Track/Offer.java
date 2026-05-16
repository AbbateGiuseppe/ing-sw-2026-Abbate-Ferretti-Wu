package it.polimi.ingsw.gc49.server.model.Track;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOffer;
import it.polimi.ingsw.gc49.server.model.Player;

import java.io.Serializable;

public class Offer implements Serializable {
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
     * can't overwrite the offer if there's already an assigned player.
     * @param assignedPlayer the newly assigned player;
     * @throws NotValidOfferException if there's already an assigned player
     */
    public void assignPlayer ( Player assignedPlayer ) throws NotValidOfferException {
        if(this.assignedPlayer != null) {
            throw new NotValidOfferException("C'è già un giocatore sull'offerta selezionata.");
        }else{
            this.assignedPlayer = assignedPlayer;
        }
    }

    /**
     * Removes the assignedPlayer from the offer.
     */
    public void deassignPlayer () {
        this.assignedPlayer = null;
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

    public MockupOffer giveOfferMockup() {
        if(assignedPlayer != null) {
            return new MockupOffer(foodGain, upperDraw, lowerDraw, assignedPlayer.getPlayerIndex());
        }else{
            return new MockupOffer(foodGain, upperDraw, lowerDraw, null);
        }
    }
}
