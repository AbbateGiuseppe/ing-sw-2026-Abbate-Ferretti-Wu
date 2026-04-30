package it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND;

import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public abstract class HallCommandPacket extends Datapacket {
    public enum HallCommandType { JOIN, CREATE }

    public final HallCommandType commandType;

    public HallCommandPacket ( HallCommandType commandType ) {
        super(DatapacketType.HALL_COMMAND, ApplicationPhase.HALL);
        this.commandType = commandType;
    }
}
