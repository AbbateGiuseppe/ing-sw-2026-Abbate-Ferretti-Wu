package it.polimi.ingsw.gc49.View.mockupModel;

import it.polimi.ingsw.gc49.model.Card.Card;
import it.polimi.ingsw.gc49.model.Era;

import java.util.ArrayList;
import java.util.List;

public class MockupGame {
    private MockupPlayer currentPlayer;
    private int currentPlayerIndex;
    private final List<MockupPlayer> players;

    /**List that keeps count of the discarded cards, adds new ones everytime.*/
    private final List<Card> discards;
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

    private List<MockupPlayer> offerBoard;
    private List<MockupPlayer> orderBoard;

    /**[constructor] Should be initialized AFTER the real game has been initialized on the server!*/
    public MockupGame (List<MockupPlayer> players, Era deckTopEra, List<Card> upperLine, List<Card> lowerLine
    , List<Card> upperBuilding, List<Card> lowerBuilding, List<MockupPlayer> offerBoard, List<MockupPlayer> orderBoard) {
        this.players = players;
        this.discards = new ArrayList<>();
        this.deckTopEra = deckTopEra;
        this.upperLine = upperLine;
        this.lowerLine = lowerLine;
        this.upperBuilding = upperBuilding;
        this.lowerBuilding = lowerBuilding;
        this.offerBoard = offerBoard;
        this.orderBoard = orderBoard;
    }

    //### setters
    public void setCurrentPlayer (MockupPlayer currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    public void setCurrentPlayerIndex (int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }
    //Cardboard
    public List<Card> getDiscards() {
        return discards;
    }
    public Era getDeckTopEra() {
        return deckTopEra;
    }
    public List<Card> getUpperLine() {
        return upperLine;
    }
    public List<Card> getLowerLine() {
        return lowerLine;
    }
    public List<Card> getUpperBuilding() {
        return upperBuilding;
    }
    public List<Card> getLowerBuilding() {
        return lowerBuilding;
    }
    //Track
    public void setOfferBoard(List<MockupPlayer> offerBoard) {
        this.offerBoard = offerBoard;
    }
    public void setOrderBoard(List<MockupPlayer> orderBoard) {
        this.orderBoard = orderBoard;
    }

    //### getters
    public MockupPlayer getCurrentPlayer () {
        return currentPlayer;
    }
    public int getCurrentPlayerIndex () {
        return currentPlayerIndex;
    }
    public List<MockupPlayer> getPlayers () {
        return players;
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
    //Track
    public List<MockupPlayer> getOfferBoard() {
        return offerBoard;
    }
    public List<MockupPlayer> getOrderBoard() {
        return orderBoard;
    }

    //### adders
    //Cardboard
    public void addDiscards(List<Card> discards) {
        this.discards.addAll(discards);
    }
}
