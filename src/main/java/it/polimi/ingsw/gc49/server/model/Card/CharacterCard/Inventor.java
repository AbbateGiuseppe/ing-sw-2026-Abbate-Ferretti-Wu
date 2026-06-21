package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.*;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;


/**
 * Represents the Inventor character card in the game.
 * At the end of the game, this provides a number of
 * Prestige points equal to the number of inventors in your
 * tribe multiplied by the number of different invention icons
 * you have.
 * There are 10 different Invention icons.
 * <p>
 * These inventions are tracked
 * in the databank and can trigger powerful synergies or bonuses (such as the rewards
 * provided by the {@code SamePairInventionsCard}).
 */
public class Inventor extends CharacterCard {
    private final Invention invention;

    public Inventor( Invention invention, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(era, minNumPlayers, queueUpdater);
        this.invention = invention;
    }

    @Override
    public void updateDataBank( DataBank dataBank) {
         dataBank.addCharacterCount(CharacterType.Inventor,1);
         dataBank.addInvention(invention);
    }

    @Override
    public String toString() {
        return "Inventor {\n" +
                " era = " + era +
                ", invention = " + invention.ordinal() +
                "\n}";
    }

    @Override
    public String simpleToString () {
        return "INVENTORE";
    }

    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append(" I ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("(").append(String.valueOf(invention.ordinal())).append(")")
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
                .style(AttributedStyle.DEFAULT).append(" I ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("(").append(String.valueOf(invention.ordinal())).append(")")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                .append("╚═══╝").toAttributedString();
        int height = 4;
        int width = 5;
        return new ItaEngRectangleAttributedString(
                height, width, itaAttributedString,
                height, width, itaAttributedString);
    }
}
