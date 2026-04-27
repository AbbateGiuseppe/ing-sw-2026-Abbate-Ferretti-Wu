package it.polimi.ingsw.gc49.rmi_socket.server.connectors;

import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;

public abstract class ProxyPlayer {
    protected final ServerMultiplexer server;
    protected final String nickname;

    public ProxyPlayer ( ServerMultiplexer server, String nickname ) {
        this.server = server;
        this.nickname = nickname;
    }
}
