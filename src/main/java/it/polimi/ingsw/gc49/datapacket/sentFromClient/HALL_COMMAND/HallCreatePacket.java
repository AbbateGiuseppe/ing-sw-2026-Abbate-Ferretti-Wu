package it.polimi.ingsw.gc49.datapacket.sentFromClient.HALL_COMMAND;

public class HallCreatePacket extends HallCommandPacket {
    public final int maxNumOfPlayers;

    public HallCreatePacket ( int maxNumOfPlayers ) {
        super(HallCommandType.CREATE);
        this.maxNumOfPlayers = maxNumOfPlayers;
    }
}
