package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_ROOM;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public class InitializeRoomPacket extends Datapacket {
    public final MockupRoom mockupRoom;

    public InitializeRoomPacket ( MockupRoom mockupRoom ) {
        super(DatapacketType.INITIALIZE_ROOM, ApplicationPhase.ROOM);
        this.mockupRoom = mockupRoom;
    }
}
