package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.TextModelElement;
import it.polimi.ingsw.gc49.server.model.*;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

/**
 * Represents a specific type of {@link BuildingCard} that grants an extra card draw.
 * <p>
 * When its corresponding event is triggered (typically at the end of the round),
 * this building allows its owner to pick one additional card from the upper line
 * of the game board.
 */
public class OneMoreCardCard extends BuildingCard {

    /**
     * Constructs a new {@code OneMoreCardCard}.
     *
     * @param buildingEvent the {@link BuildingEvent} that triggers this card's effect (usually End of Round)
     * @param pointsEndgame the base points awarded at the end of the game
     * @param foodPrice     the base food cost to acquire this building
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card
     */
    public OneMoreCardCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
    }

    /**
     * Executes the special effect of this building.
     * <p>
     * This method is invoked by the {@link EventManager}. It accesses the {@link Player}
     * owner and increments their allowed upper card draws ({@code DrawableUpper}) by 1,
     * effectively granting them an extra action to acquire a card.
     */
    @Override
    public void onEventEffect() {
        owner.setDrawableUpper(owner.getDrawableUpper()  + 1);
        if(queueUpdater != null) {
            queueUpdater.queueUpdateModelElement(new TextModelElement(
                    "La carta " + simpleToString() + " si è attivata fornendo un ulteriore pesca superiore a " + owner.getNickname()
            ));
        }
    }

    /**
     * Provides a detailed string representation of the card, including its stats and effect description.
     *
     * @return a multi-line {@link String} describing the card's attributes and the extra draw effect
     */
    @Override
    public String toString() {
        return "OneMoreCardCard {\n" +
                " era = " + era +
                ", foodPrice = " + foodPrice +
                ", pointsEndgame = " + pointsEndgame +
                ",\n effect = the player gets to pick another card at the end of the round" +
                "\n}";
    }


    /**
     * Provides a localized, concise name for this specific building card.
     *
     * @return a simple one-line {@link String} ("EDIFICIO (ulteriore carta)")
     */
    @Override
    public String simpleToString () {
        return "EDIFICIO (ulteriore carta)";
    }


    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 2 and a width of 10. It uses green styling
     * for the borders and includes symbols to represent the endgame points (♦), food cost (♥),
     * and a specific symbol (▲) to denote the extra card draw from the upper line.
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
                .style(AttributedStyle.DEFAULT).append("▲       ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╝")
                .toAttributedString();
        int height = 2;
        int width = 10;
        return new RectangleAttributedString(height, width, attributedString);
    }

    @Override
    public ItaEngRectangleAttributedString getItaEngRectangleAttributedString () {
        RectangleAttributedString globalRectangle = getRectangleAttributedString();
        return new ItaEngRectangleAttributedString(
                globalRectangle.height, globalRectangle.width, globalRectangle.attributedString,
                globalRectangle.height, globalRectangle.width, globalRectangle.attributedString);
    }
}
