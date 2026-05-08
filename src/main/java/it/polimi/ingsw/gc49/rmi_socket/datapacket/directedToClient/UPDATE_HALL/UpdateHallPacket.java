package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_HALL;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupHall;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public class UpdateHallPacket extends Datapacket {
    public final MockupHall newMockupHall;

    //TODO: make the updateHallPacket dynamic like updateModelPacket.

    public UpdateHallPacket ( MockupHall newMockupHall ) {
        super(DatapacketType.UPDATE_HALL, ApplicationPhase.HALL);
        this.newMockupHall = newMockupHall;
    }
}
