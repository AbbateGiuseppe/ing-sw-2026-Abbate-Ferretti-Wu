package it.polimi.ingsw.gc49.datapacket.sentFromClient.HALL_COMMAND;

public class HallJoinPacket extends HallCommandPacket {
    public final int roomId;

    public HallJoinPacket ( int roomId ) {
        super(HallCommandType.JOIN);
        this.roomId = roomId;
    }
}
