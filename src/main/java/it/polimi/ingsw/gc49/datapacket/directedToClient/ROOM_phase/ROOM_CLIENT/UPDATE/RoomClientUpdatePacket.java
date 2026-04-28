package it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.UPDATE;

import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.RoomClientPacket;

public class RoomClientUpdatePacket extends RoomClientPacket {
    public RoomClientUpdatePacket () {
        super(ClientRoomType.UPDATE);
    }
}
