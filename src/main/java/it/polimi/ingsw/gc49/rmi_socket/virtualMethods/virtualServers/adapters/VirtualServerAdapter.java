package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters;

import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

public abstract class VirtualServerAdapter implements VirtualServer {
    public abstract void syncPlayer ( PhasedProxyPlayer proxy ) throws Exception;
}
