package it.polimi.ingsw.gc49.client.view;

import it.polimi.ingsw.gc49.ItaEngString;
import org.jline.utils.AttributedString;

public class ItaEngRectangleAttributedString {
    public final RectangleAttributedString itaRectangleAttributedString;
    public final RectangleAttributedString engRectangleAttributedString;

    public ItaEngRectangleAttributedString(int itaHeight, int itaWidth, AttributedString itaAttributedString,
                                     int engHeight, int engWidth, AttributedString engAttributedString) {
        this.itaRectangleAttributedString = new RectangleAttributedString(itaHeight, itaWidth, itaAttributedString);
        this.engRectangleAttributedString = new RectangleAttributedString(engHeight, engWidth, engAttributedString);
    }

    public RectangleAttributedString localize ( ItaEngString.Language language ) {
        return switch (language) {
            case ITA -> itaRectangleAttributedString;
            case ENG -> engRectangleAttributedString;
        };
    }
}
