package it.polimi.ingsw.gc49.rmi_socket.server.connectors.inRoom;

import it.polimi.ingsw.gc49.rmi_socket.VirtualRoomClient;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ProxyPlayer;

public abstract class ProxyPlayerRoom extends ProxyPlayer implements VirtualRoomClient {

    public ProxyPlayerRoom ( ServerMultiplexer server, String nickname ) {
        super( server, nickname );
    }
}
