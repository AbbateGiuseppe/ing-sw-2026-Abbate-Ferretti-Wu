package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.RoomCommandPacket;

public class RoomLeavePacket extends RoomCommandPacket {

    public RoomLeavePacket () {
        super(RoomCommandType.LEAVE);
    }
}
