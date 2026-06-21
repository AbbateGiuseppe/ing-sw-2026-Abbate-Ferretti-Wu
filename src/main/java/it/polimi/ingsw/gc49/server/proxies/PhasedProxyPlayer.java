package it.polimi.ingsw.gc49.server.proxies;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualGameServerAdapter;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualHallServerAdapter;
import it.polimi.ingsw.gc49.server.controller.MassiWuPeppeController;
import it.polimi.ingsw.gc49.server.controller.MassiWuPeppeController;
import it.polimi.ingsw.gc49.server.controller.PlayerActionEnum;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Heartbeatable;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualServerAdapter;
import it.polimi.ingsw.gc49.server.rooms.PlayingRoom;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom.RoomType.PLAYING;

/**
 * The {@code PhasedProxyPlayer} abstract class acts as a server-side representative (Proxy)
 * for a connected client. It abstracts the underlying network technology (Socket or RMI)
 * and handles the player's session, including heartbeats, timeouts, and application phases
 * (e.g., HALL, ROOM, GAME).
 * It implements {@link Serializable} to allow the server to save the player's logical state,
 * marking network-specific or volatile fields as {@code transient}.
 */
public abstract class PhasedProxyPlayer implements VirtualClient, VirtualServer, Heartbeatable, Serializable {
    /** Reference to the main server multiplexer. */
    protected transient ServerMultiplexer server;

    /** The unique identifier for this player. */
    public final String nickname;

    /** The current state or location of the player within the application. */
    protected ApplicationPhase currentPhase;

    /** An adapter that routes incoming commands to the correct server entity (Hall, Room, or Game). */
    protected VirtualServerAdapter serverSide;

    /** The MVC controller assigned to this player during a game. */
    protected transient MassiWuPeppeController controller;

    /** The RMI stub used to communicate with the client (if using RMI). */
    protected transient VirtualClient clientSide;

    /** The input stream used to read data from the client (if using Socket). */
    protected transient ObjectInputStream input;

    /** The output stream used to send data to the client (if using Socket). */
    protected transient ObjectOutputStream output;

    /** Indicates whether the player is currently actively connected to the server. */
    protected volatile boolean connected;

    /** Timestamp of the last received heartbeat or activity from the client. */
    protected long lastCheckIn = System.currentTimeMillis();

    /** Scheduler used to run periodic heartbeat pinging and timeout checking tasks. */
    protected transient ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

    /** The interval (in seconds) at which heartbeats are evaluated. */
    protected static final int SEND_INTERVAL = 5;

    /** The maximum time (in seconds) allowed without a client response before dropping the connection. */
    protected static final int TIMEOUT_LIMIT = 15;

    /**
     * Constructs a new base proxy for a player.
     *
     * @param server       the main server multiplexer managing this connection.
     * @param nickname     the unique nickname of the player.
     * @param startingPhase the initial phase the player is put into (usually HALL).
     * @param serverSide   the adapter linking the proxy to the current server logic.
     * @param clientSide   the RMI client stub (null if using Socket).
     * @param input        the Socket input stream (null if using RMI).
     * @param output       the Socket output stream (null if using RMI).
     */
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
     * Initiates the virtual client's background tasks, primarily starting the heartbeat monitor.
     *
     * @throws SocketException if an immediate connection loss is detected (mainly for sockets),
     * triggering a disconnection chain.
     */
    public void runVirtualClient() throws SocketException{
        startHeartbeating();
    }

    /**
     * Updates the internal tracking phase of this proxy (e.g., moving from HALL to GAME).
     *
     * @param newPhase the new {@link ApplicationPhase} to transition to.
     */
    protected void changeLocalPhase ( ApplicationPhase newPhase ) {
        currentPhase = newPhase;
    }

    /**
     * Replaces the current server-side adapter. This is used when the player moves
     * between different areas of the server (e.g., from HallAdapter to RoomAdapter).
     *
     * @param serverSide the new {@link VirtualServerAdapter}.
     */
    public void setServerSideObject ( VirtualServerAdapter serverSide ) {
        this.serverSide = serverSide;
    }

    /**
     * Assigns the game controller to this player proxy once a match starts.
     *
     * @param controller the {@link MassiWuPeppeController} handling this player's inputs.
     */
    public void setController ( MassiWuPeppeController controller ){
        this.controller = controller;
    }

    /**
     * Retrieves the current server-side adapter tied to this player.
     *
     * @return the active {@link VirtualServerAdapter}.
     */
    public VirtualServerAdapter getServerSide(){
        return serverSide;
    }

    /**
     * Utility method to automatically inject the player's nickname into an outgoing/incoming packet.
     *
     * @param datapacket the packet to modify.
     */
    protected void addSenderNickname ( Datapacket datapacket ) {
        datapacket.setSenderNickname(nickname);
    }

    /**
     * Verifies if an incoming packet matches the current application phase of the player.
     * Prevents out-of-context commands (e.g., trying to play a card while in the Hall).
     *
     * @param datapacket the packet to validate.
     * @return {@code true} if the packet is valid for the current phase or is phase-agnostic (ANY).
     */
    protected boolean assureRightPhase ( Datapacket datapacket ) {
        if(datapacket.applicationPhase == ApplicationPhase.ANY){
            return true;
        }
        return datapacket.applicationPhase == currentPhase;
    }

