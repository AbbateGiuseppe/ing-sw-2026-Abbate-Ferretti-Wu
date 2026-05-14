package it.polimi.ingsw.gc49.server.model.Card.TribeCards.CharacterCard;

import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.server.model.CharacterType;
import it.polimi.ingsw.gc49.server.model.DataBank;
import it.polimi.ingsw.gc49.server.model.Era;
import it.polimi.ingsw.gc49.server.model.Player;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public class Hunter extends CharacterCard {
    private final boolean drumstick;

    public Hunter( boolean drumstick, Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
        this.drumstick = drumstick;
    }

    @Override
    public void updateDataBank(DataBank dataBank) {
        dataBank.addCharacterCount(CharacterType.Hunter,1);
    }

    @Override
    public void onDraw( Player player ) {
        // if the card has drumstick symbol,then give the player an amount of food equal to the number of Hunter cards that he has
        if (drumstick) {
            player.addFood(player.data.getCharacterCount(CharacterType.Hunter));
        }
    }

    @Override
    public String toString() {
        return "Hunter{" +
                "era=" + era +
                ", drumstick=" + drumstick +
                '}';
    }

    @Override
    public String simpleToString () {
        return "CACCIATORE";
    }

    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString;
        if(drumstick) {
            attributedString = new AttributedStringBuilder()
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                    .append("║")
                    .style(AttributedStyle.DEFAULT).append(" H ")
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                    .append("║")
                    .style(AttributedStyle.DEFAULT).append("♥♥♥")
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                    .append("╚═══╝").toAttributedString();
        } else {
            attributedString = new AttributedStringBuilder()
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                    .append("║")
                    .style(AttributedStyle.DEFAULT).append(" H ")
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                    .append("║")
                    .style(AttributedStyle.DEFAULT).append("   ")
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                    .append("╚═══╝").toAttributedString();
        }
        int height = 4;
        int width = 5;
        return new RectangleAttributedString(height, width, attributedString);
    }
}
