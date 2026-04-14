package it.polimi.ingsw.gc49;

import it.polimi.ingsw.gc49.datapacket.COMMAND.Command;

public interface VirtualServer {
    void sendCommand ( Command command ) throws Exception;
}
