package it.polimi.ingsw.gc49.rmi_socket.server.connectors;

import it.polimi.ingsw.gc49.controller.massi.MassiController;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;

public abstract class ConnectorServerSide {
    protected final int clientLocalIndex;
    protected final MassiController controller;
    protected final ServerMultiplexer server;

    public ConnectorServerSide ( int clientLocalIndex, MassiController controller, ServerMultiplexer server ) {
        this.clientLocalIndex = clientLocalIndex;
        this.controller = controller;
        this.server = server;
    }
}
