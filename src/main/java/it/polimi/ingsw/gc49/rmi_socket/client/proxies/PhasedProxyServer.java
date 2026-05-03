package it.polimi.ingsw.gc49.rmi_socket.client.proxies;

import it.polimi.ingsw.gc49.rmi_socket.client.ClientApplication;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class PhasedProxyServer implements VirtualClient, VirtualServer {
    public final VirtualClient clientSide;
    protected ApplicationPhase currentPhase;
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
     * For socket only
     * @throws SocketException, if it loses connection.
     */
    public void runVirtualServer() throws SocketException{}

    private void changeLocalPhase ( ApplicationPhase newPhase ) {
        currentPhase = newPhase;
    }
    public abstract void finishInitialization ( VirtualServer serverSide, ObjectInputStream input, ObjectOutputStream output );


    //Implementation heartbeat
    public void startHeartbeat() {
        // Task 1: Invia segnale al Server
        scheduler.scheduleAtFixedRate(() -> {
            try { pingServer(); } catch (Exception e) { handleServerOffline(); }
        }, 0, SEND_INTERVAL, TimeUnit.SECONDS);

        // Task 2: Controlla se il Server è muto
        scheduler.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis() - lastServerContact > TIMEOUT_LIMIT * 1000) {
                handleServerOffline();
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    public void reportActivity() {
        this.lastServerContact = System.currentTimeMillis();
    }

    protected abstract void pingServer() throws Exception;

    protected void handleServerOffline() {
        running = false;
        scheduler.shutdownNow();
        System.err.println("\n[ERRORE] Il server non risponde. Chiusura...");
        System.exit(1);
    }

    @Override
    public void receiveHeartbeat() throws RemoteException {
        reportActivity();
    }


}
