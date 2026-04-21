package it.polimi.ingsw.gc49.rmi_socket.server.connectors;

import it.polimi.ingsw.gc49.controller.massi.MassiController;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;

public abstract class ConnectorServerSide {
    protected final int clientLocalIndex;
    protected final MassiController controller;
    protected final ServerMultiplexer server;
    protected final String nickname;

    public ConnectorServerSide ( int clientLocalIndex, MassiController controller,
                                 ServerMultiplexer server, String nickname ) {
        this.clientLocalIndex = clientLocalIndex;
        this.controller = controller;
        this.server = server;
        this.nickname = nickname;
    }

    public String getNickname () {
        return nickname;
    }
}
