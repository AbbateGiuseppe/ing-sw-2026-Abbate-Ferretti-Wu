package it.polimi.ingsw.gc49.rmi_socket;

import it.polimi.ingsw.gc49.datapacket.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.RECONNECT.ReconnectPacket;

import java.rmi.Remote;

public interface VirtualServer extends Remote {
    void sendCommand ( CommandPacket commandPacket ) throws Exception;

    void disconnect ( DisconnectPacket disconnectPacket ) throws Exception;

    void reconnect ( ReconnectPacket reconnectPacket ) throws Exception;
}
