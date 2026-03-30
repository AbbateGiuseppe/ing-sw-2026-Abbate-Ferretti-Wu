package it.polimi.ingsw.gc49;

public enum Era {
    FIRST,
    SECOND,
    THIRD,
    THIRD_FINAL;

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
}
