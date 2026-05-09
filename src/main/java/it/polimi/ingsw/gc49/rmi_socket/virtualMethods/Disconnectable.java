package it.polimi.ingsw.gc49.rmi_socket.virtualMethods;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;

import java.rmi.Remote;

public interface Disconnectable extends Remote {
    void disconnect ( DisconnectPacket disconnectPacket ) throws Exception;

}
