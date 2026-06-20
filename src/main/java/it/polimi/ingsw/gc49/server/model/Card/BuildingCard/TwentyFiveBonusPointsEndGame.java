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
 * Represents a specific type of {@link BuildingCard} that grants a massive flat point bonus.
 * <p>
 * Unlike other endgame buildings that calculate their bonuses based on character sets
 * or specific classes, this card provides a direct and unconditional reward of
 * <b>25 bonus points</b> to its owner when the game ends.
 */
public class TwentyFiveBonusPointsEndGame extends BuildingCard {
    /**
     * Constructs a new {@code TwentyFiveBonusPointsEndGame}.
     *
     * @param buildingEvent the {@link BuildingEvent} that triggers this card's effect (usually Endgame)
     * @param pointsEndgame the base points awarded at the end of the game
     * @param foodPrice     the base food cost to acquire this building
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card
     */
    public TwentyFiveBonusPointsEndGame ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
    }


    /**
     * Executes the special effect of this building.
     * <p>
     * This method is invoked by the {@link it.polimi.ingsw.gc49.server.model.EventManager}. It directly accesses the
     * {@link it.polimi.ingsw.gc49.server.model.Player} owner and adds exactly 25 points to their total score.
     */
    @Override
    public void onEventEffect() {
        owner.addPoints(25);
        if(queueUpdater != null) {
            queueUpdater.queueUpdateModelElement(new TextModelElement(
                    "La carta " + simpleToString() + " si è attivata fornendo 25 punti a " + owner.getNickname()
            ));
        }
    }

    /**
     * Provides a detailed string representation of the card, including its stats and effect description.
     *
     * @return a multi-line {@link String} describing the card's attributes and the 25-point bonus
     */
    @Override
    public String toString() {
        return "TwentyFiveBonusPointsEndGame {\n" +
                " era = " + era +
                ", foodPrice = " + foodPrice +
                ", pointsEndgame = " + pointsEndgame +
                ",\n effect = get 25 bonus points at the end of the game" +
                "\n}";
    }

    /**
     * Provides a localized, concise name for this specific building card.
     *
     * @return a simple one-line {@link String} ("EDIFICIO (25 punti finali)")
     */
    @Override
    public String simpleToString () {
        return "EDIFICIO (25 punti finali)";
    }

    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 2 and a width of 10. It uses green styling
     * for the borders and includes symbols to represent the endgame points (鈾?, food cost (鈾?,
     * and the specific flat point reward (25鈾? tied to the endgame event (鈮?.
     *
     * @return a {@link RectangleAttributedString} containing the colored terminal UI graphics
     */
    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        return it.polimi.ingsw.gc49.client.view.TextCardRenderer.render(simpleToString(), era);
    }
}

