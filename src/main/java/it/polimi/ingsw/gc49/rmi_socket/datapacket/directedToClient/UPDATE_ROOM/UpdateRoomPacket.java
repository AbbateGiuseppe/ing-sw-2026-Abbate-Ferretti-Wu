package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_ROOM;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public class UpdateRoomPacket extends Datapacket {
    public final MockupRoom newMockupRoom;

    //TODO: make the updateRoomPacket dynamic like updateModelPacket.

    public UpdateRoomPacket ( MockupRoom newMockupRoom ) {
        super(DatapacketType.UPDATE_ROOM, ApplicationPhase.ROOM);
        this.newMockupRoom = newMockupRoom;
    }
}
