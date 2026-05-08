package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.HallCommandPacket;

public class HallCreatePacket extends HallCommandPacket {
    public final String roomName;
    public final int maxNumOfPlayers;

    public HallCreatePacket ( String roomName, int maxNumOfPlayers ) {
        super(HallCommandType.CREATE);
        this.maxNumOfPlayers = maxNumOfPlayers;
        this.roomName = roomName;
    }
}
