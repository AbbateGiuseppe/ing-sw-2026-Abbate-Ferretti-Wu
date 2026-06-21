package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;

import java.io.Serializable;

/**
 * Represents an abstract base class for all Character Cards in the game.
 * <p>
 * Character cards represent the members of a player's tribe (e.g., Artists, Builders, Hunters).
 * Unlike {@link it.polimi.ingsw.gc49.server.model.Card.BuildingCard.BuildingCard}s, character cards generally do not have an inherent food cost
 * to be acquired and are automatically placed in the player's databank upon drawing,
 * expanding their tribe's capabilities or providing multipliers for events.
 */
public abstract class CharacterCard extends Card implements Serializable {

    /**
     * Constructs a new {@code CharacterCard}.
     *
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card in the deck
     */
    public CharacterCard ( Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(era, minNumPlayers, queueUpdater);
    }

    /**
     * Indicates whether a player can acquire this card.
     * <p>
     * For basic {@code CharacterCard}s, this method always returns {@code true}.
     * Unlike buildings that require a specific amount of food to be purchased,
     * characters can always be picked up by a player using a standard draw action.
     *
     * @param player the {@link Player} attempting to acquire the card
     * @return always {@code true}
     */
    @Override
    public boolean canGet(Player player) {
        return true;
    }


    /**
     * Determines if this card can be placed on the lower line during the game setup.
     * <p>
     * Character cards are valid candidates for the lower line during the initial
     * setup phase of the board.
     *
     * @return always {@code true}
     */
    @Override
    public boolean isLowerLineOnSetup () {
        return true;
    }
}
