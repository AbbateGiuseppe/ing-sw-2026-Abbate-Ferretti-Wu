package it.polimi.ingsw.gc49.client.view;

import org.jline.utils.AttributedString;

public class ItaEngRectangleAttributedString {
    public final int itaHeight;
    public final int itaWidth;
    public final AttributedString itaAttributedString;
    public final int engHeight;
    public final int engWidth;
    public final AttributedString engAttributedString;

    public ItaEngRectangleAttributedString(int itaHeight, int itaWidth, AttributedString itaAttributedString,
                                     int engHeight, int engWidth, AttributedString engAttributedString) {
        this.itaHeight = itaHeight;
        this.itaWidth = itaWidth;
        this.itaAttributedString = itaAttributedString;
        this.engHeight = engHeight;
        this.engWidth = engWidth;
        this.engAttributedString = engAttributedString;
    }
}
