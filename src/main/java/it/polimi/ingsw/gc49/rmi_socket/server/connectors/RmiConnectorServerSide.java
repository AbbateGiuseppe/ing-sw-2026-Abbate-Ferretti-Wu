package it.polimi.ingsw.gc49.rmi_socket.server.connectors;

import it.polimi.ingsw.gc49.controller.massi.MassiController;
import it.polimi.ingsw.gc49.datapacket.COMMAND.Command;
import it.polimi.ingsw.gc49.rmi_socket.client.VirtualClientRmi;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.VirtualServerRmi;

import java.rmi.RemoteException;

public class RmiConnectorServerSide extends ConnectorServerSide implements VirtualServerRmi {
    public RmiConnectorServerSide ( int clientLocalIndex, MassiController controller, ServerMultiplexer server ) {
        super( clientLocalIndex, controller, server );
    }

    @Override
    public void connect ( String nickname, VirtualClientRmi client ) throws RemoteException {

    }

    @Override
    public void sendCommand ( Command command ) throws RemoteException {

    }
}
