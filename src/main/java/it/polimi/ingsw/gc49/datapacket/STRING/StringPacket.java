package it.polimi.ingsw.gc49.datapacket.STRING;

import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public class StringPacket extends Datapacket {
    public final String string;

    public StringPacket ( String string ) {
        super(DatapacketType.STRING, ApplicationPhase.ANY);
        this.string = string;
    }
}
