package it.polimi.ingsw.gc49;

import java.util.ArrayList;

public class Track {
    private final ArrayList<Offer> offerBoard = new ArrayList<>();
    private final ArrayList<OrderSlot> orderBoard = new ArrayList<>();
    private int currentOffer;
    private int nextOffer;
    private int currentOrderSlot;

    /**
     * Constructor which adapts creation to the number of players.
     * @param numOfPlayers the number of players for the game;
     */
    public Track ( int numOfPlayers ) {
        //### setting the "current" indexes at the beginning
        currentOffer = 0;
        nextOffer = 0;
        currentOrderSlot = 0;
        //### Creation of offerBoard
        switch (numOfPlayers){
            case 2:
                offerBoard.add(new Offer(0, 0, 1));
                offerBoard.add(new Offer(0, 1, 0));
                offerBoard.add(new Offer(0, 1, 1));
                offerBoard.add(new Offer(0, 2, 0));
                break;
            case 3:
                offerBoard.add(new Offer(0, 0, 1));
                offerBoard.add(new Offer(0, 1, 0));
                offerBoard.add(new Offer(0, 0, 2));
                offerBoard.add(new Offer(0, 1, 1));
                offerBoard.add(new Offer(0, 2, 0));
                break;
            case 4:
                offerBoard.add(new Offer(0, 0, 1));
                offerBoard.add(new Offer(0, 1, 0));
                offerBoard.add(new Offer(0, 0, 2));
                offerBoard.add(new Offer(0, 1, 1));
                offerBoard.add(new Offer(0, 2, 0));
                offerBoard.add(new Offer(0, 2, 1));
                break;
            case 5:
                offerBoard.add(new Offer(3, 0, 0));
                offerBoard.add(new Offer(0, 0, 1));
                offerBoard.add(new Offer(0, 1, 0));
                offerBoard.add(new Offer(0, 0, 2));
                offerBoard.add(new Offer(0, 1, 1));
                offerBoard.add(new Offer(0, 2, 0));
                offerBoard.add(new Offer(0, 2, 1));
                break;
            default: //ERRORE
        }

        //### Creation of orderBoard
        switch (numOfPlayers){
            case 2:
                orderBoard.add(new OrderSlot(1));
                orderBoard.add(new OrderSlot(1, 2));
                break;
            case 3:
                orderBoard.add(new OrderSlot(2));
                orderBoard.add(new OrderSlot());
                orderBoard.add(new OrderSlot(1, 2));
                break;
            case 4:
                orderBoard.add(new OrderSlot(2));
                orderBoard.add(new OrderSlot(1));
                orderBoard.add(new OrderSlot());
                orderBoard.add(new OrderSlot(1, 2));
                break;
            case 5:
                orderBoard.add(new OrderSlot(3));
                orderBoard.add(new OrderSlot(1));
                orderBoard.add(new OrderSlot());
                orderBoard.add(new OrderSlot());
                orderBoard.add(new OrderSlot(1, 2));
                break;
            default: //ERRORE
        }
    }

    //### TO IMPLEMENT!! RANDOMIZES THE PLAYERS TO AN ORDER SLOT.
    public void randomizeStartingOrder ( Player players[], int numOfPlayers ) {

    }

    public void assignOffer ( Player player, int offerIndex ) {
        offerBoard.get(offerIndex).assignPlayer(player);
    }

    public void deassignCurrentOffer () {
        offerBoard.get(currentOffer).assignPlayer(null);
    }

    /**
     * Gives the next player reference that is stored in the offer board.
     * If it's about to index out of the array it's going to reset the index and return a null.
     * @return next player reference or a null if there is no next player.
     */
    public Player getNextPlayerOfferAndActivate () {
        while(offerBoard.get(nextOffer).getAssignedPlayer() == null && nextOffer < offerBoard.size()-1) { //cycles out all the empty offers
            nextOffer++;
        }
        currentOffer = nextOffer;
        nextOffer++;
        Offer currentOfferObject = offerBoard.get(currentOffer);

        if (currentOfferObject.getAssignedPlayer() == null) { //reached the end and there is no player
            currentOffer = 0;
            nextOffer = 0;
            return null;
        }else{
            currentOfferObject.activate();
            return currentOfferObject.getAssignedPlayer();
        }
    }

    /**
     * gives the next player reference that is stored in the order board.
     * If it's about to index out of the array it's going to reset the index and return a null.
     * @return next player reference or a null if there is no next player.
     */
    public Player getNextPlayerOrderSlot () {
        if(currentOrderSlot <= orderBoard.size()-1) {
            OrderSlot currentOrderSlotObject = orderBoard.get(currentOrderSlot);
            currentOrderSlot++;
            if (currentOrderSlotObject.getAssignedPlayer() == null){ //logical exception used during disconnections
                currentOrderSlot = 0;
                return null;
            }else{
                return currentOrderSlotObject.getAssignedPlayer();
            }
        }else{ //surpassed the end of the order board
            currentOrderSlot = 0;
            return null;
        }
    }
}
