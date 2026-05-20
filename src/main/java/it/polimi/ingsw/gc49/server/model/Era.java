package it.polimi.ingsw.gc49.server.model;

import it.polimi.ingsw.gc49.client.view.Rectangable;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

public enum Era implements Rectangable {
    FIRST,
    SECOND,
    THIRD,
    THIRD_FINAL;

    /**
     *
     * @return the value of the first ENUM.
     */
    public static Era first(){
        Era[] eras = Era.values();
        return eras[0];
    }

    /**
     *
     * @return the value of the last ENUM.
     */
    public static Era last(){
        Era[] eras = Era.values();
        return eras[eras.length - 1];
    }

    /**
     *
     * @return the value of the next ENUM. return null if it reached the end of the ENUMS.
     */
    public Era next() {
        Era[] eras = Era.values();

        // finds the index of the current instance, goes to the next.
        int nextIndex = this.ordinal() + 1;

        if (nextIndex < eras.length) {
            return eras[nextIndex]; //returns the next Era value.
        }else{
            return null; //returns null if it reached the end.
        }
    }

    /**
     *
     * @return true if this ENUM is the last one, otherwise returns false.
     */
    public boolean isFinal() {
        return next() == null;
    }

    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedStringBuilder attributedString = new AttributedStringBuilder();


        attributedString
                .append("╔═══╗")
                .append("║Era║");

        switch(this){
            case FIRST: attributedString
                    .append("║ 1 ║");
                break;
            case SECOND: attributedString
                    .append("║ 2 ║");
                break;
            case THIRD: attributedString
                    .append("║ 3 ║");
                break;
            case THIRD_FINAL: attributedString
                    .append("║fin║");
                break;
        }

        attributedString
                .append("╚═══╝");

        int height = 4;
        int width = 5;
        return new RectangleAttributedString(height, width, attributedString.toAttributedString());
    }
}
