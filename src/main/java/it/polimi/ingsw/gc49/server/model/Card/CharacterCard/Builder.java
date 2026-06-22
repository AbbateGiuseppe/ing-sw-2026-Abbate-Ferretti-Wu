package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.DataBank;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

/**
 * Represents the Builder character card in the game.
 * <p>
 * During the game, each Builder reduces the Food cost
 * of each Building you take by the amount indicated in the top right.
 * At the end of the game, each Builder provides the Prestige Points
 * indicated in the bottom left of the card.
 * Can also awards a specific amount of builder points
 * (which can be leveraged by other cards, like the {@code DoubleBuilderPointsCard}).
 */
public class Builder extends CharacterCard {
    /** The permanent food discount this builder grants when acquiring new buildings. */
    private final int buildingDiscount;

    /** The specific builder points awarded to the owner by this card. */
    private final int numPoints;

    /**
     * Constructs a new {@code Builder} card.
     *
     * @param buildingDiscount the amount of food discount granted
     * @param numPoints        the amount of builder points provided
     * @param era              the {@link Era} this card belongs to
     * @param minNumPlayers    the minimum number of players required to include this card in the deck
     */
    public Builder(int buildingDiscount, int numPoints, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(era, minNumPlayers, queueUpdater);
        this.buildingDiscount = buildingDiscount;
        this.numPoints = numPoints;
    }

    /**
     * Updates the player's databank upon acquiring the card.
     * <p>
     * When a player draws this card, this method registers the acquisition by:
     * <ul>
     * <li>Incrementing the {@link CharacterType#Builder} counter by 1.</li>
     * <li>Adding this card's {@link #buildingDiscount} to the player's total building discount.</li>
     * <li>Adding this card's {@link #numPoints} to the player's total builder points.</li>
     * </ul>
     *
     * @param dataBank the {@link DataBank} of the player acquiring the card
     */
    @Override
    public void updateDataBank(DataBank dataBank) {
        dataBank.addCharacterCount(CharacterType.Builder,1);
        dataBank.addNumBuildingDiscount(buildingDiscount);
        dataBank.addNumBuilderPoints(numPoints);
    }

    public int getBuildingDiscount() {
        return buildingDiscount;
    }

    /**
     * Provides a detailed string representation of the card, including its stats.
     *
     * @return a multi-line {@link String} showing the card's era, discount, and points
     */
    @Override
    public String toString() {
        return "Builder {\n" +
                " era = " +  era +
                ", buildingDiscount = " + buildingDiscount +
                ", numPoints = " + numPoints +
                "\n}";
    }

    /**
     * Provides a localized, concise name for this character card.
     *
     * @return a simple one-line {@link String} ("COSTRUTTORE")
     */
    @Override
    public String simpleToString () {
        return "COSTRUTTORE";
    }


    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 4 and a width of 5. It uses a yellow
     * border typical of character cards and dynamically displays the letter 'B' along with
     * the specific discount value (♥) and builder points (♦).
     *
     * @return a {@link RectangleAttributedString} containing the colored terminal UI graphics
     */
    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("B").append(String.valueOf(buildingDiscount)).append("♥")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append(" ").append(String.valueOf(numPoints)).append("♦")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("╚═══╝").toAttributedString();
        int height = 4;
        int width = 5;
        return new RectangleAttributedString(height, width, attributedString);
    }

    @Override
    public ItaEngRectangleAttributedString getItaEngRectangleAttributedString () {
        AttributedString itaAttributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("Co").append("♥")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append(String.valueOf(numPoints)).append("♦").append(String.valueOf(buildingDiscount))
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("╚═══╝").toAttributedString();
        AttributedString engAttributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("B").append(String.valueOf(buildingDiscount)).append("♥")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append(" ").append(String.valueOf(numPoints)).append("♦")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("╚═══╝").toAttributedString();
        int height = 4;
        int width = 5;
        return new ItaEngRectangleAttributedString(
                height, width, itaAttributedString,
                height, width, engAttributedString);
    }
}
