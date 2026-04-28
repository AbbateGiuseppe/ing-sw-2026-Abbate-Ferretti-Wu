package it.polimi.ingsw.gc49.datapacket;

import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

import java.io.Serializable;

public abstract class Datapacket implements Serializable {
    private String senderNickname;

    public enum DatapacketType { INITIALIZE_MODEL, UPDATE_MODEL, COMMAND, DISCONNECT, RECONNECT, ERROR,
                                 HALL_CLIENT, HALL_COMMAND, ROOM_CLIENT, ROOM_COMMAND }

    public final DatapacketType datapacketType;
    public final ApplicationPhase applicationPhase;

    public Datapacket ( DatapacketType datapacketType, ApplicationPhase applicationPhase ) {
        this.datapacketType = datapacketType;
        this.applicationPhase = applicationPhase;
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
