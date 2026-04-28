package it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN;

import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.HallCommandPacket;

public class HallJoinPacket extends HallCommandPacket {
    public final int roomId;

    public HallJoinPacket ( int roomId ) {
        super(HallCommandType.JOIN);
        this.roomId = roomId;
    }
}
