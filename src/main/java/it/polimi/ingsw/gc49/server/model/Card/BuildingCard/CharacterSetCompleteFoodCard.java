package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.*;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

/**
 * Represents a specific type of {@link BuildingCard} that rewards the completion of character sets.
 * <p>
 * This building grants a massive bonus of <b>5 food</b> to the owner whenever they complete
 * a full set of character cards. To prevent players from getting rewarded for sets they had
 * already completed before acquiring this building, it establishes a baseline at the moment of purchase.
 */
public class CharacterSetCompleteFoodCard extends BuildingCard {
    /**
     * Constructs a new {@code CharacterSetCompleteFoodCard}.
     *
     * @param buildingEvent the {@link BuildingEvent} that triggers this card's effect
     * @param pointsEndgame the points awarded at the end of the game
     * @param foodPrice     the base food cost to acquire this building
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card
     */
    public CharacterSetCompleteFoodCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
    }

    /**
     * Executes the special effect of this building.
     * <p>
     * This method is invoked by the {@link EventManager}. It compares the player's
     * absolute number of complete sets ({@code CharacterType.CompleteSet}) against
     * the previously recorded baseline. If a new set has been completed, it rewards
     * the player with 5 food and increments the baseline to prevent double-counting.
     */
    @Override
    public void onEventEffect() {
        if (owner.data.getCharacterCount(CharacterType.CompleteSet) > owner.data.getCurrentNumCompleteCharacterSets()) {
            owner.addFood(5);
            owner.data.incrementCurrentNumCompleteCharacterSets();
        }
    }

    /**
     * Assigns this building to a specific player and initializes the set tracking.
     * <p>
     * Overrides the default {@link BuildingCard#setOwner(Player)} behavior. In addition
     * to setting the owner, it calls {@code owner.data.recordCharaSet()} to record
     * the current number of complete character card sets. This serves as the baseline
     * for future comparisons in {@link #onEventEffect()}.
     *
     * @param owner the {@link Player} who now owns the card
     */
    @Override
    public void setOwner( Player owner) {
        super.setOwner(owner);
        // Record the current number of complete character card sets for future comparision
        owner.data.recordCharaSet();
    }

    /**
     * Provides a detailed string representation of the card, including its stats and effect description.
     *
     * @return a multi-line {@link String} describing the card's attributes and mechanics
     */
    @Override
    public String toString() {
        return "CharacterSetCompleteFoodCard {\n" +
                " era = " + era +
                ", foodPrice = " + foodPrice +
                ", pointsEndgame = " + pointsEndgame +
                ",\n effect = get 5 bonus food whenever completed a full set of character cards" +
                "\n}";
    }

    /**
     * Provides a localized, concise name for this specific building card.
     *
     * @return a simple one-line {@link String} ("EDIFICIO (stracibo da set)")
     */
    @Override
    public String simpleToString () {
        return "EDIFICIO (stracibo da set)";
    }


    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 2 and a width of 10. It uses green styling
     * for the borders and includes symbols to represent the endgame points (♦), food cost (♥),
     * and the specific bonus food reward (5♥).
     *
     * @return a {@link RectangleAttributedString} containing the colored terminal UI graphics
     */
    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╔")
                .style(AttributedStyle.DEFAULT).append(String.valueOf(pointsEndgame)).append("♦")
                .append("    ").append(String.valueOf(foodPrice)).append("♥")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╗")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╚")
                .style(AttributedStyle.DEFAULT).append("5♥     ●")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╝")
                .toAttributedString();
        int height = 2;
        int width = 10;
        return new RectangleAttributedString(height, width, attributedString);
    }
}
