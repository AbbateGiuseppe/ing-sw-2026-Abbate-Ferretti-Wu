package it.polimi.ingsw.gc49;

public class ItaEngString {
    public enum Language { ITA, ENG }

    private final String itaString;
    private final String engString;

    public ItaEngString ( String itaString, String engString ) {
        this.itaString = itaString;
        this.engString = engString;
    }

    public String print ( Language language ) {
        return switch (language) {
            case ITA -> itaString;
            case ENG -> engString;
        };
    }
}
