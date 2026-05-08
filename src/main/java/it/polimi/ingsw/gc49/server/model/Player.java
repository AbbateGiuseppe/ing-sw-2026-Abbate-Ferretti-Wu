package it.polimi.ingsw.gc49.server.model;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Track.OrderSlot;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final String nickname;
    private final int playerIndex;
    private boolean connected;
    /** status for a disconnection event where the player has been indefinitely removed from the board and won't be given the chance to be chosen for the following turns, until he comes back*/
    private boolean removedFromTrack;
    private OrderSlot assignedOrderSlot;
    private Totem totem;
    private int food;
    private int points;
    private final List<Card> characterCards;
    private final List<Card> buildingCards;

    public final DataBank data;

    // Event Management
    private int foodToPay;
    private int pointsToPay;
    private boolean uniqueWinner;

    private int drawableUpper;
    private int drawableLower;
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

    //### adders
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

    //### setters
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

    //### getters
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
                    food,
                    points
        );
    }

    // Event Management
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


