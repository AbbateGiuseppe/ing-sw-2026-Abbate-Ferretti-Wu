package it.polimi.ingsw.gc49.rmi_socket.server.connectors;

import it.polimi.ingsw.gc49.rmi_socket.ConnectionProxy;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualServer;

public class ReferencedProxyPlayer {
    private ProxyPlayer proxy;

    public ReferencedProxyPlayer ( ProxyPlayer proxyPlayer ) {
        this.proxy = proxyPlayer;
    }


    public ProxyPlayer getProxy() {
        return proxy;
    }

    public void changeSubclass ( ConnectionProxy.SubclassType newSubclass, VirtualServer newServerSide ){
        this.proxy = proxy.changeSubclass(newSubclass, newServerSide);
    }
}
