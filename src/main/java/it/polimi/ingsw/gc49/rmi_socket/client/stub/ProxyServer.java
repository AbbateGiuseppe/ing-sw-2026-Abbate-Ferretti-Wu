package it.polimi.ingsw.gc49.rmi_socket.client.stub;

import it.polimi.ingsw.gc49.rmi_socket.ConnectionProxy;

public abstract class ProxyServer extends ConnectionProxy {
    public ProxyServer ( ConnectionType connectionType, SubclassType subclassType ) {
        super(connectionType, subclassType);
    }
}
