package it.polimi.ingsw.gc49.rmi_socket.virtualMethods;

import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;

public interface Disconnectable {
    void disconnect ( DisconnectPacket disconnectPacket ) throws Exception;

}
