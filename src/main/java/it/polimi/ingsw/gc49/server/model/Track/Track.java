package it.polimi.ingsw.gc49.server.model.Track;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.server.model.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Track {
    private final List<Offer> offerBoard = new ArrayList<>();
    private final List<OrderSlot> orderBoard = new ArrayList<>();
    private int selectedOffer;
    private int incomingOffer;
    /** Stores the index of the order slot that is currently in use*/
    private int selectedOrderSlot;
    /** Stores the index of the following order slot to assign or deassign*/
    private int incomingOrderSlot;

    /**
     * Constructor which adapts creation to the number of players.
     * @param numOfPlayers the number of players for the game;
     */
    public Track ( int numOfPlayers ) {
        //### setting the "current" indexes at the beginning
        selectedOffer = 0;
        incomingOffer = 0;
        selectedOrderSlot = 0;
        incomingOrderSlot = 0;
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

    //### getters
    public List<Offer> getOfferBoard() { return offerBoard; }
    public List<OrderSlot> getOrderBoard() { return orderBoard; }
    public List<MockupPlayer> giveOfferBoardMockup() {
        return offerBoard.stream()
                .map(Offer::getAssignedPlayer)
                .filter(Objects::nonNull)
                .map(Player::giveMockupPlayer)
                .toList();
    }
    public List<MockupPlayer> giveOrderBoardMockup() {
        return orderBoard.stream()
                .map(OrderSlot::getAssignedPlayer)
                .filter(Objects::nonNull)
                .map(Player::giveMockupPlayer)
                .toList();
    }

    //### logic

    public void randomizeStartingOrder ( List<Player> players ) {
        List<Player> randomizedPlayers = players.stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(), collected -> {
                    Collections.shuffle(collected);
                    return collected;
                }));
        int orderSlotIterator = 0;
        for(Player player : randomizedPlayers) {
            orderBoard.get(orderSlotIterator).assignPlayer(player);
            orderSlotIterator++;
        }
    }

    public void assignOffer ( Player player, int offerIndex ) {
        offerBoard.get(offerIndex).assignPlayer(player);
    }

    /**
     * deassigns the current player from the offer, assigns him to the next order slot and activates the order slot's effect.
     */
    public void deassignCurrentOffer () {
        Player currentPlayer = offerBoard.get(selectedOffer).getAssignedPlayer();
        offerBoard.get(selectedOffer).assignPlayer(null); //deassigns.

        //assigns the current player to the next order slot and activates the order slot's effect.
        orderBoard.get(selectedOrderSlot).assignPlayer(currentPlayer);
        orderBoard.get(selectedOrderSlot).effectOnOccupation();

        //increases the order slot iterator, it should automatically become overwritten at the first call of getNextPlayerOrderSlot().
        selectedOrderSlot++;
    }

    /**
     * Gives the next player reference that is stored in the offer board and activates the offer's effects.
     * If it's about to index out of the array it's going to reset the index and return a null.
     * @return next player reference or a null if there is no next player.
     */
    public Player getNextPlayerOfferAndActivate () {
        while(offerBoard.get(incomingOffer).getAssignedPlayer() == null && incomingOffer < offerBoard.size()-1) { //cycles out all the empty offers
            incomingOffer++;
        }
        selectedOffer = incomingOffer;
        incomingOffer++;
        Offer currentOfferObject = offerBoard.get(selectedOffer);

        if (currentOfferObject.getAssignedPlayer() == null) { //reached the end and there is no player
            selectedOffer = 0;
            incomingOffer = 0;
            return null;
        }else{ //found a player and gives him the offer effects.
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
        if(incomingOrderSlot <= orderBoard.size()-1) {
            selectedOrderSlot = incomingOrderSlot; //selects the incoming order slot as the order slot to be used.
            incomingOrderSlot++;
            OrderSlot currentOrderSlotObject = orderBoard.get(selectedOrderSlot);
            if (currentOrderSlotObject.getAssignedPlayer() == null){ //logical exception used during disconnections
                selectedOffer = 0;
                incomingOrderSlot = 0;
                return null;
            }else{
                Player selectedPlayer = currentOrderSlotObject.getAssignedPlayer();

                //clears the references
                selectedPlayer.setAssignedOrderSlot(null);
                currentOrderSlotObject.assignPlayer(null);

                return selectedPlayer;
            }
        }else{ //surpassed the end of the order board
            incomingOrderSlot = 0;
            return null;
        }
    }
}
