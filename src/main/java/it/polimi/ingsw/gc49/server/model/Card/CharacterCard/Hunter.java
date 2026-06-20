package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsOneModelElement;
import it.polimi.ingsw.gc49.server.model.*;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public class Hunter extends CharacterCard {
    private final boolean drumstick;

    public Hunter( boolean drumstick, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(era, minNumPlayers, queueUpdater);
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
            if(queueUpdater != null) {
                queueUpdater.queueUpdateModelElement(new FoodAndPointsOneModelElement(
                        "La carta " + simpleToString() + " si è attivata alla pesca fornendo "
                                + player.data.getCharacterCount(CharacterType.Hunter) + " di cibo a " + player.getNickname(),
                        player.getPlayerIndex(),
                        player.getFood(),
                        player.getPoints()
                ));
            }
        }
    }

    @Override
    public String toString() {
        return "Hunter {\n" +
                " era = " + era +
                ", drumstick = " + drumstick +
                "\n}";
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

    @Override
    public ItaEngRectangleAttributedString getItaEngRectangleAttributedString () {
        AttributedString itaAttributedString;
        AttributedString engAttributedString;
        if(drumstick) {
            itaAttributedString = new AttributedStringBuilder()
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                    .append("║")
                    .style(AttributedStyle.DEFAULT).append(" Ca")
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                    .append("║")
                    .style(AttributedStyle.DEFAULT).append("♥♥♥")
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                    .append("╚═══╝").toAttributedString();
            engAttributedString = new AttributedStringBuilder()
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                    .append("║")
                    .style(AttributedStyle.DEFAULT).append(" H ")
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                    .append("║")
                    .style(AttributedStyle.DEFAULT).append("♥♥♥")
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                    .append("╚═══╝").toAttributedString();
        } else {
            itaAttributedString = new AttributedStringBuilder()
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╔═══╗")
                    .append("║")
                    .style(AttributedStyle.DEFAULT).append(" Ca")
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                    .append("║")
                    .style(AttributedStyle.DEFAULT).append("   ")
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("║")
                    .append("╚═══╝").toAttributedString();
            engAttributedString = new AttributedStringBuilder()
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
        return new ItaEngRectangleAttributedString(
                height, width, itaAttributedString,
                height, width, engAttributedString);
    }
}
