package it.polimi.ingsw.gc49.datapacket.sentFromServer.HALL_RETURN;

public class ClientHallUpdatePacket extends ClientHallPacket {
    public ClientHallUpdatePacket() {
        super(ClientHallType.UPDATE);
    }
}
