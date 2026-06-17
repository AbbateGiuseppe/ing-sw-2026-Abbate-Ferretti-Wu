package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsOneModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.TextModelElement;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

/**
 * Represents a specific type of {@link BuildingCard} that awards endgame bonus points for completed character sets.
 * <p>
 * When its corresponding event is triggered (typically at the end of the game),
 * this building evaluates the owner's databank and grants <b>6 bonus points</b>
 * for every fully completed set of character cards they possess.
 */
public class CharacterSetCompletePointEndGameCard extends BuildingCard {

    /**
     * Constructs a new {@code CharacterSetCompletePointEndGameCard}.
     *
     * @param buildingEvent the {@link BuildingEvent} that triggers this card's effect (usually Endgame)
     * @param pointsEndgame the base points awarded at the end of the game
     * @param foodPrice     the base food cost to acquire this building
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card
     */
    public CharacterSetCompletePointEndGameCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
    }


    /**
     * Executes the special effect of this building.
     * <p>
     * This method is invoked by the {@link it.polimi.ingsw.gc49.server.model.EventManager}. It counts the total number of
     * complete character sets ({@link CharacterType#CompleteSet}) owned by the player,
     * multiplies that amount by 6, and adds the resulting points to the player's total score.
     */
    @Override
    public void onEventEffect() {
         owner.addPoints(6 * owner.data.getCharacterCount(CharacterType.CompleteSet));
        if(queueUpdater != null) {
            queueUpdater.queueUpdateModelElement(new TextModelElement(
                    "La carta " + simpleToString() + " si è attivata fornendo 6 di punti a " + owner.getNickname() +
                            " per ogni suo set"
            ));
        }
    }


    /**
     * Provides a detailed string representation of the card, including its stats and effect description.
     *
     * @return a multi-line {@link String} describing the card's attributes and the set multiplier effect
     */
    @Override
    public String toString() {
        return "CharacterSetCompletePointEndGameCard {\n" +
                " era = " + era +
                ", foodPrice = " + foodPrice +
                ", pointsEndgame = " + pointsEndgame +
                ",\n effect = get 6 bonus points for each completed set of character cards at the end of the game" +
                "\n}";
    }


    /**
     * Provides a localized, concise name for this specific building card.
     *
     * @return a simple one-line {@link String} ("EDIFICIO (strapunti da set)")
     */
    @Override
    public String simpleToString () {
        return "EDIFICIO (strapunti da set)";
    }


    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 2 and a width of 10. It uses green styling
     * for the borders and includes symbols to represent the endgame points (♦), food cost (♥),
     * and the specific bonus multiplier for sets (6♦x●).
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
                .style(AttributedStyle.DEFAULT).append("6♦x●   ≥")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╝")
                .toAttributedString();
        int height = 2;
        int width = 10;
        return new RectangleAttributedString(height, width, attributedString);
    }
}
