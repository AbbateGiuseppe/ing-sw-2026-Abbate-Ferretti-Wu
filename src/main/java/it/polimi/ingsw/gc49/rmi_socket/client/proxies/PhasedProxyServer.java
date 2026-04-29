package it.polimi.ingsw.gc49.rmi_socket.client.proxies;

import it.polimi.ingsw.gc49.rmi_socket.client.ClientApplication;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.adapters.VirtualClientAdapter;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

public abstract class PhasedProxyServer implements VirtualClient, VirtualServer {
    public final ClientApplication client;
    protected ApplicationPhase currentPhase;
    protected VirtualClientAdapter clientSide;
    protected final VirtualServer serverSide;
    protected final ObjectInputStream input;
    protected final ObjectOutputStream output;
    protected volatile boolean running;


    public PhasedProxyServer ( ClientApplication client,
                               ApplicationPhase startingPhase,
                               VirtualClientAdapter clientSide, VirtualServer serverSide,
                               ObjectInputStream input, ObjectOutputStream output ) {
        this.client = client;
        this.currentPhase = startingPhase;
        this.clientSide = clientSide;
        this.serverSide = serverSide;
        this.input = input;
        this.output = output;
    }

    /**
     * For socket only
     * @throws SocketException, if it loses connection.
     */
    public void runVirtualServer() throws SocketException{}

    private void changeLocalPhase ( ApplicationPhase newPhase ) {
        currentPhase = newPhase;
    }
    public void setClientSideObject ( VirtualClientAdapter clientSide ) {
        this.clientSide = clientSide;
    }
}
