package it.polimi.ingsw.gc49.rmi_socket.server.proxies;

import it.polimi.ingsw.gc49.controller.MassiWuController;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualServerAdapter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class PhasedProxyPlayer implements VirtualClient, VirtualServer {
    public final ServerMultiplexer server;
    public final String nickname;
    protected ApplicationPhase currentPhase;
    protected VirtualServerAdapter serverSide;
    protected MassiWuController controller;
    protected VirtualClient clientSide;
    protected ObjectInputStream input;
    protected ObjectOutputStream output;
    protected volatile boolean running;
    //Handling TIMEOUTS
    protected long lastCheckIn = System.currentTimeMillis();
    protected ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    protected static final int SEND_INTERVAL = 5; // secondi
    protected static final int TIMEOUT_LIMIT = 15; // secondi


    public PhasedProxyPlayer ( ServerMultiplexer server, String nickname,
                               ApplicationPhase startingPhase,
                               VirtualServerAdapter serverSide, VirtualClient clientSide,
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

    protected void changeLocalPhase ( ApplicationPhase newPhase ) {
        currentPhase = newPhase;
    }
    public void setServerSideObject ( VirtualServerAdapter serverSide ) {
        this.serverSide = serverSide;
    }
    public void setController ( MassiWuController controller ){
        this.controller = controller;
    }

    protected void addSenderNickname ( Datapacket datapacket ) {
        datapacket.setSenderNickname(nickname);
    }
    protected boolean assureRightPhase ( Datapacket datapacket ) {
        if(datapacket.applicationPhase == ApplicationPhase.ANY){
            return true;
        }
        return datapacket.applicationPhase == currentPhase;
    }

    public void startNetworkHealthChecks() {
        // sending heartbeat for each interval
        scheduler.scheduleAtFixedRate(() -> {
            try { ping(); } catch (Exception e) {
                try {
                    onConnectionLost();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }, 0, SEND_INTERVAL, TimeUnit.SECONDS);

        // check if client is silent
        scheduler.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis() - lastCheckIn > TIMEOUT_LIMIT * 1000) {
                try {
                    onConnectionLost();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 5, 5, TimeUnit.SECONDS);
    }

    public void reportActivity() {
        this.lastCheckIn = System.currentTimeMillis();
    }

    public abstract void ping() throws Exception;


    // methods for reconnection
    public boolean isRunning() {
        return running;
    }

    private ApplicationPhase getCurrentPhase() {
        return currentPhase;
    }

    public void onConnectionLost() throws Exception {
        if (this.getCurrentPhase() != ApplicationPhase.GAME) {
            fullDisconnect();
        } else {
            handleDisconnection();
        }
    }

    public void fullDisconnect() throws Exception {
        this.running = false;
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
        this.server.fullDisconnect(this);
    }

    protected synchronized void handleDisconnection() {
        if (!running) return;
        running = false; // Il proxy rimane in memoria ma segnato come non attivo
        if (scheduler != null) scheduler.shutdownNow();
    }

    public void refreshClientState() {
        try {
            this.serverSide.syncPlayer(this);
            System.out.println("[RECONNECT] Sync completed for " + nickname);
        } catch (Exception e) {
            System.err.println("[RECONNECT] Error during the refresh of " + nickname + ": " + e.getMessage());
        }
    }

    public String getNickname(){
        return nickname;
    }

    public VirtualServerAdapter getServerSide(){
        return serverSide;
    }

    @Override
    public void receiveHeartbeat() throws RemoteException {
        this.reportActivity(); // Reset timestamp
    }

    @Override
    public void disconnect( DisconnectPacket disconnectPacket) throws Exception {
        this.fullDisconnect();
    }
}
