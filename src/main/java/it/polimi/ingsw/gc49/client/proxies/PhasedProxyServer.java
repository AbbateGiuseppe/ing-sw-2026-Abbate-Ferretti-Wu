package it.polimi.ingsw.gc49.client.proxies;

import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Heartbeatable;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * The {@code PhasedProxyServer} abstract class acts as the client-side representative (Proxy)
 * for the remote server. It bridges the local {@link ClientApplication} with the network,
 * abstracting away the underlying communication protocol (RMI or Socket).
 * It continuously monitors the connection health via a heartbeat system and triggers
 * emergency procedures if the server goes offline.
 */
public abstract class PhasedProxyServer implements VirtualClient, VirtualServer, Heartbeatable {

    /** The local client application that processes game logic and UI updates. */
    public final VirtualClient clientSide;

    /** The remote server reference (used primarily in RMI implementations). */
    protected VirtualServer serverSide;

    /** The input stream used to read data from the server (used in Socket implementations). */
    protected ObjectInputStream input;

    /** The output stream used to send data to the server (used in Socket implementations). */
    protected ObjectOutputStream output;

    /** Flag indicating whether the proxy is currently running and actively listening. */
    protected volatile boolean running;


    //handling Timeout
    /** Timestamp of the last received heartbeat or activity from the server. */
    protected long lastServerContact = System.currentTimeMillis();

    /** Scheduler used to handle periodic heartbeat pinging and timeout checking tasks. */
    protected ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    /** The interval (in seconds) at which heartbeats are sent to the server. */
    protected static final int SEND_INTERVAL = 5;

    /** The maximum time (in seconds) allowed without a server response before declaring it offline. */
    protected static final int TIMEOUT_LIMIT = 15;


    /**
     * Constructs a new {@code PhasedProxyServer}.
     *
     * @param clientSide the local {@link it.polimi.ingsw.gc49.client.ClientApplication} instance handling game views and inputs.
     */
    public PhasedProxyServer ( VirtualClient clientSide ) {
        this.clientSide = clientSide;
    }

    /**
     * Starts the virtual server background processes, primarily initiating the heartbeat monitor.
     * Implementing subclasses (like Socket) usually override this to also start the listening loop.
     *
     * @throws SocketException if an immediate connection loss is detected (for socket only),
     * triggering a disconnection event chain.
     */
    public void runVirtualServer() throws SocketException{
        startHeartbeating();
    }

    /**
     * Finalizes the initialization of the proxy with the specific network implementation details.
     *
     * @param serverSide the remote server RMI stub (null if using Sockets).
     * @param input      the Socket input stream (null if using RMI).
     * @param output     the Socket output stream (null if using RMI).
     */

    public abstract void finishInitialization ( VirtualServer serverSide, ObjectInputStream input, ObjectOutputStream output );

    // ============================================================
    // HEARTBEAT
    // ============================================================

    /**
     * Starts the asynchronous heartbeat system on the client side.
     * It spins up two scheduled tasks: one to periodically ping the server,
     * and another to monitor if the server has been silent for too long.
     */
    @Override
    public void startHeartbeating() {
        if (this.scheduler == null || this.scheduler.isShutdown()) {
            this.scheduler = Executors.newScheduledThreadPool(1);
        }
        // Task 1: SEND SIGNAL TO SERVER
        scheduler.scheduleAtFixedRate(() -> {
            try { sendHeartbeat(); } catch (Exception e) { handleServerOffline(); }
        }, 0, SEND_INTERVAL, TimeUnit.SECONDS);

        // Task 2: CHECK IF SERVER'S MUTE
        scheduler.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis() - lastServerContact > TIMEOUT_LIMIT * 1000) {
                handleServerOffline();
            }
        }, 5, SEND_INTERVAL*2, TimeUnit.SECONDS);
    }

    /**
     * Registers a received heartbeat ping from the server by updating the last contact timestamp.
     */
    @Override
    public void receiveHeartbeat() {
        this.lastServerContact = System.currentTimeMillis();
    }


    /**
     * Sends a ping to the server to confirm this client is still active.
     * The actual implementation varies depending on the network protocol (Socket or RMI).
     *
     * @throws Exception if the ping fails to send due to network issues.
     */
    @Override
    public abstract void sendHeartbeat() throws Exception;


    /**
     * Handles the catastrophic event of losing connection to the server.
     * It halts the running loops, shuts down the heartbeat scheduler, prints an error,
     * and forcibly terminates the client application.
     */
    public void handleServerOffline() {
        running = false;
        scheduler.shutdownNow();
        System.err.println("\n[ERRORE] Il server non risponde. Chiusura...");
        System.exit(1);
    }

}
