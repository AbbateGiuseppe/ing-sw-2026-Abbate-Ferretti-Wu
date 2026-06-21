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
 * Represents the Gatherer character card in the game.
 * <p>
 * During the Sustenance Event (see Event Descriptions on page 6), they provide a 3 Food discount
 * off the total you would have to pay.
 * Note: Never obtain Food directly from Gatherers.
 */
public class Gatherer extends CharacterCard {
    /**
     * Constructs a new {@code Gatherer} card.
     *
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card in the deck
     */
    public Gatherer ( Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(era, minNumPlayers, queueUpdater);
    }


    /**
     * Updates the player's databank upon acquiring the card.
     * <p>
     * When a player draws this card, this method registers the acquisition by:
     * <ul>
     * <li>Incrementing the {@link CharacterType#Gatherer} counter by 1.</li>
     * <li>Adding a fixed discount of 3 ({@code addNumSustenanceDiscount(3)}) to the player's total sustenance discount.</li>
     * </ul>
     *
     * @param dataBank the {@link DataBank} of the player acquiring the card
     */
    @Override
    public void updateDataBank(DataBank dataBank) {
         dataBank.addCharacterCount(CharacterType.Gatherer,1);
         dataBank.addNumSustenanceDiscount(3);
    }

    /**
     * Provides a detailed string representation of the card, including its stats.
     *
     * @return a multi-line {@link String} showing the card's era and its fixed sustenance discount of 3
     */
    @Override
    public String toString() {
        return "Gatherer {\n" +
                " era = " + era +
                ", sustenanceDiscount = 3" +
                "\n}";
    }

    /**
     * Provides a localized, concise name for this character card.
     *
     * @return a simple one-line {@link String} ("RACCOGLITORE")
     */
    @Override
    public String simpleToString () {
        return "RACCOGLITORE";
    }

    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 4 and a width of 5. It uses a yellow
     * border typical of character cards and dynamically displays the letter 'G' along with
     * the fixed sustenance discount of 3 food (3♥).
     *
     * @return a {@link RectangleAttributedString} containing the colored terminal UI graphics
     */
    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append(" G ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("3♥ ")
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
                .style(AttributedStyle.DEFAULT).append(" R ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("3♥ ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("╚═══╝").toAttributedString();
        AttributedString engAttributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append(" G ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("3♥ ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("╚═══╝").toAttributedString();
        int height = 4;
        int width = 5;
        return new ItaEngRectangleAttributedString(
                height, width, itaAttributedString,
                height, width, engAttributedString);
    }
}
