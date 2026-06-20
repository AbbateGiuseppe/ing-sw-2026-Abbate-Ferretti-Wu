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

public class Artist extends CharacterCard {
    public Artist ( Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(era, minNumPlayers, queueUpdater);
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
        dataBank.addCharacterCount(CharacterType.Artist,1);
    }

    @Override
    public String toString() {
        return "Artist {\n" +
                " era = " +  era +
                "\n}";
    }

    @Override
    public String simpleToString () {
        return "ARTISTA";
    }

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
