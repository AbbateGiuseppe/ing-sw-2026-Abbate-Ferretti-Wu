package it.polimi.ingsw.gc49.datapacket.sentFromClient.RECONNECT;

import it.polimi.ingsw.gc49.datapacket.Datapacket;

public class ReconnectPacket extends Datapacket {
    public final String nickname;

    public ReconnectPacket ( String nickname ) {
        super(DatapacketType.RECONNECT);
        this.nickname = nickname;
    }
}
