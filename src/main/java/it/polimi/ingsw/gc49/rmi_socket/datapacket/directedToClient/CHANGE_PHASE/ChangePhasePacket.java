package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public class ChangePhasePacket extends Datapacket {
    public final ApplicationPhase newPhase;

    public ChangePhasePacket ( ApplicationPhase newPhase ) {
        super(DatapacketType.CHANGE_PHASE, ApplicationPhase.ANY);
        this.newPhase = newPhase;
    }
}
