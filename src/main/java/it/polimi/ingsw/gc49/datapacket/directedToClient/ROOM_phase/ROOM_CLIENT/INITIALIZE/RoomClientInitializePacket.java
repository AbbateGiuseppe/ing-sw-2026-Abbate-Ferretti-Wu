package it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.INITIALIZE;

import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.RoomClientPacket;

public class RoomClientInitializePacket extends RoomClientPacket {
    public RoomClientInitializePacket () {
        super(ClientRoomType.INITIALIZE);
    }
}
