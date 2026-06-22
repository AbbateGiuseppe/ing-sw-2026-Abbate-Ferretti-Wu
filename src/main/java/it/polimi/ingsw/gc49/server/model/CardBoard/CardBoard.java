package it.polimi.ingsw.gc49.server.model.CardBoard;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.States.DeckEmptiedException;
import it.polimi.ingsw.gc49.server.model.States.EraEndedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Manages the main card game board.
 * Coordinates core components such as the deck (Deck), the line of available cards (Line),
 * and the discard pile. It also provides methods for managing Eras and rounds
 * within the game's Finite State Machine (FSM).
 */
public class CardBoard implements Serializable {

    private final Line line;
    private final List<Card> discards;
    private final Deck deck;
    private final int numPlayers;

    /**
     * Constructs a new game board, initializing the deck, the card line, and the discards.
     *
     * @param game The main game instance from which to retrieve the number of players.
     */
    public CardBoard( Game game ) {
        this.numPlayers = game.getNumOfPlayers();
        this.deck = new Deck(game);   // costruisci il mazzo completo
        this.line = new Line(game, deck);   // se vuoi, puoi passare deck nel costruttore
        this.discards = new ArrayList<>();
    }


    /**
     * Draws a Character card from the upper row of the line.
     *
     * @param cardIndex     The index of the card to draw.
     * @param drawingPlayer The player performing the draw action.
     * @return The drawn Character card.
     */
    public Card drawUpperCharacter(int cardIndex, Player drawingPlayer) {
        return line.drawUpperCharacter(cardIndex, drawingPlayer);
    }


    /**
     * Draws a Character card from the lower row of the line.
     *
     * @param cardIndex     The index of the card to draw.
     * @param drawingPlayer The player performing the draw action.
     * @return The drawn Character card.
     */
    public Card drawLowerCharacter(int cardIndex, Player drawingPlayer) {
        return line.drawLowerCharacter(cardIndex, drawingPlayer);
    }



    /**
     * Draws a Building card from the upper row of the line.
     *
     * @param cardIndex     The index of the card to draw.
     * @param drawingPlayer The player performing the draw action.
     * @return The drawn Character card.
     */
    public Card drawUpperBuilding(int cardIndex, Player drawingPlayer) {
        return line.drawUpperBuilding(cardIndex, drawingPlayer);
    }


    /**
     * Draws a Building card from the upper row of the line.
     *
     * @param cardIndex     The index of the card to draw.
     * @param drawingPlayer The player performing the draw action.
     * @return The drawn Character card.
     */
    public Card drawLowerBuilding(int cardIndex, Player drawingPlayer) {
        return line.drawLowerBuilding(cardIndex, drawingPlayer);
    }



    // ============================================================
    // GETTERS
    // ============================================================

    public Line getLine() {
        return line;
    }

    public List<Card> getDiscards() {
        return new ArrayList<>(discards);
    }

    public Deck getDeck() {
        return deck;
    }

    public Era getNextEra() {
        return line.getNewEra();
    }
    public Era getCurrentEra() {
        return line.getCurrentEra();
    }


    // ============================================================
    // ADDERS
    // ============================================================


    /**
     * Utility to add a card to the global discard pile.
     * Potrai chiamarlo da Line o da altri componenti se serve.
     */
    public void addToDiscards(Card card) {
        if (card != null) {
            discards.add(card);
        }
    }


    // ============================================================
    // FINITE STATE METHODS
    // ============================================================

    public boolean hasEraChanged() {
        return line.hasEraChanged();
    }



    /**
     * Handles end-of-game operations by delegating to the line, where there's resolve Events.
     */
    public void endGame() {
        line.endGame();
        // eventuale logica extra legata agli scarti o al deck
    }



    /**
     * Ends the current round.
     * Propagates the update to the line and checks if an Era change occurs.
     *
     * @throws EraEndedException If ending the round triggers the end of the current Era.
     */
    public void endRound() throws EraEndedException {
        try {
            line.endRound(numPlayers);
        } catch ( EraEndedException e ) {
            throw new EraEndedException("We have changed the era.");
        }
    }

    /**
     * Ends the current Era and prepares the setup for the new Era.
     * Executes building-related logic and resets Era change flags.
     *
     * @param newEra The new Era that is about to begin.
     * @throws DeckEmptiedException If the new Era is the final one and the deck runs out.
     */

    public void endEra(Era newEra) throws DeckEmptiedException {
        line.endEra();       // qui fai tutta la logica edifici/new era
        line.clearEraChange();
        if(newEra.isFinal()){
            throw new DeckEmptiedException("We have emptied the deck.");
        }
    }

}

