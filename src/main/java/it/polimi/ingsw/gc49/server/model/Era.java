package it.polimi.ingsw.gc49.server.model;

import it.polimi.ingsw.gc49.client.view.Rectangable;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.TextCardRenderer;

public enum Era implements Rectangable {
    FIRST,
    SECOND,
    THIRD,
    THIRD_FINAL;

    public static Era first() {
        return Era.values()[0];
    }

    public static Era last() {
        Era[] eras = Era.values();
        return eras[eras.length - 1];
    }

    public Era next() {
        int nextIndex = this.ordinal() + 1;
        Era[] eras = Era.values();
        return nextIndex < eras.length ? eras[nextIndex] : null;
    }

    public boolean isFinal() {
        return next() == null;
    }

    @Override
    public RectangleAttributedString getRectangleAttributedString() {
        return TextCardRenderer.renderEra(this);
    }
}
