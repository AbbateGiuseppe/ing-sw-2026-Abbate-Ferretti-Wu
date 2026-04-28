package it.polimi.ingsw.gc49.datapacket.sentFromClient.ROOM_COMMAND;

import it.polimi.ingsw.gc49.datapacket.Datapacket;

public abstract class RoomCommandPacket extends Datapacket {
    public enum RoomCommandType { LEAVE }

    public final RoomCommandType commandType;

    public RoomCommandPacket ( RoomCommandType commandType ) {
        super(DatapacketType.ROOM_COMMAND);
        this.commandType = commandType;
    }
}
