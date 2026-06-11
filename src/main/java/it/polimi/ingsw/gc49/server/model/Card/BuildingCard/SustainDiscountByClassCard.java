package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

/**
 * Represents a specific type of {@link BuildingCard} that provides a food discount during Sustenance events.
 * <p>
 * When its corresponding event is triggered (typically a Sustenance event),
 * this building reduces the amount of food the owner has to pay based on the amount
 * of a specific {@link CharacterType} they possess. For every unit of that chosen
 * character class in their databank, the food penalty is reduced by 1.
 */
public class SustainDiscountByClassCard extends BuildingCard {
    /** The specific class of character (e.g., Gatherer, Builder) that grants the discount. */
    private final CharacterType unit;

    /**
     * Constructs a new {@code SustainDiscountByClassCard}.
     *
     * @param unit          the {@link CharacterType} whose count determines the discount amount
     * @param buildingEvent the {@link BuildingEvent} that triggers this card's effect (usually Sustenance)
     * @param pointsEndgame the base points awarded at the end of the game
     * @param foodPrice     the base food cost to acquire this building
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card
     */
    public SustainDiscountByClassCard ( CharacterType unit, BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
        this.unit = unit;
    }

    /**
     * Executes the special effect of this building.
     * <p>
     * This method is invoked by the {@link it.polimi.ingsw.gc49.server.model.EventManager}. It counts the total number of
     * characters matching the specified {@link #unit} owned by the player. It then decreases
     * the player's pending food penalty ({@code FoodToPay}) by that exact amount.
     */
    @Override
    public void onEventEffect() {
        owner.setFoodToPay(owner.getFoodToPay() - owner.data.getCharacterCount(unit));
    }


    /**
     * Provides a detailed string representation of the card, including its stats and effect description.
     *
     * @return a multi-line {@link String} describing the card's attributes and the dynamic discount effect
     */
    @Override
    public String toString() {
        return "SustainDiscountByClassCard {\n" +
                " era = " + era +
                ", foodPrice = " + foodPrice +
                ", pointsEndgame = " + pointsEndgame +
                ",\n effect = get 1 food discount for each " + unit + " in possession during the sustenance event" +
                "\n}";
    }

    /**
     * Provides a detailed string representation of the card, including its stats and effect description.
     *
     * @return a multi-line {@link String} describing the card's attributes and the dynamic discount effect
     */
    @Override
    public String simpleToString () {
        return "EDIFICIO (sconto sostentamento)";
    }

    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 2 and a width of 10. It dynamically
     * appends a specific letter based on the {@link #unit} type (e.g., 'A' for Artist,
     * 'G' for Gatherer) to visually indicate which character class grants the discount,
     * along with the sustenance symbol (€).
     *
     * @return a {@link RectangleAttributedString} containing the colored terminal UI graphics
     */
    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedStringBuilder attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╔")
                .style(AttributedStyle.DEFAULT).append(String.valueOf(pointsEndgame)).append("♦")
                .append("    ").append(String.valueOf(foodPrice)).append("♥")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╗")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╚")
                .style(AttributedStyle.DEFAULT).append("♥x");
        switch(unit){
            case Artist -> attributedString.append("A");
            case Builder -> attributedString.append("B");
            case Gatherer -> attributedString.append("G");
            case Hunter -> attributedString.append("H");
            case Inventor -> attributedString.append("I");
            case Shaman -> attributedString.append("S");
        }
        attributedString.append("    €")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╝");
        int height = 2;
        int width = 10;
        return new RectangleAttributedString(height, width, attributedString.toAttributedString());
    }
}
