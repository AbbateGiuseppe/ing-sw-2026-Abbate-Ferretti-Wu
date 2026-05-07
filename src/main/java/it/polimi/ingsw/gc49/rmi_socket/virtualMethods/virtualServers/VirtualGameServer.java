package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers;

import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Disconnectable;

import java.rmi.Remote;

public interface VirtualGameServer extends Remote, Disconnectable {
    void sendCommand ( CommandPacket commandPacket ) throws Exception;

}
