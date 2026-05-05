package it.polimi.ingsw.gc49.datapacket.HEARTBEAT;

import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public class HeartbeatPacket extends Datapacket {
    public HeartbeatPacket() {
        super(DatapacketType.HEARTBEAT, ApplicationPhase.ANY);
    }
}
