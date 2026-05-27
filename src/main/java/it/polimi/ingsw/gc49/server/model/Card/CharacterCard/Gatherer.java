package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.DataBank;
import it.polimi.ingsw.gc49.server.model.Era;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public class Gatherer extends CharacterCard {
    public Gatherer ( Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
         dataBank.addCharacterCount(CharacterType.Gatherer,1);
         dataBank.addNumSustenanceDiscount(3);
    }

    @Override
    public String toString() {
        return "Gatherer {\n" +
                " era = " + era +
                ", sustenanceDiscount = 3" +
                "\n}";
    }

    @Override
    public String simpleToString () {
        return "RACCOGLITORE";
    }

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
}
