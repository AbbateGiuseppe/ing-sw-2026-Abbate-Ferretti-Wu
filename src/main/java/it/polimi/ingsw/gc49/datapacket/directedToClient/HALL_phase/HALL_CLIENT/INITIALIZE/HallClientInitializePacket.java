package it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.INITIALIZE;

import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.HallClientPacket;

public class HallClientInitializePacket extends HallClientPacket {
    public HallClientInitializePacket () {
        super(ClientHallType.INITIALIZE);
    }
}
