package it.polimi.ingsw.gc49.server.model.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.DataBank;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Invention;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public class Inventor extends CharacterCard {
    private final Invention invention;

    public Inventor( Invention invention, Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
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
                ", invention = " + invention +
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
}
