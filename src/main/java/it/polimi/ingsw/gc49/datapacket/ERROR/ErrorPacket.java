package it.polimi.ingsw.gc49.datapacket.ERROR;

import it.polimi.ingsw.gc49.datapacket.Datapacket;

public class ErrorPacket extends Datapacket {
    public final Exception e;

    public ErrorPacket ( Exception e ) {
        super(DatapacketType.ERROR);
        this.e = e;
    }
}
