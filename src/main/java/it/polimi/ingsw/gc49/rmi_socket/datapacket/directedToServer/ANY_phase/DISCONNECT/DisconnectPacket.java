package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public class DisconnectPacket extends Datapacket {

    public DisconnectPacket () {
        super(DatapacketType.DISCONNECT, ApplicationPhase.ANY);
    }
}
