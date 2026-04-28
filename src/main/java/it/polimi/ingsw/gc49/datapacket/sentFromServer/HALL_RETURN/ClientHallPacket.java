package it.polimi.ingsw.gc49.datapacket.sentFromServer.HALL_RETURN;

import it.polimi.ingsw.gc49.datapacket.Datapacket;

public abstract class ClientHallPacket extends Datapacket {
    public enum ClientHallType { INITIALIZE, UPDATE }

    public final ClientHallType commandType;

    public ClientHallPacket ( ClientHallType commandType ) {
        super(DatapacketType.HALL_RETURN);
        this.commandType = commandType;
    }
}
