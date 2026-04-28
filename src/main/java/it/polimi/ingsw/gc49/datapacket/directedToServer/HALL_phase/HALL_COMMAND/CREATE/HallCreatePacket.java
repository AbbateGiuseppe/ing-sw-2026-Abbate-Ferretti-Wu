package it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE;

import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.HallCommandPacket;

public class HallCreatePacket extends HallCommandPacket {
    public final int maxNumOfPlayers;

    public HallCreatePacket ( int maxNumOfPlayers ) {
        super(HallCommandType.CREATE);
        this.maxNumOfPlayers = maxNumOfPlayers;
    }
}
