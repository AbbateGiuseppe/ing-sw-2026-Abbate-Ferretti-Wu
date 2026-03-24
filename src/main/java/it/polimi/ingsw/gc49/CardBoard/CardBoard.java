package it.polimi.ingsw.gc49.CardBoard;

import it.polimi.ingsw.gc49.Card.BuildingCard.BuildingCard;
import it.polimi.ingsw.gc49.Card.Card;
import it.polimi.ingsw.gc49.Era;

import java.util.ArrayList;
import java.util.List;



public class CardBoard {

    private final Line line;
    private final ArrayList<Card> discards;
    private final Deck deck;

    public CardBoard(int numPlayers) {
        this.deck = new Deck(numPlayers);   // costruisci il mazzo completo
        this.line = new Line(numPlayers);   // se vuoi, puoi passare deck nel costruttore
        this.discards = new ArrayList<>();
    }

    public Card drawUpperCharacter(int index) {
        Card drawn = line.drawUpperCharacter(index);
        // niente gestione scarti qui: la carta viene presa dal giocatore
        return drawn;
    }

    public Card drawUpperBuilding(int index, int availableFood) {
        Card drawn = line.drawUpperBuilding(index, availableFood);
        return drawn;
    }


    public Card drawLowerCharacter(int index) {
        Card drawn = line.drawLowerCharacter(index);
        return drawn;
    }


    public Card drawLowerBuilding(int index, int availableFood) {
        Card drawn = line.drawLowerBuilding(index, availableFood);
        return drawn;
    }



    public Line getLine() {
        return line;
    }

    public List<Card> getDiscards() {
        return new ArrayList<>(discards);
    }

    public Deck getDeck() {
        return deck;
    }

    /**
     * Utility to add a card to the global discard pile.
     * Potrai chiamarlo da Line o da altri componenti se serve.
     */
    public void addToDiscards(Card card) {
        if (card != null) {
            discards.add(card);
        }
    }


    public boolean hasEraChanged() {
        return line.hasEraChanged();
    }

    public Era getNextEra() {
        return line.getNextEra();
    }


    /// tutti gli end da implementare nel finite state
    public void endGame() {
        line.endGame();
        // eventuale logica extra legata agli scarti o al deck
    }
    public void endRound(int numPlayers) {
        line.endRound(numPlayers);
    }

    public void endEra(Era newEra) {
        line.endEra(newEra);       // qui fai tutta la logica edifici/new era
        line.clearEraChange();
    }

}

