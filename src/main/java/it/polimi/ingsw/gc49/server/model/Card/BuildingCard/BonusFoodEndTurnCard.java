package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsOneModelElement;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;


/**
 * Represents a specific type of {@link BuildingCard} that grants a food bonus.
 * <p>
 * When its corresponding event is triggered, this building checks the player's currently
 * assigned order slot. If that slot inherently provides a food gain (greater than zero),
 * this building rewards the owner with <b>1 additional bonus food</b>.
 */
public class BonusFoodEndTurnCard extends BuildingCard {
    /**
     * Constructs a new {@code BonusFoodEndTurnCard}.
     *
     * @param buildingEvent the {@link BuildingEvent} that triggers this card's effect
     * @param pointsEndgame the points awarded at the end of the game
     * @param foodPrice     the base food cost to acquire this building
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card
     */
    public BonusFoodEndTurnCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
    }

    /**
     * Executes the special effect of this building.
     * <p>
     * GAME RULE:
     * If at the end of your turn (including the last
     * round), when you move your Totem onto the
     * Turn Order tile, you place it on a
     * space with a Food bonus, take
     * 1 additional Food. If you place it on the last
     * space, pay the Food normally, and the Building
     * has no effect.
     * <p>
     * This method is invoked by the {@link it.polimi.ingsw.gc49.server.model.EventManager}. It retrieves the current
     * order slot assigned to the {@link it.polimi.ingsw.gc49.server.model.Player} owner. If the slot yields a food gain
     * ({@code foodGain > 0}), the player receives exactly {@code 1} extra food.
     */

    @Override
    public void onEventEffect() {
        if (owner.getAssignedOrderSlot().getFoodGain() > 0) {
            owner.addFood(1);
            if(queueUpdater != null) {
                queueUpdater.queueUpdateModelElement(new FoodAndPointsOneModelElement(
                        "La carta " + simpleToString() + " si è attivata fornendo 1 di cibo a " + owner.getNickname(),
                        owner.getPlayerIndex(),
                        owner.getFood(),
                        owner.getPoints()
                ));
            }
        }
    }


    /**
     * Provides a detailed string representation of the card, including its stats and effect description.
     *
     * @return a multi-line {@link String} describing the card's attributes and mechanics
     */
    @Override
    public String toString() {
        return "BonusFoodEndTurnCard {\n" +
                " era = " + era +
                ", foodPrice = " + foodPrice +
                ", pointsEndgame = " + pointsEndgame +
                ",\n effect = get one bonus food if the totem is placed on an orderslot with food at the end of the turn" +
                "\n}";
    }

    /**
     * Provides a localized, concise name for this specific building card.
     *
     * @return one-line {@link String} ("EDIFICIO (cibo da piazzamento)")
     */
    @Override
    public String simpleToString () {
        return "EDIFICIO (cibo da piazzamento)";
    }


    /**
     * Generates a visually formatted ASCII-art representation of the card for the TUI.
     * <p>
     * The generated drawing has a fixed height of 2 and a width of 10. It uses green styling
     * for the borders and includes symbols to represent the endgame points (♦) and food cost/gain (♥).
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
                .style(AttributedStyle.DEFAULT).append("░1♥     ")
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
