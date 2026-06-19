package it.polimi.ingsw.gc49.server.model.Track;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOffer;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupOrder;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.playerExceptions.NotValidOfferException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class Track implements Serializable {
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
    public List<MockupOffer> giveOfferBoardMockup() {
        List<MockupOffer> mockupOffers = new ArrayList<>();
        for(Offer offer : offerBoard){
            mockupOffers.add(offer.giveOfferMockup());
        }
        return mockupOffers;
    }
    public List<MockupOrder> giveOrderBoardMockup() {
        List<MockupOrder> mockupOrders = new ArrayList<>();
        for(OrderSlot orderSlot : orderBoard){
            mockupOrders.add(orderSlot.giveOrderMockup());
        }
        return mockupOrders;
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
            switch (orderSlotIterator){
                case 0 -> player.setFood(2);
                case 1, 2 -> player.setFood(3);
                case 3, 4 -> player.setFood(4);
            }
            orderBoard.get(orderSlotIterator).assignPlayer(player);
            orderSlotIterator++;
        }
    }

    public void assignOffer ( Player player, int offerIndex ) throws NotValidOfferException {
        Offer chosenOffer;
        try {
            chosenOffer = offerBoard.get(offerIndex);
        } catch (IndexOutOfBoundsException e) {
            throw new NotValidOfferException("L'indice inserito sfora.");
        }
        chosenOffer.assignPlayer(player);
    }

    /**
     * deassigns the current player from the offer, assigns him to the next order slot and activates the order slot's effect.
     */
    public void deassignCurrentOffer () {
        Player currentPlayer = offerBoard.get(selectedOffer).getAssignedPlayer();
        if ( currentPlayer != null ) {
            offerBoard.get(selectedOffer).deassignPlayer(); //deassigns.

            //assigns the current player to the next order slot and activates the order slot's effect.
            while(orderBoard.get(selectedOrderSlot).getAssignedPlayer() != null) selectedOrderSlot++; //special case for skipping reconnected players sitting on the orderBoard
            orderBoard.get(selectedOrderSlot).assignPlayer(currentPlayer);
            orderBoard.get(selectedOrderSlot).effectOnOccupation();

            //increases the order slot iterator, it should automatically become overwritten at the first call of getNextPlayerOrderSlot().
            selectedOrderSlot++;
        }
    }

    /**
     * Gives the next player reference that is stored in the offer board and activates the offer's effects.
     * If it's about to index out of the array it's going to reset the index and return a null.
     * @return next player reference or a null if there is no next player.
     */
    public Player getNextPlayerOfferAndActivate () {
        while( incomingOffer < offerBoard.size()-1 && offerBoard.get(incomingOffer).getAssignedPlayer() == null ) { //cycles out all the empty offers
            incomingOffer++;
        }
        if ( incomingOffer <= offerBoard.size()-1 && offerBoard.get(incomingOffer).getAssignedPlayer() != null ) { //if a player was found
            selectedOffer = incomingOffer;
            incomingOffer++;

            Offer currentOfferObject = offerBoard.get(selectedOffer);

            //found a player and gives him the offer effects.
            currentOfferObject.activate();
            return currentOfferObject.getAssignedPlayer();

        } else {
            //reached the end
            selectedOffer = 0;
            incomingOffer = 0;
            return null;
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

            if (currentOrderSlotObject.getAssignedPlayer() == null){ //logical exception used during disconnections, where there'd be some empty slots at the end
                selectedOrderSlot = 0;
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
            selectedOrderSlot = 0;
            incomingOrderSlot = 0;
            return null;
        }
    }

    public void deassignCurrentOrderSlot ( Player deassignedPlayer ) {
        if ( deassignedPlayer != null ) {
            deassignedPlayer.setAssignedOrderSlot(null);
            orderBoard.get(selectedOrderSlot).assignPlayer(null);
        }
    }
}
