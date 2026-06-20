package it.polimi.ingsw.gc49.server.model;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public enum Totem {
    ORANGE,
    WHITE,
    BLUE,
    BLACK,
    YELLOW;

    public AttributedString getTotemAttributedString() {
        AttributedStringBuilder attributedString = new AttributedStringBuilder();
        AttributedStyle style = switch (this) {
            case ORANGE -> AttributedStyle.DEFAULT.foreground(AttributedStyle.RED);
            case WHITE -> AttributedStyle.DEFAULT.bold().foreground(AttributedStyle.WHITE);
            case BLUE -> AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE);
            case BLACK -> AttributedStyle.DEFAULT.foreground(AttributedStyle.BLACK).background(AttributedStyle.WHITE);
            case YELLOW -> AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW);
        };
        return attributedString.style(style).append("#").style(AttributedStyle.DEFAULT).toAttributedString();
    }
}
