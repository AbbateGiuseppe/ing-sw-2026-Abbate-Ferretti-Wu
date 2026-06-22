package it.polimi.ingsw.gc49.server.model;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Track.OrderSlot;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * The {@code Player} class represents the server-side model of a game participant.
 * It holds the definitive state of a player's resources (food, points), their inventory
 * (character and building cards), and their current status within a turn (actions left to do,
 * connection status, and track position).
 * It also encapsulates a {@link DataBank} to keep track of complex statistics and effects.
 */
public class Player implements Serializable {
    /** The unique identifier chosen by the player. */
    private final String nickname;

    /** The index representing the player's position in the game's internal arrays. */
    private final int playerIndex;

    /** Indicates whether the player is currently connected to the server. */
    private boolean connected;

    /** * Status flag for a disconnection event.
     * If true, the player has been indefinitely removed from the board and won't be
     * given the chance to be chosen for the following turns, until they reconnect.
     */
    private boolean removedFromTrack;

    /** The specific slot on the order track currently occupied by this player. */
    private OrderSlot assignedOrderSlot;

    /** The totem assigned to the player. */
    private Totem totem;

    /** The current amount of food the player has. */
    private int food;

    /** The current amount of victory points the player has. */
    private int points;

    /** The list of character cards the player currently holds/has played. */
    private final List<Card> characterCards;

    /** The list of building cards the player currently holds/has played. */
    private final List<Card> buildingCards;

    /** The personal ledger managing statistics, discounts, and scientific inventions. */
    public final DataBank data;

    // Event Management

    /** Temporary accumulator for food that the player is required to pay. */
    private int foodToPay;

    /** Temporary accumulator for points that the player will lose (e.g., due to starvation). */
    private int pointsToPay;

    /** Flag indicating if this player triggered a unique win condition. */
    private boolean uniqueWinner;

    // Action Management

    /** The number of upper line cards this player is currently allowed to draw. */
    private int drawableUpper;

    /** The number of lower line cards this player is currently allowed to draw. */
    private int drawableLower;

    /** Flag indicating if the player has already chosen an offer during the current phase. */
    private boolean choseAnOffer;
    /**
     *
     * @param nickname the player's nickname;
     * @param playerIndex this player index in the model's array of players;
     */
    public Player ( String nickname, int playerIndex ) {
        this.nickname = nickname;
        this.playerIndex = playerIndex;

        food = 0;
        points = 0;
        characterCards = new ArrayList<>();
        buildingCards = new ArrayList<>();

        connected = true;
        removedFromTrack = false;
        choseAnOffer = false;

        drawableUpper = 0;
        drawableLower = 0;

        data = new DataBank(this);
    }

    // ============================================================
    // ### ADDERS
    // ============================================================

    public void addFood (int addedFood) {
        food = food + addedFood;
    }
    public void addPoints (int addedPoints) {
        points = points + addedPoints;
    }
    public void addCharacterCard ( Card card ) {
        characterCards.add(card);
        card.updateDataBank(data);
        card.onDraw(this);
    }
    public void addBuildingCard ( Card card ) {
        buildingCards.add(card);
        card.updateDataBank(data);
        card.onDraw(this);
    }

    // ============================================================
    // ### SETTERS
    // ============================================================

    public void setFood (int food) {
        this.food = food;
    }
    public void setPoints (int points) {
        this.points = points;
    }
    public void setDrawableUpper (int drawableUpper) {
        this.drawableUpper = drawableUpper;
    }
    public void setDrawableLower (int drawableLower) {
        this.drawableLower = drawableLower;
    }
    public void setTotem (Totem totem) {
        this.totem = totem;
    }
    public void setAssignedOrderSlot (OrderSlot assignedOrderSlot) {this.assignedOrderSlot = assignedOrderSlot;}
    public void setRemovedFromTrack (boolean removedFromTrack) {this.removedFromTrack = removedFromTrack;}
    public void setChoseAnOffer(boolean choseAnOffer) {this.choseAnOffer = choseAnOffer;}

    // ============================================================
    // ### GETTERS
    // ============================================================

    public String getNickname () {
        return nickname;
    }
    public int getPlayerIndex () {
        return playerIndex;
    }
    public Totem getTotem() {
        return totem;
    }
    public OrderSlot getAssignedOrderSlot() { return assignedOrderSlot; }
    public int getFood () {
        return food;
    }
    public int getPoints () {
        return points;
    }
    public int getDrawableUpper () {
        return drawableUpper;
    }
    public int getDrawableLower () {
        return drawableLower;
    }
    public MockupPlayer giveMockupPlayer() {
        return new MockupPlayer(
                    nickname,
                    playerIndex,
                    connected,
                    totem,
                    food,
                    points,
                    characterCards,
                    buildingCards,
                    drawableUpper,
                    drawableLower
        );
    }

    // ============================================================
    // ### EVENT MANAGEMENT
    // ============================================================

    public int getFoodToPay() {
        return foodToPay;
    }
    public void setFoodToPay(int foodToPay) {
        this.foodToPay = foodToPay;
    }
    public int getPointsToPay() {
        return pointsToPay;
    }
    public void setPointsToPay(int pointsToPay) {
        this.pointsToPay = pointsToPay;
    }
    public boolean isUniqueWinner() {return uniqueWinner;}
    public void setUniqueWinner(boolean uniqueWinner) {this.uniqueWinner = uniqueWinner;}
    public void confirmToPay () {food -= foodToPay;points -= pointsToPay;reset();}
    private void reset() {foodToPay = 0; pointsToPay = 0; uniqueWinner = false;}



    //### connective and exceptional states
    public boolean isConnected () {
        return connected;
    }
    public void setConnected (boolean connected) {
        this.connected = connected;
    }
    public boolean isRemovedFromTrack () {
        return removedFromTrack;
    }

    //### actions' management and checks

    /**
     * Tells the caller if this player has some actions remaining.
     * @return true if the player has still actions left to do.
     */
    public boolean hasActionsLeft () {
        boolean hasNoActionsLeft = (drawableUpper==0 && drawableLower==0);
        return !hasNoActionsLeft;
    }

    /**
     * Cleans the remaining actions of the player.
     */
    public void cleanRemainingActions () {
        drawableUpper = 0;
        drawableLower = 0;
    }

    /**
     * Tells the caller if this player has chosen an offer yet.
     * @return true if the player has chosen an offer.
     */
    public boolean hasChosenAnOffer () {
        return choseAnOffer;
    }
}


