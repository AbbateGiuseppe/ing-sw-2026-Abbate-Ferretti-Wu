package it.polimi.ingsw.gc49.client.view;

import org.jline.utils.AttributedString;

/**
 * It contains all the elements to display a two-dimensional text image,
 * by rending the one-dimensional {@link #attributedString} into a two-dimensional {@link #width} x {@link #height}.
 */
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
