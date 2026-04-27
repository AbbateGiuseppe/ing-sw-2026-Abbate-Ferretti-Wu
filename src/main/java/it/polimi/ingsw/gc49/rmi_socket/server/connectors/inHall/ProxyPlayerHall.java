package it.polimi.ingsw.gc49.rmi_socket.server.connectors.inHall;

import it.polimi.ingsw.gc49.rmi_socket.VirtualHallClient;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ProxyPlayer;

public abstract class ProxyPlayerHall extends ProxyPlayer implements VirtualHallClient {

    public ProxyPlayerHall ( ServerMultiplexer server, String nickname ) {
        super( server, nickname );
    }
}
