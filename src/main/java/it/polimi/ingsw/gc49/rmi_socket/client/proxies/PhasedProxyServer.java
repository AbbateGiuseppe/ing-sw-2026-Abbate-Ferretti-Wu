package it.polimi.ingsw.gc49.rmi_socket.client.proxies;

import it.polimi.ingsw.gc49.rmi_socket.client.ClientApplication;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

public abstract class PhasedProxyServer implements VirtualClient, VirtualServer {
    public final VirtualClient clientSide;
    protected ApplicationPhase currentPhase;
    protected VirtualServer serverSide;
    protected ObjectInputStream input;
    protected ObjectOutputStream output;
    protected volatile boolean running;


    public PhasedProxyServer ( VirtualClient clientSide ) {
        this.clientSide = clientSide;
    }

    /**
     * For socket only
     * @throws SocketException, if it loses connection.
     */
    public void runVirtualServer() throws SocketException{}

    private void changeLocalPhase ( ApplicationPhase newPhase ) {
        currentPhase = newPhase;
    }
    public abstract void finishInitialization ( VirtualServer serverSide, ObjectInputStream input, ObjectOutputStream output );
}
