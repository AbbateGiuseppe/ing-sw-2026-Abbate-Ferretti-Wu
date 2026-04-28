package it.polimi.ingsw.gc49.datapacket.sentFromServer.ROOM_RETURN;

import it.polimi.ingsw.gc49.datapacket.Datapacket;

public abstract class ClientRoomPacket extends Datapacket {
    public enum ClientRoomType { INITIALIZE, UPDATE }

    public final ClientRoomType commandType;

    public ClientRoomPacket ( ClientRoomType commandType ) {
        super(DatapacketType.ROOM_RETURN);
        this.commandType = commandType;
    }
}
