package it.polimi.ingsw.gc49.rmi_socket.server.connectors;

import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.ConnectionProxy;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualServer;

public abstract class ProxyPlayer extends ConnectionProxy {
    public final ServerMultiplexer server;
    public final String nickname;

    public ProxyPlayer ( ConnectionType connectionType, SubclassType subclassType,
                         ServerMultiplexer server, String nickname ) {
        super(connectionType, subclassType);
        this.server = server;
        this.nickname = nickname;
    }

    public abstract ProxyPlayer changeSubclass ( SubclassType newSubclass, VirtualServer newServerSide );

    public void addSenderNickname ( Datapacket datapacket ) {
        datapacket.setSenderNickname(nickname);
    }
}
