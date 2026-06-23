package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.States.State;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The {@code MockupGame} class represents the complete client-side snapshot of an active game.
 * It serves as a data container for the user interface, holding the current state of all players,
 * the shared game board (cards, deck, discards), and the track boards (offers and orders).
 * To ensure data integrity, most getters return unmodifiable lists.
 */
public class MockupGame implements Serializable {
    private State.States gameState = State.States.OTHER;
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

    private String finalStandings = "";

    /**
     * Constructs a new {@code MockupGame}.
     * This should be initialized strictly AFTER the real game has been initialized on the server.
     *
     * @param players       the initial list of players in the game.
     * @param deckTopEra    the era of the top card on the deck.
     * @param upperLine     the initial list of cards in the upper line.
     * @param lowerLine     the initial list of cards in the lower line.
     * @param upperBuilding the initial list of cards in the upper building.
     * @param lowerBuilding the initial list of cards in the lower building.
     * @param offerBoard    the list representing the offer board.
     * @param orderBoard    the list representing the order board.
     */
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

    // ============================================================
    // SETTERS
    // ============================================================


    public void setFinalStandings ( String finalStandings ) {
        this.finalStandings = finalStandings;
    }

    public void setGameState ( State.States gameState ) {
        this.gameState = gameState;
    }

    /**
     * Replaces the current list of players.
     *
     * @param players the new list of {@link MockupPlayer}s.
     */
    public void setPlayers (List<MockupPlayer> players) { this.players = players; }

    /**
     * Updates the current player index.
     * Automatically removes the turn flag from the previous player and grants it to the new one.
     *
     * @param currentPlayerIndex the index of the player whose turn is starting.
     */
    public void setCurrentPlayerIndex (int currentPlayerIndex) {
        players.get(this.currentPlayerIndex).setOfTurn(false);
        this.currentPlayerIndex = currentPlayerIndex;
        players.get(this.currentPlayerIndex).setOfTurn(true);
    }

    //Cardboard

    /**
     * Updates the era visible on the top of the deck.
     *
     * @param deckTopEra the new {@link Era}.
     */
    public void setDeckTopEra(Era deckTopEra) {
        this.deckTopEra = deckTopEra;
    }

    /**
     * Updates the upper line of cards on the board.
     *
     * @param upperLine the new list of {@link Card}s.
     */
    public void setUpperLine(List<Card> upperLine) {
        this.upperLine = upperLine;
    }

    /**
     * Updates the lower line of cards on the board.
     *
     * @param lowerLine the new list of {@link Card}s.
     */
    public void setLowerLine(List<Card> lowerLine) {
        this.lowerLine = lowerLine;
    }

    /**
     * Updates the upper building area cards.
     *
     * @param upperBuilding the new list of {@link Card}s.
     */
    public void setUpperBuilding(List<Card> upperBuilding) {
        this.upperBuilding = upperBuilding;
    }

    /**
     * Updates the lower building area cards.
     *
     * @param lowerBuilding the new list of {@link Card}s.
     */
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

    // ============================================================
    // ### GETTERS
    // ============================================================


    public String getFinalStandings () {
        return finalStandings;
    }

    // States
    public State.States getGameState() {
        return gameState;
    }

    // Players

    /**
     * Retrieves the index of the player whose turn it is.
     *
     * @return the current player's index.
     */
    public int getCurrentPlayerIndex () {
        return currentPlayerIndex;
    }

    /**
     * Retrieves the list of all players.
     *
     * @return an unmodifiable list of {@link MockupPlayer}s.
     */
    public List<MockupPlayer> getPlayers () {
        return Collections.unmodifiableList(players);
    }

    /**
     * Retrieves a specific player by their index.
     *
     * @param i the index of the requested player.
     * @return the {@link MockupPlayer} at the specified index.
     */
    public MockupPlayer getPlayer(int i) {return players.get(i);}


    //Cardboard
    /**
     * Retrieves the list of discarded cards.
     *
     * @return an unmodifiable list of {@link Card}s representing the discard pile.
     */
    public List<Card> getDiscards() {
        return Collections.unmodifiableList(discards);
    }

    /**
     * Retrieves the era visible on the top of the deck.
     *
     * @return the current {@link Era}.
     */
    public Era getDeckTopEra() {
        return deckTopEra;
    }

    /**
     * Retrieves the cards currently in the upper line.
     *
     * @return an unmodifiable list of {@link Card}s.
     */
    public List<Card> getUpperLine() {
        return Collections.unmodifiableList(upperLine);
    }

    /**
     * Retrieves the cards currently in the lower line.
     *
     * @return an unmodifiable list of {@link Card}s.
     */
    public List<Card> getLowerLine() {
        return Collections.unmodifiableList(lowerLine);
    }

    /**
     * Retrieves the cards currently in the upper building area.
     *
     * @return an unmodifiable list of {@link Card}s.
     */
    public List<Card> getUpperBuilding() {
        return Collections.unmodifiableList(upperBuilding);
    }

    /**
     * Retrieves the cards currently in the lower building area.
     *
     * @return an unmodifiable list of {@link Card}s.
     */
    public List<Card> getLowerBuilding() {
        return Collections.unmodifiableList(lowerBuilding);
    }


    //Track
    /**
     * Retrieves the current state of the order board.
     *
     * @return an unmodifiable list of {@link MockupOrder}s.
     */
    public List<MockupOrder> getOrderBoard () {
        return Collections.unmodifiableList(orderBoard);
    }

    /**
     * Retrieves a visually formatted string representation of the order board.
     *
     * @return a list of {@link RectangleAttributedString}s representing each order.
     */
    public List<RectangleAttributedString> getOrderBoardRectangleStrings() {
        return orderBoard
                .stream()
                .map(MockupOrder::getRectangleAttributedString)
                .toList();
    }

    /**
     * Retrieves the current state of the offer board.
     *
     * @return an unmodifiable list of {@link MockupOffer}s.
     */
    public List<MockupOffer> getOfferBoard() {
        return Collections.unmodifiableList(offerBoard);
    }

    /**
     * Retrieves a visually formatted string representation of the offer board.
     *
     * @return a list of {@link RectangleAttributedString}s representing each offer.
     */
    public List<RectangleAttributedString> getOfferBoardRectangleStrings(){
        return offerBoard
                .stream()
                .map(MockupOffer::getRectangleAttributedString)
                .toList();
    }

    // ============================================================
    // ### ADDERS
    // ============================================================

    // Cardboard

    /**
     * Adds a batch of cards to the discard pile.
     *
     * @param discards the list of {@link Card}s to be added.
     */
    public void addDiscards(List<Card> discards) {
        this.discards.addAll(discards);
    }
}
