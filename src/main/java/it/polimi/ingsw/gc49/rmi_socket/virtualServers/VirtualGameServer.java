package it.polimi.ingsw.gc49.rmi_socket.virtualServers;

import it.polimi.ingsw.gc49.datapacket.sentFromClient.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.RECONNECT.ReconnectPacket;

public interface VirtualGameServer {
    void sendCommand ( CommandPacket commandPacket ) throws Exception;

    void disconnect ( DisconnectPacket disconnectPacket ) throws Exception;

    void reconnect ( ReconnectPacket reconnectPacket ) throws Exception;
}
