package it.polimi.ingsw.gc49.datapacket.sentFromServer.ROOM_RETURN;

public class ClientRoomInitializePacket extends ClientRoomPacket {
    public ClientRoomInitializePacket() {
        super(ClientRoomType.INITIALIZE);
    }
}
