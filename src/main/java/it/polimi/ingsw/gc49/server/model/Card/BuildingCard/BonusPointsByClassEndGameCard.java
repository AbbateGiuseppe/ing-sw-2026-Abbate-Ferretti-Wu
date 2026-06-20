package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.TextModelElement;
import it.polimi.ingsw.gc49.server.model.BuildingEvent;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;


/**
 * Represents a specific type of {@link BuildingCard} that awards endgame bonus points.
 * <p>
 * When its corresponding event is triggered (typically at the end of the game),
 * this building calculates a point bonus based on the amount of a specific
 * {@link CharacterType} the owner possesses. The player receives a fixed number
 * of points for every unit of that chosen character class in their databank.
 */

public class BonusPointsByClassEndGameCard extends BuildingCard {
    /** The specific class of character (e.g., Artist, Hunter) this building counts for the bonus. */
    private final CharacterType unit;

    /** The amount of bonus points awarded for each character of the specified type. */
    private final int pointsPerUnit;

    /**
     * Constructs a new {@code BonusPointsByClassEndGameCard}.
     *
     * @param unit          the {@link CharacterType} to be counted for the bonus
     * @param pointsPerUnit the number of points awarded per unit of the specified character
     * @param buildingEvent the {@link BuildingEvent} that triggers this card's effect (usually Endgame)
     * @param pointsEndgame the base points awarded at the end of the game
     * @param foodPrice     the base food cost to acquire this building
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card
     */
    public BonusPointsByClassEndGameCard( CharacterType unit, int pointsPerUnit, BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(buildingEvent, pointsEndgame, foodPrice, era, minNumPlayers, queueUpdater);
        this.unit = unit;
        this.pointsPerUnit = pointsPerUnit;
    }


    /**
     * Executes the special effect of this building.
     * <p>
     * This method is invoked by the {@link it.polimi.ingsw.gc49.server.model.EventManager}. It counts the total number of
     * characters matching the specified {@link #unit} owned by the player, multiplies that
     * count by {@link #pointsPerUnit}, and adds the resulting points to the player's score.
     */
    @Override
    public void onEventEffect() {
        owner.addPoints(pointsPerUnit * owner.data.getCharacterCount(unit));
        if(queueUpdater != null) {
            queueUpdater.queueUpdateModelElement(new TextModelElement(
                    "La carta " + simpleToString() + " si è attivata fornendo 1 di punti a " + owner.getNickname() +
                            " per ogni suo " + unit.toString()
            ));
        }
    }

    /**
     * Provides a detailed string representation of the card, including its stats and effect description.
     *
     * @return a multi-line {@link String} describing the card's attributes and the dynamic multiplier effect
     */
    @Override
    public String toString() {
        return "BonusPointsByClassEndGameCard {\n" +
                " era = " + era +
                ", foodPrice = " + foodPrice +
                ", pointsEndgame = " + pointsEndgame +
                ",\n effect = get " + pointsPerUnit + " bonus points for each " + unit + " in possession at the end of the game" +
                "\n}";
    }


    /**
     * Provides a localized, concise name for this specific building card.
     *
     * @return a simple one-line {@link String} ("EDIFICIO (strapunti da classe)")
     */
    @Override
    public String simpleToString () {
        return "EDIFICIO (strapunti da classe)";
    }


    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 2 and a width of 10. It dynamically
     * appends a specific letter based on the {@link #unit} type (e.g., 'A' for Artist,
     * 'H' for Hunter) to visually indicate which character class grants the bonus.
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
                .style(AttributedStyle.DEFAULT).append("3♦x");
        switch(unit){
            case Artist -> attributedString.append("A");
            case Builder -> attributedString.append("B");
            case Gatherer -> attributedString.append("G");
            case Hunter -> attributedString.append("H");
            case Inventor -> attributedString.append("I");
            case Shaman -> attributedString.append("S");
        }
        attributedString.append("   ≥")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╝");
        int height = 2;
        int width = 10;
        return new RectangleAttributedString(height, width, attributedString.toAttributedString());
    }

    @Override
    public ItaEngRectangleAttributedString getItaEngRectangleAttributedString () {
        AttributedStringBuilder itaAttributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╔")
                .style(AttributedStyle.DEFAULT).append(String.valueOf(pointsEndgame)).append("♦")
                .append("    ").append(String.valueOf(foodPrice)).append("♥")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╗")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╚")
                .style(AttributedStyle.DEFAULT).append("3♦x");
        switch(unit){
            case Artist -> itaAttributedString.append("A   ≥");
            case Builder -> itaAttributedString.append("Co  ≥");
            case Gatherer -> itaAttributedString.append("R   ≥");
            case Hunter -> itaAttributedString.append("Ca  ≥");
            case Inventor -> itaAttributedString.append("I   ≥");
            case Shaman -> itaAttributedString.append("S   ≥");
        }
        itaAttributedString.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╝");

        AttributedStringBuilder engAttributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╔")
                .style(AttributedStyle.DEFAULT).append(String.valueOf(pointsEndgame)).append("♦")
                .append("    ").append(String.valueOf(foodPrice)).append("♥")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╗")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╚")
                .style(AttributedStyle.DEFAULT).append("3♦x");
        switch(unit){
            case Artist -> engAttributedString.append("A");
            case Builder -> engAttributedString.append("B");
            case Gatherer -> engAttributedString.append("G");
            case Hunter -> engAttributedString.append("H");
            case Inventor -> engAttributedString.append("I");
            case Shaman -> engAttributedString.append("S");
        }
        engAttributedString.append("   ≥").style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╝");
        int height = 2;
        int width = 10;
        return new ItaEngRectangleAttributedString(
                height, width, itaAttributedString.toAttributedString(),
                height, width, engAttributedString.toAttributedString());
    }
}
