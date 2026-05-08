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

public abstract class PhasedProxyServer implements VirtualClient, VirtualServer, Heartbeatable {
    public final VirtualClient clientSide;
    protected VirtualServer serverSide;
    protected ObjectInputStream input;
    protected ObjectOutputStream output;
    protected volatile boolean running;
    //handling Timeout
    protected long lastServerContact = System.currentTimeMillis();
    protected ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    protected static final int SEND_INTERVAL = 5;
    protected static final int TIMEOUT_LIMIT = 15;


    public PhasedProxyServer ( VirtualClient clientSide ) {
        this.clientSide = clientSide;
    }

    /**
     * @throws SocketException, if it loses connection (for socket only), starts a disconnection event chain.
     */
    public void runVirtualServer() throws SocketException{
        startHeartbeating();
    }

    public abstract void finishInitialization ( VirtualServer serverSide, ObjectInputStream input, ObjectOutputStream output );

    ///-----------------------------------
    // HEARTBEAT
    @Override
    public void startHeartbeating() {
        // Task 1: Invia segnale al Server
        scheduler.scheduleAtFixedRate(() -> {
            try { sendHeartbeat(); } catch (Exception e) { handleServerOffline(); }
        }, 0, SEND_INTERVAL, TimeUnit.SECONDS);

        // Task 2: Controlla se il Server è muto
        scheduler.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis() - lastServerContact > TIMEOUT_LIMIT * 1000) {
                handleServerOffline();
            }
        }, 5, SEND_INTERVAL*2, TimeUnit.SECONDS);
    }

    @Override
    public void receiveHeartbeat() {
        this.lastServerContact = System.currentTimeMillis();
    }

    @Override
    public abstract void sendHeartbeat() throws Exception;

    public void handleServerOffline() {
        running = false;
        scheduler.shutdownNow();
        System.err.println("\n[ERRORE] Il server non risponde. Chiusura...");
        System.exit(1);
    }

}
