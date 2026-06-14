package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsOneModelElement;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

/**
 * Represents a specific type of {@link BuildingCard} that grants a bonus during hunting events.
 * <p>
 * When its corresponding event is triggered, this building rewards the owner based on
 * the number of Hunter characters they possess.
 */
public class BonusHuntingCard extends BuildingCard {
    /**
     * Constructs a new {@code BonusHuntingCard}.
     *
     * @param buildingEvent the {@link BuildingEvent} that triggers this card's effect (usually a Hunting event)
     * @param pointsEndgame the points awarded at the end of the game
     * @param foodPrice     the base food cost to acquire this building
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card
     */
    public BonusHuntingCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
    }



    /**
     * Executes the special effect of this building.
     * <p>
     * This method is invoked by the {@link it.polimi.ingsw.gc49.server.model.EventManager}. It calculates the total number of
     * {@link CharacterType#Hunter} cards owned by the player. It then decreases both the
     * player's pending food penalty ({@code FoodToPay}) and points penalty ({@code PointsToPay})
     * by that exact amount.
     */
    @Override
    public void onEventEffect() {
        owner.setFoodToPay(owner.getFoodToPay() - owner.data.getCharacterCount(CharacterType.Hunter));
        owner.setPointsToPay(owner.getPointsToPay() - owner.data.getCharacterCount(CharacterType.Hunter));
        if(queueUpdater != null) {
            queueUpdater.queueUpdateModelElement(new FoodAndPointsOneModelElement(
                    "La carta " + simpleToString() + " si è attivata fornendo 1 di cibo e punti a " + owner.getNickname() +
                            " per ogni suo cacciatore",
                    owner.getPlayerIndex(),
                    owner.getFood(),
                    owner.getPoints()
            ));
        }
    }

    /**
     * Provides a detailed string representation of the card, including its stats and effect description.
     *
     * @return a multi-line {@link String} describing the card's attributes and mechanics
     */

    @Override
    public String toString() {
        return "BonusHuntingCard {\n" +
                " era = " + era +
                ", foodPrice = " + foodPrice +
                ", pointsEndgame = " + pointsEndgame +
                ",\n effect = get bonus food and points equal to the number of hunters in possession during the hunting event" +
                "\n}";
    }

    /**
     * Provides a localized, concise name for this specific building card.
     *
     * @return a one-line {@link String} ("EDIFICIO (stracaccia)")
     */

    @Override
    public String simpleToString () {
        return "EDIFICIO (stracaccia)";
    }



    /**
     * Generates a visually formatted ASCII-art representation of the card for the TUI.
     * <p>
     * The generated drawing has a fixed height of 2 and a width of 10. It uses green styling
     * for the borders and includes symbols to represent the endgame points (♦), food cost (♥),
     * and the specific hunter multiplier effect (♥♦xH).
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
                .style(AttributedStyle.DEFAULT).append("♥♦xH   %")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╝")
                .toAttributedString();
        int height = 2;
        int width = 10;
        return new RectangleAttributedString(height, width, attributedString);
    }
}
