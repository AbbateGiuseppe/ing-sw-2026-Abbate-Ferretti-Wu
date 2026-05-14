package it.polimi.ingsw.gc49.client.view;

import org.jline.utils.AttributedString;

public class RectangleAttributedString {
    public final int height;
    public final int width;
    public final AttributedString attributedString;

    public RectangleAttributedString(int height, int width, AttributedString attributedString) {
        this.height = height;
        this.width = width;
        this.attributedString = attributedString;
    }
}
