package it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.CHANGE_PHASE;

import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public class ChangePhasePacket extends Datapacket {
    public final ApplicationPhase newPhase;

    public ChangePhasePacket ( ApplicationPhase newPhase ) {
        super(DatapacketType.CHANGE_PHASE, ApplicationPhase.ANY);
        this.newPhase = newPhase;
    }
}
