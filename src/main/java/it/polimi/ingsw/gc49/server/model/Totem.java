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


    public AttributedString getTotemAttributedString(){
        AttributedStringBuilder attributedString = new AttributedStringBuilder();

        switch(this){
            case ORANGE:
                attributedString.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)).append("▓")
                        .style(AttributedStyle.DEFAULT);
                break;
            case WHITE:
                attributedString.style(AttributedStyle.DEFAULT.bold().foreground(AttributedStyle.WHITE)).append("▓")
                        .style(AttributedStyle.DEFAULT);
                break;
            case BLUE:
                attributedString.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE)).append("▓")
                        .style(AttributedStyle.DEFAULT);
                break;
            case BLACK:
                attributedString.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.BLACK).background(AttributedStyle.WHITE)).append("▓")
                        .style(AttributedStyle.DEFAULT);
                break;
            case YELLOW:
                attributedString.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("▓")
                        .style(AttributedStyle.DEFAULT);
                break;
        }

        return attributedString.toAttributedString();
    }

}
