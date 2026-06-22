package it.polimi.ingsw.gc49.server.model;

import it.polimi.ingsw.gc49.ItaEngString;
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

    public String itaEngCommand ( ItaEngString.Language language ){
        switch (this){
            case ORANGE:
                if(language == ItaEngString.Language.ITA){
                    return "arancione";
                } else {
                    return "orange";
                }
            case WHITE:
                if(language == ItaEngString.Language.ITA){
                    return "bianco";
                } else {
                    return "white";
                }
            case BLUE:
                if(language == ItaEngString.Language.ITA){
                    return "blu";
                } else {
                    return "blue";
                }
            case BLACK:
                if(language == ItaEngString.Language.ITA){
                    return "nero";
                } else {
                    return "black";
                }
            case YELLOW:
                if(language == ItaEngString.Language.ITA){
                    return "giallo";
                } else {
                    return "yellow";
                }
            default:
                throw new AssertionError();
        }
    }

}
