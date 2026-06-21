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
 * Represents the Artist character card in the game.
 * <p>
 * During the Cave Paintings Event, you can gain
 * or lose Prestige Points based on the number
 * of Artists you have in your tribe.
 * At the end of the game, you gain 10 Prestige Points for every
 * two Painters in your tribe.
 * Character cards like the Artist do not usually have direct activated abilities.
 */
public class Artist extends CharacterCard {
    /**
     * Constructs a new {@code Artist} card.
     *
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card in the deck
     */
    public Artist ( Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(era, minNumPlayers, queueUpdater);
    }

    /**
     * Updates the player's databank upon acquiring the card.
     * <p>
     * When a player draws this card, this method is called to permanently record
     * the acquisition. It increments the specific counter for {@link CharacterType#Artist}
     * by 1 in the player's databank, which can later be used to calculate event bonuses.
     *
     * @param dataBank the {@link DataBank} of the player acquiring the card
     */
    @Override
    public void updateDataBank(DataBank dataBank) {
        dataBank.addCharacterCount(CharacterType.Artist,1);
    }

    /**
     * Provides a detailed string representation of the card.
     *
     * @return a multi-line {@link String} showing the card's class and era
     */
    @Override
    public String toString() {
        return "Artist {\n" +
                " era = " +  era +
                "\n}";
    }

    /**
     * Provides a localized, concise name for this character card.
     *
     * @return a simple one-line {@link String} ("ARTISTA")
     */
    @Override
    public String simpleToString () {
        return "ARTISTA";
    }

    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 4 and a width of 5. It features
     * a distinctive yellow border to distinguish it from other card types, and displays
     * the letter 'A' in the center to denote an Artist.
     *
     * @return a {@link RectangleAttributedString} containing the colored terminal UI graphics
     */
    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append(" A ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("   ")
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
                .style(AttributedStyle.DEFAULT).append(" A ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("   ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("╚═══╝").toAttributedString();
        int height = 4;
        int width = 5;
        return new ItaEngRectangleAttributedString(
                height, width, itaAttributedString,
                height, width, itaAttributedString);
    }
}
