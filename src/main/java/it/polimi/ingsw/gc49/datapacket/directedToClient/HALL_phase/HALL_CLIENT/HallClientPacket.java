package it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT;

import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public abstract class HallClientPacket extends Datapacket {
    public enum ClientHallType { INITIALIZE, UPDATE }

    public final ClientHallType commandType;

    public HallClientPacket ( ClientHallType commandType ) {
        super(DatapacketType.HALL_CLIENT, ApplicationPhase.HALL);
        this.commandType = commandType;
    }
}
