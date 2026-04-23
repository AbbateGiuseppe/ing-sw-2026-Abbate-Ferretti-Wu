package it.polimi.ingsw.gc49.rmi_socket.server.connectors;

import it.polimi.ingsw.gc49.controller.massi.MassiController;
import it.polimi.ingsw.gc49.datapacket.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.VirtualServer;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;

import java.rmi.RemoteException;

public class RmiConnectorServerSide extends ConnectorServerSide implements VirtualServer {
    public RmiConnectorServerSide ( int clientLocalIndex, MassiController controller,
                                    ServerMultiplexer server, String nickname ) {
        super( clientLocalIndex, controller, server, nickname );
    }

    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws RemoteException {
        controller.executeCommand(commandPacket);
    }

    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        //TODO: Giuseppe
    }

    @Override
    public void reconnect ( ReconnectPacket reconnectPacket ) throws Exception {
        //TODO: Giuseppe
    }
}
