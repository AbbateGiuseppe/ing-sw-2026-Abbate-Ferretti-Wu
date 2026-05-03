package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters;

import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

public abstract class VirtualServerAdapter implements VirtualServer {
    @Override
    public void receiveHeartbeat() throws Exception {
    }
}
