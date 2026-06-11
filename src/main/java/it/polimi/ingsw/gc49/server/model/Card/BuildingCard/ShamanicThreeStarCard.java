package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.DataBank;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

/**
 * Represents a specific type of {@link BuildingCard} that grants permanent Shamanic stars.
 * <p>
 * Unlike other event-driven buildings, this card provides an immediate and permanent
 * advantage. Upon acquisition, it grants the owner <b>3 virtual stars</b>, permanently
 * boosting their base score for all future Shamanic Ritual events.
 */
public class ShamanicThreeStarCard extends BuildingCard {
    /**
     * Constructs a new {@code ShamanicThreeStarCard}.
     *
     * @param buildingEvent the {@link BuildingEvent} that conceptually links to this card (usually a Shamanic event)
     * @param pointsEndgame the base points awarded at the end of the game
     * @param foodPrice     the base food cost to acquire this building
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card
     */
    public ShamanicThreeStarCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
    }

    /**
     * Executes the special effect of this building during an event.
     * <p>
     * For this specific card, this method is intentionally left empty. The bonus
     * is provided passively via the databank update rather than dynamically
     * calculating an effect during the event resolution phase.
     */
    @Override
    public void onEventEffect() {
        return;
    }


    /**
     * Updates the player's databank upon acquiring the card.
     * <p>
     * Calls the {@code super} method to add the standard endgame points, and additionally
     * adds 3 permanent Shamanic stars ({@code addNumStar(3)}) to the owner's databank.
     *
     * @param dataBank the {@link DataBank} of the player acquiring the card
     */
    @Override
    public void updateDataBank(DataBank dataBank) {
        super.updateDataBank(dataBank);
        dataBank.addNumStar(3);
    }

    /**
     * Provides a detailed string representation of the card, including its stats and effect description.
     *
     * @return a multi-line {@link String} describing the card's attributes and the 3-star passive bonus
     */
    @Override
    public String toString() {
        return "ShamanicThreeStarCard {\n" +
                " era = " + era +
                ", foodPrice = " + foodPrice +
                ", pointsEndgame = " + pointsEndgame +
                ",\n effect = get 3 virtual stars during the shamanic event" +
                "\n}";
    }

    /**
     * Provides a localized, concise name for this specific building card.
     *
     * @return a simple one-line {@link String} ("EDIFICIO (tre stelle)")
     */
    @Override
    public String simpleToString () {
        return "EDIFICIO (tre stelle)";
    }


    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 2 and a width of 10. It uses green styling
     * for the borders and includes symbols to represent the endgame points (♦), food cost (♥),
     * and the specific 3 virtual stars bonus (3*) associated with the shamanic event (§).
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
                .style(AttributedStyle.DEFAULT).append("3*     §")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╝")
                .toAttributedString();
        int height = 2;
        int width = 10;
        return new RectangleAttributedString(height, width, attributedString);
    }
}
