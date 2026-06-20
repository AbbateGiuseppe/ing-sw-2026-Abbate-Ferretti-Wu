package it.polimi.ingsw.gc49;

import org.jline.utils.AttributedStringBuilder;

public class ItaEngAttributedStringBuilder {
    public final AttributedStringBuilder itaString;
    public final AttributedStringBuilder engString;

    public ItaEngAttributedStringBuilder ( AttributedStringBuilder itaString, AttributedStringBuilder engString ) {
        this.itaString = itaString;
        this.engString = engString;
    }

    public AttributedStringBuilder print ( ItaEngString.Language language ) {
        return switch (language) {
            case ITA -> itaString;
            case ENG -> engString;
        };
    }
}
