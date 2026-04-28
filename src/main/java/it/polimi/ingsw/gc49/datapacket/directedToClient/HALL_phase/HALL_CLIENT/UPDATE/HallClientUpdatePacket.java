package it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.UPDATE;

import it.polimi.ingsw.gc49.datapacket.directedToClient.HALL_phase.HALL_CLIENT.HallClientPacket;

public class HallClientUpdatePacket extends HallClientPacket {
    public HallClientUpdatePacket () {
        super(ClientHallType.UPDATE);
    }
}
