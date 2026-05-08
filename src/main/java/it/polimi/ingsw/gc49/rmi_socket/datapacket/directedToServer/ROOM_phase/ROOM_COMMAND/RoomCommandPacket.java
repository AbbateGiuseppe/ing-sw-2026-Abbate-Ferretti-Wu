package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public abstract class RoomCommandPacket extends Datapacket {
    public enum RoomCommandType { LEAVE }

    public final RoomCommandType commandType;

    public RoomCommandPacket ( RoomCommandType commandType ) {
        super(DatapacketType.ROOM_COMMAND, ApplicationPhase.ROOM);
        this.commandType = commandType;
    }
}
