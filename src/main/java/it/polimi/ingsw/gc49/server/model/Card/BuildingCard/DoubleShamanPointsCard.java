package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

/**
 * Represents a specific type of {@link BuildingCard} that enhances the rewards of a Shamanic Ritual event.
 * <p>
 * When its corresponding event is triggered, this building checks if the owner is the
 * unique winner of the ritual. If so, it doubles the bonus points awarded to the player
 * for winning the event.
 */
public class DoubleShamanPointsCard extends BuildingCard{
    /**
     * Constructs a new {@code DoubleShamanPointsCard}.
     *
     * @param buildingEvent the {@link BuildingEvent} that triggers this card's effect (usually a Shamanic event)
     * @param pointsEndgame the base points awarded at the end of the game
     * @param foodPrice     the base food cost to acquire this building
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card
     */
    public DoubleShamanPointsCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
    }

    /**
     * Executes the special effect of this building.
     * <p>
     * This method is invoked by the {@link it.polimi.ingsw.gc49.server.model.EventManager}. It first verifies if the {@link Player}
     * is the unique winner of the event ({@code isUniqueWinner()}). Then, it checks if the player
     * has a negative points penalty ({@code getPointsToPay() < 0}), which in the game's logic
     * represents a pending point reward. If both conditions are met, the reward is doubled.
     */
    @Override
    public void onEventEffect() {
        if (owner.isUniqueWinner() && owner.getPointsToPay() < 0) {
            owner.setPointsToPay(2 * owner.getPointsToPay());
        }
    }


    /**
     * Provides a detailed string representation of the card, including its stats and effect description.
     *
     * @return a multi-line {@link String} describing the card's attributes and the unique winner doubling effect
     */
    @Override
    public String toString() {
        return "DoubleShamanPointsCard {\n" +
                " era = " + era +
                ", foodPrice = " + foodPrice +
                ", pointsEndgame = " + pointsEndgame +
                ",\n effect = if the player is the unique winner,then double the reward points during the shamanic event" +
                "\n}";
    }

    /**
     * Provides a localized, concise name for this specific building card.
     *
     * @return a simple one-line {@link String} ("EDIFICIO (doppie stelle)")
     */
    @Override
    public String simpleToString () {
        return "EDIFICIO (doppie stelle)";
    }


    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 2 and a width of 10. It uses green styling
     * for the borders and includes symbols to represent the endgame points (♦), food cost (♥),
     * and the specific multiplier for shamanic ritual points (x2♦).
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
                .style(AttributedStyle.DEFAULT).append("x2♦    §")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╝")
                .toAttributedString();
        int height = 2;
        int width = 10;
        return new RectangleAttributedString(height, width, attributedString);
    }
}
