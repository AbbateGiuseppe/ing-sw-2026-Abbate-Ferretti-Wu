package it.polimi.ingsw.gc49.model.CardBoard;

import it.polimi.ingsw.gc49.model.Card.Card;
import it.polimi.ingsw.gc49.model.Era;
import it.polimi.ingsw.gc49.model.Game;
import it.polimi.ingsw.gc49.model.Player;
import it.polimi.ingsw.gc49.model.States.DeckEmptiedException;
import it.polimi.ingsw.gc49.model.States.EraEndedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



public class CardBoard implements Serializable {

    private final Line line;
    private final List<Card> discards;
    private final Deck deck;
    private final Game game;
    private int numPlayers;
/// chiedere come mettere playerlist



    public CardBoard( Game game ) {
        this.game = game;
        this.numPlayers = game.getNumOfPlayers();
        this.deck = new Deck(game);   // costruisci il mazzo completo
        this.line = new Line(game, deck);   // se vuoi, puoi passare deck nel costruttore
        this.discards = new ArrayList<>();
    }

    public Card drawUpperCharacter(int cardIndex, Player drawingPlayer) {
        Card drawn = line.drawUpperCharacter(cardIndex, drawingPlayer);
        // niente gestione scarti qui: la carta viene presa dal giocatore
        return drawn;
    }

    public Card drawLowerCharacter(int cardIndex, Player drawingPlayer) {
        Card drawn = line.drawLowerCharacter(cardIndex, drawingPlayer);
        return drawn;
    }


    public Card drawUpperBuilding(int cardIndex, Player drawingPlayer) {
        Card drawn = line.drawUpperBuilding(cardIndex, drawingPlayer);
        return drawn;
    }

    public Card drawLowerBuilding(int cardIndex, Player drawingPlayer) {
        Card drawn = line.drawLowerBuilding(cardIndex, drawingPlayer);
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
        return line.getNewEra();
    }


    /// tutti gli end da implementare nel finite state
    public void endGame() {
        line.endGame();
        // eventuale logica extra legata agli scarti o al deck
    }
    public void endRound (int numPlayers) throws EraEndedException {
        try {
            line.endRound(numPlayers);
        } catch ( EraEndedException e ) {
            throw new EraEndedException("We have changed the era.");
        }
    }

    public void endEra(Era newEra) throws DeckEmptiedException {
        line.endEra();       // qui fai tutta la logica edifici/new era
        line.clearEraChange();
        if(newEra.isFinal()){
            throw new DeckEmptiedException("We have emptied the deck.");
        }
    }

}

