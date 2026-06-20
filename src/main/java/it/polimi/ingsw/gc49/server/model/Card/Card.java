package it.polimi.ingsw.gc49.server.model.Card;


import it.polimi.ingsw.gc49.client.view.ItaEngRectangable;
import it.polimi.ingsw.gc49.client.view.Rectangable;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.DataBank;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;

import java.io.Serializable;
/**
 * Represents an abstract base class for all cards in the game.
 * <p>
 * This class implements {@link Rectangable} //TODO spiega utilizzo rectangable
 * and {@link Serializable}. It holds common
 * properties such as the {@link Era} it belongs to and the minimum number of players
 * required to use it. Subclasses must define specific acquisition rules and behaviors.
 */
public abstract class Card implements Rectangable, Serializable, ItaEngRectangable {
    /** The era this card belongs to. */
    protected final Era era;
    /** The minimum number of players required for this card to be included in the game. */
    private final int minNumPlayers;
    /** The update queue, where updates can be queued at the card activation. */
    protected final transient QueueUpdatable queueUpdater;


    /**
     * Constructs a new {@code Card} with the specified era and minimum number of players.
     *
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to play with this card
     */
    public Card( Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        this.era = era;
        this.minNumPlayers = minNumPlayers;
        this.queueUpdater = queueUpdater;
    }

    /**
     * Retrieves the era of this card.
     *
     * @return the {@link Era} of the card
     */
    public Era getEra() {return era;}



    /**
     * Retrieves the minimum number of players required for this card.
     *
     * @return the minimum number of players
     */
    public int getMinNumPlayers() {return minNumPlayers;}



    /**
     * Checks whether the specified player is eligible to acquire this card.
     * <p>
     * Implementation specifics typically follow these rules:
     * <ul>
     * <li><b>Character Cards:</b> always return {@code true}.</li>
     * <li><b>Event Cards:</b> always return {@code false}.</li>
     * <li><b>Building Cards:</b> evaluate the player's food and building discounts against the card's food price.</li>
     * </ul>
     * If this method returns {@code true}, the player is expected to subsequently call {@link #updateDataBank(DataBank)}.
     *
     * @param player the {@link Player} attempting to acquire the card
     * @return {@code true} if the player can get the card, {@code false} otherwise
     */

    public abstract boolean canGet(Player player);

    /**
     * Updates the values of the drawing player's databank during a draw.
     * <p>
     * This method provides a default empty implementation and should be overridden
     * by subclass cards that actually need to modify the player's databank.
     * * @param dataBank the {@link DataBank} of the player drawing the card
     */
    public void updateDataBank( DataBank dataBank ) {
        // will be overridden by the subclass card when it has to actually modify the player's databank.
    }

    /**
     * Handles any special effects applied by the card after it has been drawn.
     * <p>
     * This method should be called <b>after</b> {@link #updateDataBank(DataBank)}.
     * * @param player the drawing {@link Player} receiving the card's effects
     */
    public void onDraw( Player player ) {

    }

    /**
     * Provides a simple, identifiable name for the card.
     * <p>
     * This is intended to be used for printing a concise, one-line string instead of
     * the detailed description typically returned by the {@code toString()} method.
     *
     * @return a simple one-line {@link String} representing the card's name or type
     */
    public abstract String simpleToString();

    /**
     * Determines if this card can be placed on the lower line during the game setup.
     *Implementation specifics typically follow these rules:
     * <ul>
     * <li><b>Character Cards:</b> always return {@code true}.</li>
     * <li><b>Event Cards:</b> always return {@code false}.</li>
     * <li><b>Building Cards:</b> always return {@code false}.</li>
     * </ul>
     * @return {@code true} if this card can be placed on the lower line on setup, {@code false} otherwise
     */
    public abstract boolean isLowerLineOnSetup();
}