    /**
     * Checks the network connectivity status of this proxy.
     *
     * @return {@code true} if the player is currently connected and responsive.
     */
    public boolean isConnected () {
        return connected;
    }

    // ============================================================
    //HEARTBEAT
    // ============================================================

    /**
     * Starts the asynchronous heartbeat system.
     * It spins up scheduled tasks to routinely ping the client and check for timeouts.
     */
    @Override
    public void startHeartbeating() {
        // sending heartbeat for each interval
        if (this.scheduler == null || this.scheduler.isShutdown()) {
            this.scheduler = Executors.newScheduledThreadPool(1);
        }
        scheduler.scheduleAtFixedRate(() -> {
            try {
                sendHeartbeat();
            } catch (Exception e) {
                try {
                    onConnectionLost();
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        }, 0, SEND_INTERVAL*2, TimeUnit.SECONDS);

        // check if client is silent
        scheduler.scheduleAtFixedRate(() -> {
            if (System.currentTimeMillis() - lastCheckIn > TIMEOUT_LIMIT * 1000) {
                try {
                    onConnectionLost();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, 5, SEND_INTERVAL, TimeUnit.SECONDS);
    }

    /**
     * Registers a received heartbeat ping from the client by updating the last check-in timestamp.
     */
    @Override
    public void receiveHeartbeat() {
        this.lastCheckIn = System.currentTimeMillis();
    }

    /**
     * Sends a ping to the client. The actual implementation varies between Socket and RMI.
     *
     * @throws Exception if the ping fails to send.
     */
    @Override
    public abstract void sendHeartbeat() throws Exception;

    /**
     * Triggered internally when the heartbeat monitor detects a timeout or a failed ping.
     *
     * @throws Exception if an error occurs while forcing the disconnection.
     */
    private void onConnectionLost() throws Exception {
        forceDisconnect();
    }

    // ============================================================
    //RESILIENCE
    // ============================================================

    /**
     * Instigates an immediate disconnection of the player from the server.
     * Typically called when a critical network exception occurs.
     *
     * @throws Exception if the disconnection procedure fails.
     */
    public void forceDisconnect() throws Exception {
        DisconnectPacket disconnectPacket = new DisconnectPacket();
        disconnect(disconnectPacket);
    }

    /**
     * Handles an explicit disconnection request.
     *
     * @param disconnectPacket the packet containing disconnection metadata.
     * @throws Exception if the internal disconnection procedure fails.
     */
    @Override
    public void disconnect( DisconnectPacket disconnectPacket) throws Exception {
        disconnectPacket.setSenderNickname(nickname);
        this.disconnectProcedure(disconnectPacket);
        System.out.println("Connection lost with " + nickname);
    }

    /**
     * Executes the actual logic to tear down the player's connection.
     * Stops the heartbeat scheduler, alerts the game controller (if in-game) to suspend the player,
     * and detaches from the server-side environment.
     *
     * @param disconnectPacket the packet containing disconnection details.
     * @throws Exception if an error occurs while notifying the server or controller.
     */
    protected void disconnectProcedure ( DisconnectPacket disconnectPacket ) throws Exception {
        connected = false;
        scheduler.shutdownNow();
        if (controller != null) {
            controller.executeCommand(new CommandPacket(PlayerActionEnum.DISCONNECT));
        }
        serverSide.disconnect(disconnectPacket);
    }

    /**
     * Restores the player's session using new network streams/stubs after a temporary drop.
     * It reconnects the controller, syncs the phase, and notifies the game that the player is back.
     *
     * @param newClientSide the new RMI client stub (null if using Socket).
     * @param newInput      the new Socket input stream (null if using RMI).
     * @param newOutput     the new Socket output stream (null if using RMI).
     * @throws Exception if the reconnection or state synchronization fails.
     */
    public void reconnect ( VirtualClient newClientSide, ObjectInputStream newInput, ObjectOutputStream newOutput) throws Exception {
        clientSide = newClientSide;
        input = newInput;
        output = newOutput;
        if (controller != null) {
            controller.executeCommand(new CommandPacket(PlayerActionEnum.CONNECT));
        }
        changeLocalPhase(ApplicationPhase.GAME);
        serverSide.syncPlayer(this);
    }

    // ============================================================
    //CONVERSION METHODS
    // ============================================================
    /**
     * Converts this proxy instance into an RMI-based proxy.
     * Used when a returning player decides to switch connection protocols.
     *
     * @return a new {@link RmiProxyPlayer} adopting this player's state.
     */
    public abstract RmiProxyPlayer convertToRmi();

    /**
     * Converts this proxy instance into a Socket-based proxy.
     * Used when a returning player decides to switch connection protocols.
     *
     * @return a new {@link SocketProxyPlayer} adopting this player's state.
     */
    public abstract SocketProxyPlayer convertToSocket();


    // ============================================================
    //PERSISTENCE
    // ============================================================
    /**
     * Re-initializes transient fields after the server has recovered its state from disk.
     * Ensures the proxy is in a clean, disconnected state waiting for the actual client to reconnect.
     *
     * @param server the newly booted {@link ServerMultiplexer} to attach to.
     */
    public void resumeAfterServerCrash(ServerMultiplexer server) {
        this.server = server;
        this.connected = false;
        this.scheduler = Executors.newScheduledThreadPool(2);
    }
}
