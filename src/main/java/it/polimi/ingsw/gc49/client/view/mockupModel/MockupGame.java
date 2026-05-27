package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Era;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MockupGame implements Serializable {
    private int currentPlayerIndex;
    private List<MockupPlayer> players;

    /**List that keeps count of the discarded cards, adds new ones everytime.*/
    private final List<Card> discards = new ArrayList<>();
    /**Stores the era on the back of the card on top of the deck*/
    private Era deckTopEra;
    /**Gets replaced with a new List at every line update*/
    private List<Card> upperLine;
    /**Gets replaced with a new List at every line update*/
    private List<Card> lowerLine;
    /**Gets replaced with a new List at every line update*/
    private List<Card> upperBuilding;
    /**Gets replaced with a new List at every line update*/
    private List<Card> lowerBuilding;

    private final List<MockupOffer> offerBoard;
    private final List<MockupOrder> orderBoard;

    /**[constructor] Should be initialized AFTER the real game has been initialized on the server!*/
    public MockupGame (List<MockupPlayer> players, Era deckTopEra, List<Card> upperLine, List<Card> lowerLine
    , List<Card> upperBuilding, List<Card> lowerBuilding, List<MockupOffer> offerBoard, List<MockupOrder> orderBoard) {
        this.players = players;
        this.deckTopEra = deckTopEra;
        this.upperLine = upperLine;
        this.lowerLine = lowerLine;
        this.upperBuilding = upperBuilding;
        this.lowerBuilding = lowerBuilding;
        this.offerBoard = offerBoard;
        for(MockupOffer offer : offerBoard) {
            offer.setGame(this);
        }
        this.orderBoard = orderBoard;
        for(MockupOrder order : orderBoard) {
            order.setGame(this);
        }
    }

    //### setters
    //Players
    public void setPlayers (List<MockupPlayer> players) { this.players = players; }
    public void setCurrentPlayerIndex (int currentPlayerIndex) {
        players.get(this.currentPlayerIndex).setOfTurn(false);
        this.currentPlayerIndex = currentPlayerIndex;
        players.get(this.currentPlayerIndex).setOfTurn(true);
    }
    //Cardboard
    public void setDeckTopEra(Era deckTopEra) {
        this.deckTopEra = deckTopEra;
    }
    public void setUpperLine(List<Card> upperLine) {
        this.upperLine = upperLine;
    }
    public void setLowerLine(List<Card> lowerLine) {
        this.lowerLine = lowerLine;
    }
    public void setUpperBuilding(List<Card> upperBuilding) {
        this.upperBuilding = upperBuilding;
    }
    public void setLowerBuilding(List<Card> lowerBuilding) {
        this.lowerBuilding = lowerBuilding;
    }

    /**
     * Changes the player occupying an offer, can also assign null (deassign).
     * @param offerIndex the index of the offer on the offerBoard
     * @param playerIndex the index of the player to assign
     */
    public void setOfferPlayerIndex(int offerIndex, Integer playerIndex) {
        if(offerBoard.get(offerIndex) != null){
            offerBoard.get(offerIndex).setAssignedPlayerIndex(playerIndex);
        }
    }
    /**
     * Changes the player occupying an orderSlot, can also assign null (deassign).
     * @param orderIndex the index of the orderSlot on the orderBoard
     * @param playerIndex the index of the player to assign
     */
    public void setOrderPlayerIndex(int orderIndex, Integer playerIndex) {
        if(orderBoard.get(orderIndex) != null){
            orderBoard.get(orderIndex).setAssignedPlayerIndex(playerIndex);
        }
    }

    //### getters
    //Players
    public int getCurrentPlayerIndex () {
        return currentPlayerIndex;
    }
    public List<MockupPlayer> getPlayers () {
        return Collections.unmodifiableList(players);
    }
    public MockupPlayer getPlayer(int i) {return players.get(i);}
    //Cardboard
    public List<Card> getDiscards() {
        return Collections.unmodifiableList(discards);
    }
    public Era getDeckTopEra() {
        return deckTopEra;
    }
    public List<Card> getUpperLine() {
        return Collections.unmodifiableList(upperLine);
    }
    public List<Card> getLowerLine() {
        return Collections.unmodifiableList(lowerLine);
    }
    public List<Card> getUpperBuilding() {
        return Collections.unmodifiableList(upperBuilding);
    }
    public List<Card> getLowerBuilding() {
        return Collections.unmodifiableList(lowerBuilding);
    }
    //Track
    public List<MockupOrder> getOrderBoard () {
        return Collections.unmodifiableList(orderBoard);
    }
    public List<RectangleAttributedString> getOrderBoardRectangleStrings() {
        return orderBoard
                .stream()
                .map(MockupOrder::getRectangleAttributedString)
                .toList();
    }
    public List<MockupOffer> getOfferBoard() {
        return Collections.unmodifiableList(offerBoard);
    }
    public List<RectangleAttributedString> getOfferBoardRectangleStrings(){
        return offerBoard
                .stream()
                .map(MockupOffer::getRectangleAttributedString)
                .toList();
    }

    //### adders
    //Cardboard
    public void addDiscards(List<Card> discards) {
        this.discards.addAll(discards);
    }
}
