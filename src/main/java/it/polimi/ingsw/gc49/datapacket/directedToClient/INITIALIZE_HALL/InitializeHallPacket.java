package it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_HALL;

import it.polimi.ingsw.gc49.View.mockupHall.MockupHall;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public class InitializeHallPacket extends Datapacket {
    public final MockupHall mockupHall;

    public InitializeHallPacket ( MockupHall mockupHall ) {
        super(DatapacketType.INITIALIZE_HALL, ApplicationPhase.HALL);
        this.mockupHall = mockupHall;
    }
}
