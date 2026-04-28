package it.polimi.ingsw.gc49.datapacket.sentFromClient.DISCONNECT;

import it.polimi.ingsw.gc49.datapacket.Datapacket;

public class DisconnectPacket extends Datapacket {
    public final String nickname;

    public DisconnectPacket ( String nickname ) {
        super(DatapacketType.DISCONNECT);
        this.nickname = nickname;
    }
}
