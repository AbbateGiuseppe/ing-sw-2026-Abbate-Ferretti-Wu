package it.polimi.ingsw.gc49.datapacket;

import java.io.Serializable;

public abstract class Datapacket implements Serializable {
    private String senderNickname;

    public enum DatapacketType { INITIALIZE_MODEL, UPDATE_MODEL, COMMAND, DISCONNECT, RECONNECT, ERROR,
                                 HALL_RETURN, HALL_COMMAND, ROOM_RETURN, ROOM_COMMAND }

    public final DatapacketType datapacketType;
    //TODO: Maybe the datapacket should store the sender's name...
    public Datapacket(final DatapacketType datapacketType) {
        this.datapacketType = datapacketType;
    }

    public DatapacketType getDatapacketType() {
        return datapacketType;
    }


    public String getSenderNickname() {
        return senderNickname;
    }
    public void setSenderNickname ( String senderNickname ) {
        this.senderNickname = senderNickname;
    }
}
