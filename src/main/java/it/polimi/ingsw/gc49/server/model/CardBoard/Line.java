package it.polimi.ingsw.gc49.server.model.CardBoard;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.EventCard.EventCard;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.States.EraEndedException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.sort;

/**
 * Represents the central market  of cards in the game.
 * Manages the available character (tribe) and building cards across different rows (upper and lower),
 * handles the progression of Eras, and resolves events at the end of rounds or the game.
 */
public class Line implements Serializable {

    private Era currentEra = Era.first();
    private boolean eraChanged = false;
    private Era newEra = currentEra.next();
    private final int numPlayers;
    private final List<Player> playerList;



    private final List<Card> upperLine;
    private final List<Card> lowerLine;
    private final List<Card> upperBuilding;
    private final List<Card> lowerBuilding;
    private final Deck deck;



    /**
     * Constructs the Line and sets up the initial board state.
     * Deals the starting tribe cards to the upper and lower lines based on the number of players,
     * and places the initial building cards for the first Era.
     *
     * @param game The main game instance.
     * @param deck The deck used to draw cards.
     */

    public Line( Game game, Deck deck) {
        this.numPlayers = game.getNumOfPlayers();
        this.playerList = game.getPlayers();
        this.deck = deck;
        this.upperLine = new ArrayList<>();
        this.lowerLine = new ArrayList<>();
        this.upperBuilding = new ArrayList<>();
        this.lowerBuilding = new ArrayList<>();

        int upperPlaced = 0;
        int lowerPlaced = 0;

        while (lowerPlaced < numPlayers + 1) {
            Card drawn = deck.dealTribeCard();
            if (drawn == null) break;

            if(drawn.isLowerLineOnSetup()) {
                lowerLine.add(drawn);
                lowerPlaced++;
            } else {
                upperLine.add(drawn);
                upperPlaced++;
            }
        }

        while (upperPlaced < numPlayers + 4) {
            Card drawn = deck.dealTribeCard();
            if (drawn == null) break;

            upperLine.add(drawn);
            upperPlaced++;
        }

        int buildingsToPlace = deck.getBuildingsToPlace(numPlayers, currentEra);
        for (int i = 0; i < buildingsToPlace; i++) {
            Card building = deck.dealBuildingCard();
            if (building == null) break;
            upperBuilding.add(building);
        }
    }

    /**
     * Deals a Tribe card directly from the deck.
     *
     * @return The drawn Tribe card, or null if the deck is empty.
     */
    public Card dealTribeCard() {
            return deck.dealTribeCard();
    }

    /**
     * Deals a Building card directly from the deck.
     *
     * @return The drawn Tribe card, or null if the deck is empty.
     */
    public Card dealBuildingCard() {
            return deck.dealBuildingCard();
    }


    /**
     * Attempts to draw a Character card from the upper line.
     *
     * @param cardIndex     The index of the card in the upper line.
     * @param drawingPlayer The player attempting to draw the card.
     * @return The drawn Card if successful, or null if the index is invalid or the player cannot get it.
     */

    public Card drawUpperCharacter(int cardIndex, Player drawingPlayer) {
        if (cardIndex < 0 || cardIndex >= upperLine.size()) {
            return null;
        }

        Card picked = upperLine.get(cardIndex);
        if (!picked.canGet(drawingPlayer)) {
            return null;
        }else{
            upperLine.remove(cardIndex); //actually removing the card from the line
            return picked;
        }
    }


    /**
     * Attempts to draw a Character card from the lower line.
     *
     * @param cardIndex     The index of the card in the upper line.
     * @param drawingPlayer The player attempting to draw the card.
     * @return The drawn Card if successful, or null if the index is invalid or the player cannot get it.
     */
    public Card drawLowerCharacter(int cardIndex, Player drawingPlayer) {
        if (cardIndex < 0 || cardIndex >= lowerLine.size()) {
            return null;
        }

        Card picked = lowerLine.get(cardIndex);
        if (!picked.canGet(drawingPlayer)) {
            return null;
        }else {
            lowerLine.remove(cardIndex); //actually removing the card from the line
            return picked;
        }
    }


    /**
     * Attempts to draw a Building  card from the upper line.
     *
     * @param cardIndex     The index of the card in the upper line.
     * @param drawingPlayer The player attempting to draw the card.
     * @return The drawn Card if successful, or null if the index is invalid or the player cannot get it.
     */
    public Card drawUpperBuilding(int cardIndex, Player drawingPlayer) {
        if (cardIndex < 0 || cardIndex>= upperBuilding.size()) {
            return null;
        }

        Card pickedBuilding = upperBuilding.get(cardIndex);
        if (!pickedBuilding.canGet(drawingPlayer)) {
            return null;
        }else{
            upperBuilding.remove(cardIndex); //actually removing the card from the line
            return pickedBuilding;
        }
    }


