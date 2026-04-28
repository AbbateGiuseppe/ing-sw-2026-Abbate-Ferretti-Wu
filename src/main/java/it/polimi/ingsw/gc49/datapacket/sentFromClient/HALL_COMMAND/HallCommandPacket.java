package it.polimi.ingsw.gc49.datapacket.sentFromClient.HALL_COMMAND;

import it.polimi.ingsw.gc49.datapacket.Datapacket;

public abstract class HallCommandPacket extends Datapacket {
    public enum HallCommandType { JOIN, CREATE }

    public final HallCommandType commandType;

    public HallCommandPacket ( HallCommandType commandType ) {
        super(DatapacketType.HALL_COMMAND);
        this.commandType = commandType;
    }
}
