package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.client.view.Rectangable;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;
import org.w3c.dom.Attr;

public class MockupOffer implements Rectangable {
    /** The player currently assigned to the offer.*/
    private MockupPlayer assignedPlayer;
    /** The food gained on effect activation.*/
    private final int foodGain;
    /** The upper line drawable cards gained on effect activation.*/
    private final int upperDraw;
    /** The lower line drawable cards on effect activation.*/
    private final int lowerDraw;

    public MockupOffer(int foodGain, int upperDraw, int lowerDraw) {
        this.foodGain = foodGain;
        this.upperDraw = upperDraw;
        this.lowerDraw = lowerDraw;
    }

    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        int height = 5;
        int width = 5;
        //First line
        AttributedStringBuilder attributedString = new AttributedStringBuilder().append("╔═══╗");

        //Next 1 player line
        if(assignedPlayer == null) {
            attributedString.append("║ ░ ║");
        }else{
            attributedString.
                    append("║ ")
                    //TODO: choose color based on totem color!
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("▓")
                    .style(AttributedStyle.DEFAULT).append(" ║");
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
