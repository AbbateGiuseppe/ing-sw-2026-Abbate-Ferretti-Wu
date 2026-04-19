package it.polimi.ingsw.gc49.model;

public enum Era {
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
}
