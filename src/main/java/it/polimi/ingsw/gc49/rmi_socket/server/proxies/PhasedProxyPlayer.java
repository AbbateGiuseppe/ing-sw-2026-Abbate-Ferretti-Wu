package it.polimi.ingsw.gc49.rmi_socket.server.proxies;

import it.polimi.ingsw.gc49.controller.MassiWuController;
import it.polimi.ingsw.gc49.controller.PlayerActionEnum;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Heartbeatable;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualServerAdapter;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class PhasedProxyPlayer implements VirtualClient, VirtualServer, Heartbeatable {
    protected final ServerMultiplexer server;
    public final String nickname;
    protected ApplicationPhase currentPhase;
    protected VirtualServerAdapter serverSide;
    protected MassiWuController controller;
    protected VirtualClient clientSide;
    protected ObjectInputStream input;
    protected ObjectOutputStream output;
    protected volatile boolean connected;
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
    public void runVirtualClient() throws SocketException{
        startHeartbeating();
    }

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

    public String getNickname(){
        return nickname;
    }
    public boolean isConnected () {
        return connected;
    }

    ///----------------------------------
    // HEARTBEAT
    @Override
    public void startHeartbeating() {
        // sending heartbeat for each interval
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

    @Override
    public void receiveHeartbeat() {
        this.lastCheckIn = System.currentTimeMillis();
    }

    @Override
    public abstract void sendHeartbeat() throws Exception;

    private void onConnectionLost() throws Exception {
        DisconnectPacket disconnectPacket = new DisconnectPacket();
        disconnectPacket.setSenderNickname(nickname);
        disconnectProcedure(disconnectPacket);
    }

    ///---------------------------------------
    // methods for disconnection/reconnection
    @Override
    public void disconnect( DisconnectPacket disconnectPacket) throws Exception {
        this.disconnectProcedure(disconnectPacket);
    }

    public void disconnectProcedure ( DisconnectPacket disconnectPacket ) throws Exception {
        connected = false;
        scheduler.shutdownNow();
        controller.executeCommand(new CommandPacket(PlayerActionEnum.DISCONNECT));
        serverSide.disconnect(disconnectPacket);
    }

    public abstract void reconnectProcedure ( VirtualClient newClientSide, ObjectInputStream newInput, ObjectOutputStream newOutput) throws Exception;
}
