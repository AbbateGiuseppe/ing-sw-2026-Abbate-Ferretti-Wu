package it.polimi.ingsw.gc49.datapacket.HALL_COMMAND;

import it.polimi.ingsw.gc49.datapacket.Datapacket;

public class HallCommandPacket extends Datapacket {

    public HallCommandPacket () {
        super(DatapacketType.HALL_COMMAND);
    }
}
