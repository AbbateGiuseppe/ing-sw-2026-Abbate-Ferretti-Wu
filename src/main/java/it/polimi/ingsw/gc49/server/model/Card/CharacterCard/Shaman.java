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
 * Represents the Shaman character card in the game.
 * <p>
 * They can display 1 to 3 icons.
 * During the Shamanic Ritual Event, having a majority of these icons
 * provides PP;
 * having a minority of them, however, causes PP to be lost .*/
public class Shaman extends CharacterCard {
    /** The number of shamanic stars this card permanently provides to the owner. */
    private final int numStars;

    /**
     * Constructs a new {@code Shaman} card.
     *
     * @param numStars      the amount of stars provided by this shaman
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card in the deck
     */
    public Shaman( int numStars, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(era, minNumPlayers, queueUpdater);
        this.numStars = numStars;
    }

    /**
     * Updates the player's databank upon acquiring the card.
     * <p>
     * When a player draws this card, this method registers the acquisition by:
     * <ul>
     * <li>Adding this card's {@link #numStars} to the player's total stars.</li>
     * <li>Incrementing the {@link CharacterType#Shaman} counter by 1.</li>
     * </ul>
     *
     * @param dataBank the {@link DataBank} of the player acquiring the card
     */
    @Override
    public void updateDataBank(DataBank dataBank) {
        dataBank.addNumStar(numStars);
        dataBank.addCharacterCount(CharacterType.Shaman,1);
    }

    /**
     * Provides a detailed string representation of the card, including its stats.
     *
     * @return a multi-line {@link String} showing the card's era and provided stars
     */
    @Override
    public String toString() {
        return "Shaman {\n" +
                " era = " + era +
                ", numStars = " + numStars +
                "\n}";
    }

    /**
     * Provides a localized, concise name for this character card.
     *
     * @return a simple one-line {@link String} ("SCIAMANO")
     */
    @Override
    public String simpleToString () {
        return "SCIAMANO";
    }

    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 4 and a width of 5. It uses a yellow
     * border typical of character cards and displays the letter 'S' along with the
     * specific number of stars (*) this character grants.
     *
     * @return a {@link RectangleAttributedString} containing the colored terminal UI graphics
     */
    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append(" S ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append(String.valueOf(numStars)).append("* ")
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
                .style(AttributedStyle.DEFAULT).append(" S ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append(String.valueOf(numStars)).append("* ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("╚═══╝").toAttributedString();
        int height = 4;
        int width = 5;
        return new ItaEngRectangleAttributedString(
                height, width, itaAttributedString,
                height, width, itaAttributedString);
    }
}
