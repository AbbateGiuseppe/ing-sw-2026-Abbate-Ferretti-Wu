package it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT;

import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public abstract class RoomClientPacket extends Datapacket {
    public enum ClientRoomType { INITIALIZE, UPDATE }

    public final ClientRoomType commandType;

    public RoomClientPacket ( ClientRoomType commandType ) {
        super(DatapacketType.ROOM_CLIENT, ApplicationPhase.ROOM);
        this.commandType = commandType;
    }
}
