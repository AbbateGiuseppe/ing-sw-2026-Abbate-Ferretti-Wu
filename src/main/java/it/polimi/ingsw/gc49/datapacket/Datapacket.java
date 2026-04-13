package it.polimi.ingsw.gc49.datapacket;

import java.io.Serializable;

public abstract class Datapacket implements Serializable {
    public enum DatapacketType { UPDATE_MODEL, COMMAND } //TODO: enumerate all possible data transfers.

    private final DatapacketType datapacketType;

    public Datapacket(final DatapacketType datapacketType) {
        this.datapacketType = datapacketType;
    }

    public DatapacketType getDatapacketType() {
        return datapacketType;
    }
}
