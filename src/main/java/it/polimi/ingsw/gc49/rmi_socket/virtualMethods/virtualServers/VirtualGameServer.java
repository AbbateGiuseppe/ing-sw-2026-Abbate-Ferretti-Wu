package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers;

import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.uncertain.RECONNECT.ReconnectPacket;

import java.rmi.Remote;

public interface VirtualGameServer extends Remote {
    void sendCommand ( CommandPacket commandPacket ) throws Exception;

    void disconnect ( DisconnectPacket disconnectPacket ) throws Exception;

    void reconnect ( ReconnectPacket reconnectPacket ) throws Exception;
}
