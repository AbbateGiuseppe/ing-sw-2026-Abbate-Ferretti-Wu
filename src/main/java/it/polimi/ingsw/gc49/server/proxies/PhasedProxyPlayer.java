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

public abstract class PhasedProxyPlayer implements VirtualClient, VirtualServer, Heartbeatable, Serializable {
    private static final long serialVersionUID = 3305693863268735459L;

    protected transient ServerMultiplexer server;
    public final String nickname;
    protected ApplicationPhase currentPhase;
    protected VirtualServerAdapter serverSide;
    protected transient MassiWuPeppeController controller;
    protected transient VirtualClient clientSide;
    protected transient ObjectInputStream input;
    protected transient ObjectOutputStream output;
    protected volatile boolean connected;
    //Handling TIMEOUTS
    protected long lastCheckIn = System.currentTimeMillis();
    protected transient ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
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
     * @throws SocketException, if it loses connection (for socket only), starts a disconnection event chain.
     */
    public void runVirtualClient() throws SocketException{
        startHeartbeating();
    }

    protected void changeLocalPhase ( ApplicationPhase newPhase ) {
        currentPhase = newPhase;
    }

    public void setServerSideObject ( VirtualServerAdapter serverSide ) {
        this.serverSide = serverSide;
    }
    public void setController ( MassiWuPeppeController controller ){
        this.controller = controller;
    }
    public VirtualServerAdapter getServerSide(){
        return serverSide;
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

    public boolean isConnected () {
        return connected;
    }

    public void markConnected() {
        connected = true;
        lastCheckIn = System.currentTimeMillis();
    }

    ///----------------------------------
    // HEARTBEAT
    @Override
    public void startHeartbeating() {
        markConnected();
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

    @Override
    public void receiveHeartbeat() {
        this.lastCheckIn = System.currentTimeMillis();
    }

    @Override
    public abstract void sendHeartbeat() throws Exception;

    private void onConnectionLost() throws Exception {
        forceDisconnect();
    }

    ///---------------------------------------
    // methods for disconnection/reconnection

    /**
     * Forces a disconnection of the player from the server, typically called when an exception with that player is thrown.
     */
    public void forceDisconnect() throws Exception {
        DisconnectPacket disconnectPacket = new DisconnectPacket();
        disconnect(disconnectPacket);
    }

    @Override
    public void disconnect( DisconnectPacket disconnectPacket) throws Exception {
        disconnectPacket.setSenderNickname(nickname);
        this.disconnectProcedure(disconnectPacket);
        System.out.println("Connection lost with " + nickname);
    }

    protected void disconnectProcedure ( DisconnectPacket disconnectPacket ) throws Exception {
        connected = false;
        scheduler.shutdownNow();
        if (controller != null) {
            controller.executeCommand(new CommandPacket(PlayerActionEnum.DISCONNECT));
        }
        serverSide.disconnect(disconnectPacket);
    }

    public void reconnect ( VirtualClient newClientSide, ObjectInputStream newInput, ObjectOutputStream newOutput) throws Exception {
        clientSide = newClientSide;
        input = newInput;
        output = newOutput;
        if (controller != null) {
            controller.executeCommand(new CommandPacket(PlayerActionEnum.CONNECT));
        }
        serverSide.syncPlayer(this);
    }

    ///-------------------------------
    // conversions methods
    public abstract RmiProxyPlayer convertToRmi();
    public abstract SocketProxyPlayer convertToSocket();

    public void resumeAfterServerCrash(ServerMultiplexer server) {
        this.server = server; // Ri-assegna il server (era transient)
        this.connected = false; // Importante: deve essere false finché il client non torna
        this.scheduler = Executors.newScheduledThreadPool(2);
    }
}
