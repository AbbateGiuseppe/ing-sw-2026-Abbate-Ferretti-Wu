package it.polimi.ingsw.gc49.View.mockupModel;

import it.polimi.ingsw.gc49.model.Card.Card;
import it.polimi.ingsw.gc49.model.Era;

import java.util.ArrayList;
import java.util.List;

public class MockupCardBoard {
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

    /**Wants a copy of each one of the card lines*/
    public MockupCardBoard(List<Card> upperLine, List<Card> lowerLine, List<Card> upperBuilding, List<Card> lowerBuilding) {
        this.discards = new ArrayList<>();
        this.upperLine = upperLine;
        this.lowerLine = lowerLine;
        this.upperBuilding = upperBuilding;
        this.lowerBuilding = lowerBuilding;
    }

    //### getters
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

    //### setters
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

    //### adders
    public void addDiscards(List<Card> discards) {
        this.discards.addAll(discards);
    }
}
