package it.polimi.ingsw.gc49.datapacket.sentFromClient.ROOM_COMMAND;

public class RoomLeavePacket extends RoomCommandPacket {

    public RoomLeavePacket () {
        super(RoomCommandType.LEAVE);
    }
}
