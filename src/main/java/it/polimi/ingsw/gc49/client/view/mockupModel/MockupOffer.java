package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.client.view.Rectangable;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.Serializable;


/**
 * The {@code MockupOffer} class represents a single slot on the offer board within the game's client-side mockup.
 * Players can assign themselves to an offer to gain specific rewards such as food or the ability
 * to draw cards from the upper or lower lines.
 * It implements {@link Rectangable} to allow the text-based user interface to render it as a styled ASCII box.
 */
public class MockupOffer implements Serializable, Rectangable {
    private MockupGame game;
    /** The player currently assigned to the offer.*/
    private Integer assignedPlayerIndex;
    /** The food gained on effect activation.*/
    private final int foodGain;
    /** The upper line drawable cards gained on effect activation.*/
    private final int upperDraw;
    /** The lower line drawable cards on effect activation.*/
    private final int lowerDraw;


    /**
     * Constructs a new {@code MockupOffer} with the specified rewards and initial state.
     *
     * @param foodGain            the amount of food provided by the offer.
     * @param upperDraw           the number of upper line cards that can be drawn.
     * @param lowerDraw           the number of lower line cards that can be drawn.
     * @param assignedPlayerIndex the index of the player assigned to this offer (can be {@code null}).
     */
    public MockupOffer(int foodGain, int upperDraw, int lowerDraw, Integer assignedPlayerIndex) {
        this.foodGain = foodGain;
        this.upperDraw = upperDraw;
        this.lowerDraw = lowerDraw;
        this.assignedPlayerIndex = assignedPlayerIndex;
    }


    // ============================================================
    // ### SETTERS
    // ============================================================

    /**
     * Updates the player assigned to this offer.
     *
     * @param assignedPlayerIndex the index of the new player, or {@code null} to unassign.
     */
    public void setAssignedPlayerIndex(Integer assignedPlayerIndex) {
        this.assignedPlayerIndex = assignedPlayerIndex;
    }

    /**
     * Sets the reference to the parent game mockup.
     * Essential for retrieving the assigned player's totem color during rendering.
     *
     * @param game the {@link MockupGame} instance holding this offer.
     */
    public void setGame(MockupGame game) { this.game = game; }

    // ============================================================
    // ### GETTERS
    // ============================================================

    /**
     * Retrieves the index of the player currently assigned to this offer.
     *
     * @return the player's index, or {@code null} if the offer is vacant.
     */
    public Integer getAssignedPlayerIndex() {
        return assignedPlayerIndex;
    }


    // ============================================================
    // ### RENDERING
    // ============================================================

    /**
     * Generates a graphical representation of the offer using ASCII characters and ANSI colors.
     * The resulting box is 5 characters wide and 5 lines tall. It dynamically displays
     * the assigned player's colored totem (or a placeholder if empty) and the specific rewards
     * (food or colored arrows indicating card draws).
     *
     * @return a {@link RectangleAttributedString} containing the stylized box.
     */
    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        int height = 5;
        int width = 5;
        //First line
        AttributedStringBuilder attributedString = new AttributedStringBuilder().append("╔═══╗");

        //Next 1 player line
        if(assignedPlayerIndex == null) {
            attributedString.append("║ ░ ║");
        }else{
            attributedString
                    .append("║ ")
                    //Adds color based on player's totem color
                    .append(game.getPlayer(assignedPlayerIndex).getTotem().getTotemAttributedString())
                    .append(" ║");
        }

        //Next 2 lines
        if(foodGain>0) {
            attributedString
                    .append("║ ").append(String.valueOf(foodGain)).append("♥").append("║")
                    .append("║   ║");
        }else if(lowerDraw>0){
            if(upperDraw>0) {
                attributedString
                        .append("║ ").append(String.valueOf(lowerDraw))
                        .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)).append("▼")
                        .style(AttributedStyle.DEFAULT).append("║")
                        .append("║ ").append(String.valueOf(upperDraw))
                        .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)).append("▲")
                        .style(AttributedStyle.DEFAULT).append("║");
            }else{
                attributedString
                        .append("║ ").append(String.valueOf(lowerDraw))
                        .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)).append("▼")
                        .style(AttributedStyle.DEFAULT).append("║")
                        .append("║   ║");
            }
        }else{
            attributedString
                    .append("║ ").append(String.valueOf(upperDraw))
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)).append("▲")
                    .style(AttributedStyle.DEFAULT).append("║")
                    .append("║   ║");
        }

        //Last line
        attributedString.append("╚═══╝");
        return new RectangleAttributedString(height, width, attributedString.toAttributedString());
    }
}
