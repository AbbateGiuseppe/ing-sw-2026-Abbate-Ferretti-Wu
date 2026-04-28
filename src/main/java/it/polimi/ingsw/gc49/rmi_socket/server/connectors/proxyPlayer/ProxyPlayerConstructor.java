package it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer;

import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualServer;

public abstract class ProxyPlayerConstructor extends ProxyPlayer {
    public ProxyPlayerConstructor ( ProxyPlayer proxyPlayer, SubclassType subclassType ) {
        super( proxyPlayer.connectionType, subclassType, proxyPlayer.server, proxyPlayer.nickname );
    }
}
