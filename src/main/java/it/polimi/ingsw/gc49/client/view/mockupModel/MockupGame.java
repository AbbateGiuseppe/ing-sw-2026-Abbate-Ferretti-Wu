package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Era;

import java.io.Serializable;
import java.util.ArrayList;
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
        this.orderBoard = orderBoard;
    }

    //### setters
    //Players
    public void setPlayers (List<MockupPlayer> players) { this.players = players; }
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

    //### getters
    //Players
    public int getCurrentPlayerIndex () {
        return currentPlayerIndex;
    }
    public List<MockupPlayer> getPlayers () {
        return players;
    }
    public MockupPlayer getPlayer(int i) {return players.get(i);}
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
    public List<MockupOffer> getOfferBoard() {
        return offerBoard;
    }
    public List<MockupOrder> getOrderBoard() {
        return orderBoard;
    }

    //### adders
    //Cardboard
    public void addDiscards(List<Card> discards) {
        this.discards.addAll(discards);
    }
}
