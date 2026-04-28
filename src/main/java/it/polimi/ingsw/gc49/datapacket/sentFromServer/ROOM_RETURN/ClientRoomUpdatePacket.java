package it.polimi.ingsw.gc49.datapacket.sentFromServer.ROOM_RETURN;

public class ClientRoomUpdatePacket extends ClientRoomPacket {
    public ClientRoomUpdatePacket() {
        super(ClientRoomType.UPDATE);
    }
}
