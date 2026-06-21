package it.polimi.ingsw.gc49.server.model.Card.CharacterCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsOneModelElement;
import it.polimi.ingsw.gc49.server.model.*;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;


/**
 * Represents the Hunter character card in the game.
 * <p>
 * Whenever you add a
 * Hunter without an icon to your tribe, you gain nothing. Whenever
 * you add a Hunter with an icon,
 * you immediately take 1 Food for each Hunter
 * in your tribe (with or without an icon).
 * During the Hunt Event, take Food and gain
 * PP based on the number of Hunters in your tribe.
 */
public class Hunter extends CharacterCard {
    /** Indicates whether this specific Hunter card has the drumstick bonus symbol. */
    private final boolean drumstick;


    /**
     * Constructs a new {@code Hunter} card.
     *
     * @param drumstick     {@code true} if the card grants an immediate food bonus on draw, {@code false} otherwise
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card in the deck
     */
    public Hunter( boolean drumstick, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(era, minNumPlayers, queueUpdater);
        this.drumstick=drumstick;
    }


    /**
     * Updates the player's databank upon acquiring the card.
     * <p>
     * When a player draws this card, this method registers the acquisition by
     * incrementing the generic {@link CharacterType#Hunter} counter by 1.
     *
     * @param dataBank the {@link DataBank} of the player acquiring the card
     */

    @Override
    public void updateDataBank(DataBank dataBank) {
        dataBank.addCharacterCount(CharacterType.Hunter,1);
    }


    /**
     * Handles the logic upon drawing the card, specifically applying the drumstick bonus.
     * <p>
     * If this card possesses the drumstick symbol ({@code drumstick == true}), it immediately
     * rewards the player with an amount of food equal to the total number of Hunter cards
     * they currently possess in their databank.
     *
     * @param player the {@link Player} acquiring the card
     */
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


    /**
     * Provides a detailed string representation of the card, including its stats.
     *
     * @return a multi-line {@link String} showing the card's era and its drumstick status
     */
    @Override
    public String toString() {
        return "Hunter {\n" +
                " era = " + era +
                ", drumstick = " + drumstick +
                "\n}";
    }

    /**
     * Provides a localized, concise name for this character card.
     *
     * @return a simple one-line {@link String} ("CACCIATORE")
     */
    @Override
    public String simpleToString () {
        return "CACCIATORE";
    }

    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 4 and a width of 5. It uses a yellow
     * border typical of character cards and displays the letter 'H'. If the card possesses
     * the {@link #drumstick} bonus, it additionally displays three heart symbols (♥♥♥)
     * to indicate the immediate food reward.
     *
     * @return a {@link RectangleAttributedString} containing the colored terminal UI graphics
     */

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

