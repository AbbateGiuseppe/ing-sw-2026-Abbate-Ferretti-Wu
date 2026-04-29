package it.polimi.ingsw.gc49.rmi_socket.server.connectors.newest;

import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

public abstract class PhasedProxyPlayer implements VirtualClient, VirtualServer {
    public final ServerMultiplexer server;
    public final String nickname;
    protected ApplicationPhase currentPhase;
    protected final VirtualServer serverSide;
    protected final VirtualClient clientSide;
    protected final ObjectInputStream input;
    protected final ObjectOutputStream output;
    protected volatile boolean running;


    public PhasedProxyPlayer ( ServerMultiplexer server, String nickname,
                               ApplicationPhase startingPhase,
                               VirtualServer serverSide, VirtualClient clientSide,
                               ObjectInputStream input, ObjectOutputStream output ) {
        this.server = server;
        this.nickname = nickname;
        this.currentPhase = startingPhase;
        this.serverSide = serverSide;
        this.clientSide = clientSide;
        this.input = input;
        this.output = output;
    }

    /**
     * For socket only
     * @throws SocketException, if it loses connection.
     */
    public void runVirtualClient() throws SocketException{}

    public void changeLocalPhase ( ApplicationPhase newPhase ) {
        currentPhase = newPhase;
    }

    protected void addSenderNickname ( Datapacket datapacket ) {
        datapacket.setSenderNickname(nickname);
    }
    protected boolean assureRightPhase ( Datapacket datapacket ) {
        return datapacket.applicationPhase == currentPhase;
    }
}
