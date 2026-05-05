package it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT;

import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

public class DisconnectPacket extends Datapacket {

    public DisconnectPacket () {
        super(DatapacketType.DISCONNECT, ApplicationPhase.GAME);
    }
}