    /**
     * Attempts to draw a Building card from the lower line.
     *
     * @param cardIndex     The index of the card in the upper line.
     * @param drawingPlayer The player attempting to draw the card.
     * @return The drawn Card if successful, or null if the index is invalid or the player cannot get it.
     */
    public Card drawLowerBuilding(int cardIndex, Player drawingPlayer) {
        if (cardIndex < 0 || cardIndex >= lowerBuilding.size()) {
            return null;
        }

        Card pickedBuilding = lowerBuilding.get(cardIndex);
        if (!pickedBuilding.canGet(drawingPlayer)) {
            return null;
        }else {
            lowerBuilding.remove(cardIndex); //actually removing the card from the line
            return pickedBuilding;
        }
    }


    // ============================================================
    // FINITE STATE
    // ============================================================

    public boolean hasEraChanged() {
        return eraChanged;
    }
    public Era getNewEra() {
        return newEra;
    }
    public void clearEraChange() {
        eraChanged = false;
        newEra = null;
    }
    public Era getCurrentEra() {
        return currentEra;
    }

    /**
     * Concludes the current round. Resolves events in the lower line, shifts the upper line
     * down, and draws new cards. Detects if an Era transition is triggered.
     *
     * @param numPlayers The number of players in the game (used to determine card draw limits).
     * @throws EraEndedException If drawing new cards reveals a card from the next Era.
     */
    public void endRound(int numPlayers) throws EraEndedException {
        resolveEvent(lowerLine, playerList);
        lowerLine.clear();
        lowerLine.addAll(upperLine);
        upperLine.clear();

        Era previousEra = currentEra;
        Era lastCardEra = currentEra;

        for (int i = 0; i < numPlayers + 4; i++) {
            Card drawn = deck.dealTribeCard();
            if (drawn == null) break;

            upperLine.add(drawn);

            lastCardEra = drawn.getEra();
        }
        if (!lastCardEra.equals(previousEra)) {
            // segna che c'è stato un cambio era, ma NON fai ancora endEra
            eraChanged = true;
            newEra = lastCardEra;
            throw new EraEndedException("Era ended");
        }
    }

    /**
     * Processes the transition into a new Era.
     * Handles the shifting and discarding of Building cards based on the new Era's rules,
     * and populates the upper building line with new cards.
     */

    public void endEra() {
        // 1) If transitioning to Era III: discard any buildings in the lower row
        if (newEra == Era.THIRD) {
            lowerBuilding.clear();
        }

        // 2) Move buildings from the upper row to the lower row (happens when Era II or III starts)
        lowerBuilding.addAll(upperBuilding);
        upperBuilding.clear();

        // 3) Add buildings of the newly started Era to the upper row,
        //    quantity depends on numPlayers (per rulebook table)
        int buildingsToPlace = deck.getBuildingsToPlace(numPlayers, newEra);
        for (int i = 0; i < buildingsToPlace; i++) {
            Card building = deck.dealBuildingCard();
            if (building == null) break;       // nessuna carta edificio rimasta
            upperBuilding.add(building);
        }

        currentEra = newEra;
        eraChanged = false;
    }


    /**
     * Concludes the game by resolving all remaining events on the board.
     * Optionally clears the board entirely.
     */
    public void endGame() {

        resolveEvent(lowerLine, playerList);

        resolveEvent(upperLine, playerList);

        lowerLine.clear();
        upperLine.clear();
        lowerBuilding.clear();
        upperBuilding.clear();
    }

    /**
     * Filters a given line for Event cards, sorts them according to game rules,
     * and resolves their effects for all players.
     *
     * @param line       The line of cards to check for events.
     * @param playerList The list of players affected by the events.
     */

    private void resolveEvent(List<Card> line, List<Player> playerList) {
        List<EventCard> events = new ArrayList<>();

        for (Card c : line) {
            if (c instanceof EventCard) {
                events.add((EventCard) c);
            }
        }
        sort(events);

        for (EventCard e : events) {
            e.resolveEvent(playerList);
        }
    }

    // ============================================================
    // GETTERS
    // ============================================================

    public List<Card> getUpperLine() { return upperLine; }
    public List<Card> getLowerLine() { return lowerLine; }
    public List<Card> getUpperBuilding() { return upperBuilding; }
    public List<Card> getLowerBuilding() { return lowerBuilding; }
}
