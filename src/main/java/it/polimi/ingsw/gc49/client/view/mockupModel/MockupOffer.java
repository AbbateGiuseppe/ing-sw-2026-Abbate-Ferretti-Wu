package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.client.view.Rectangable;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.Serializable;

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

    public MockupOffer(int foodGain, int upperDraw, int lowerDraw, Integer assignedPlayerIndex) {
        this.foodGain = foodGain;
        this.upperDraw = upperDraw;
        this.lowerDraw = lowerDraw;
        this.assignedPlayerIndex = assignedPlayerIndex;
    }


    //###setters
    public void setAssignedPlayerIndex(Integer assignedPlayerIndex) {
        this.assignedPlayerIndex = assignedPlayerIndex;
    }
    public void setGame(MockupGame game) { this.game = game; }

    //###getters
    public Integer getAssignedPlayerIndex() {
        return assignedPlayerIndex;
    }

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
