package it.polimi.ingsw.gc49.server.model.Card.EventCard;

import it.polimi.ingsw.gc49.server.model.*;
import it.polimi.ingsw.gc49.server.model.Card.Card;

import java.io.Serializable;
import java.util.List;
/**
 * Represents an abstract base class for all Event Cards in the game.
 * <p>
 * Event cards behave differently from character or building cards: they cannot be directly
 * acquired by players and are instead resolved globally at specific points in the game.
 * This class implements {@link Comparable} to ensure events are resolved in the correct order,
 * following the game rules.
 */

public abstract class EventCard extends Card implements Comparable<EventCard>, Serializable {

    /**
     * The central dispatcher used to trigger building-related events during this card's resolution.
     * <p>
     * Since event cards often cause game-wide effects, they hold a reference to this manager.
     * When {@link #resolveEvent(List)} is called, the card can use this manager (via methods
     * like {@link EventManager#invokeEvent(BuildingEvent)}) to notify all active buildings on
     * the board that a specific event has occurred, automatically applying their special effects
     * to the players who own them.
     */
    protected final EventManager eventManager;

    /**
     * Constructs a new {@code EventCard}.
     *
     * @param eventManager  the {@link EventManager} used to trigger or listen to events
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card in the game
     */


    public EventCard ( EventManager eventManager, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(era, minNumPlayers, queueUpdater);
        this.eventManager = eventManager;
    }

    /**
     * Indicates whether a player can acquire this card.
     * <p>
     * For {@code EventCard}s, this method always returns {@code false} because
     * event cards are not drawn or kept by players; they are resolved automatically.
     *
     * @param player the {@link Player} attempting to acquire the card
     * @return always {@code false}
     */
    @Override
    public boolean canGet(Player player) {
        return false;
    }


    /**
     * Compares this event card with another to determine their resolution order.
     * <p>
     * The sorting rules applied according to the game manual are:
     * <ol>
     * <li><b>Sustenance Last:</b> A {@code SustenanceEvent} is always resolved after any other event type.</li>
     * <li><b>Era Order:</b> In case of same types (or neither being Sustenance), they are resolved in ascending {@link Era} order.</li>
     * <li><b>Alphabetical fallback:</b> If both era and sustenance-status are identical, they are sorted by class name to maintain consistency.</li>
     * </ol>
     *
     * @param other the other {@code EventCard} to be compared against
     * @return a negative integer, zero, or a positive integer as this card is to be resolved before,
     * at the same time as, or after the specified card
     */
    @Override
    public int compareTo(EventCard other) {
        if (this.getEra().compareTo(other.getEra()) != 0) {
            return this.getEra().compareTo(other.getEra());
        } else {
            return this instanceof SustenanceEvent ?  1 : -1;
        }

    }

    /**
     * Resolves the specific effects of this event card.
     *
     * @param players the list of {@link Player}s currently in the game affected by this event
     */
    public abstract void resolveEvent(List<Player> players);


    /**
     * Determines if this card can be placed on the lower line during the game setup.
     * <p>
     * Event cards are never placed on the lower line during the initial setup phase.
     *
     * @return always {@code false}
     */

    @Override
    public boolean isLowerLineOnSetup () {
        return false;
    }
}
