package it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN;

import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.HallCommandPacket;

public class HallJoinPacket extends HallCommandPacket {
    public final String roomName;

    public HallJoinPacket ( String roomName ) {
        super(HallCommandType.JOIN);
        this.roomName = roomName;
    }
}
