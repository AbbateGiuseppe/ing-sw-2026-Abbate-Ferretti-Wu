package it.polimi.ingsw.gc49.datapacket.sentFromServer.HALL_RETURN;

public class ClientHallInitializePacket extends ClientHallPacket {
    public ClientHallInitializePacket() {
        super(ClientHallType.INITIALIZE);
    }
}
