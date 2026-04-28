package it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.ERROR;

import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public class ErrorPacket extends Datapacket {
    public final Exception e;

    public ErrorPacket ( Exception e ) {
        super(DatapacketType.ERROR, ApplicationPhase.ANY);
        this.e = e;
    }
}
