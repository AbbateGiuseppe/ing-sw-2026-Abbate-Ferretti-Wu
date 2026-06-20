package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsOneModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.TextModelElement;

/**
 * Represents a specific type of {@link BuildingCard} that grants immunity during Shamanic events.
 * <p>
 * When its corresponding event is triggered (typically a Shamanic Ritual), this building
 * protects its owner from negative consequences. If the player loses the ritual and is
 * slated to receive a point penalty, this card cancels that penalty entirely.
 */
public class ShamanicImmunityCard extends BuildingCard {

    /**
     * Constructs a new {@code ShamanicImmunityCard}.
     *
     * @param buildingEvent the {@link BuildingEvent} that triggers this card's effect (usually a Shamanic event)
     * @param pointsEndgame the base points awarded at the end of the game
     * @param foodPrice     the base food cost to acquire this building
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card
     */
    public ShamanicImmunityCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
    }

    /**
     * Executes the special effect of this building.
     * <p>
     * This method is invoked by the {@link it.polimi.ingsw.gc49.server.model.EventManager}. It checks the player's current
     * point penalty ({@code getPointsToPay()}). If the value is strictly greater than 0
     * (meaning the player is penalized for losing the event), it resets the penalty to 0.
     * It deliberately ignores negative values, which represent rewards.
     */
    @Override
    public void onEventEffect() {
        if (owner.getPointsToPay() > 0) {
            owner.setPointsToPay(0);
            if(queueUpdater != null) {
                queueUpdater.queueUpdateModelElement(new TextModelElement(
                        "La carta " + simpleToString() + " si è attivata fornendo l'immunità sciamanica a " + owner.getNickname()
                ));
            }
        }
    }

    /**
     * Provides a detailed string representation of the card, including its stats and effect description.
     *
     * @return a multi-line {@link String} describing the card's attributes and the immunity effect
     */
    @Override
    public String toString() {
        return "ShamanicImmunityCard {\n" +
                " era = " + era +
                ", foodPrice = " + foodPrice +
                ", pointsEndgame = " + pointsEndgame +
                ",\n effect = if the player is the loser,then he doesn't get penalized during the shamanic event" +
                "\n}";
    }


    /**
     * Provides a localized, concise name for this specific building card.
     *
     * @return a simple one-line {@link String} ("EDIFICIO (immunit脿 sciamanica)")
     */
    @Override
    public String simpleToString () {
        return "EDIFICIO (immunit脿 sciamanica)";
    }


    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 2 and a width of 10. It uses green styling
     * for the borders and includes symbols to represent the endgame points (鈾?, food cost (鈾?,
     * and the specific tag ("imm.") indicating immunity for the shamanic event (搂).
     *
     * @return a {@link RectangleAttributedString} containing the colored terminal UI graphics
     */
    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        return it.polimi.ingsw.gc49.client.view.TextCardRenderer.render(simpleToString(), era);
    }
}

