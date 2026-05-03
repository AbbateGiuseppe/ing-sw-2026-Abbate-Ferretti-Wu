package it.polimi.ingsw.gc49.datapacket;

import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

import java.io.Serializable;

public abstract class Datapacket implements Serializable {
    private String senderNickname;
    private static final long serialVersionUID = 1L;

    public enum DatapacketType { INITIALIZE_MODEL, UPDATE_MODEL, ERROR, CHANGE_PHASE,
        COMMAND, DISCONNECT, RECONNECT,
        INITIALIZE_HALL, UPDATE_HALL,
        HALL_COMMAND,
        INITIALIZE_ROOM, UPDATE_ROOM,
        ROOM_COMMAND, HEARTBEAT }

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
