package it.polimi.ingsw.gc49.rmi_socket.server.connectors.inGame;

import it.polimi.ingsw.gc49.rmi_socket.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ProxyPlayer;

public abstract class ProxyPlayerGame extends ProxyPlayer implements VirtualClient {

    public ProxyPlayerGame ( ServerMultiplexer server, String nickname ) {
        super( server, nickname );
    }
}
