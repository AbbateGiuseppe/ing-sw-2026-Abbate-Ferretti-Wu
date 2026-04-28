package it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inGame;

import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ANY_phase.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.uncertain.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inHall.ProxyPlayerHallSocket;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inRoom.ProxyPlayerRoomSocket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualHallServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualRoomServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

public class ProxyPlayerGameSocket extends ProxyPlayer {
    private final VirtualGameServer serverSide;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private volatile boolean running;

    public ProxyPlayerGameSocket ( ServerMultiplexer server, String nickname,
                                   VirtualGameServer serverSide,
                                   ObjectInputStream input, ObjectOutputStream output ) {
        super( ConnectionType.SOCKET, SubclassType.GAME, server, nickname );
        this.serverSide = serverSide;
        this.input = input;
        this.output = output;
    }
    public ProxyPlayerGameSocket ( ProxyPlayer OldProxyPlayer,
                                   VirtualGameServer serverSide,
                                   ObjectInputStream input, ObjectOutputStream output ){
        super( ConnectionType.SOCKET, SubclassType.GAME, OldProxyPlayer.server, OldProxyPlayer.nickname );
        this.serverSide = serverSide;
        this.input = input;
        this.output = output;
    }


    //### socket-input reader
    @Override
    public void runVirtualClient() throws SocketException {
        running = true;

        Datapacket datapacket;

        try {
            while (running) {
                datapacket = (Datapacket) input.readObject();

                switch(datapacket.datapacketType){
                    case COMMAND -> sendCommand( (CommandPacket) datapacket );
                    case DISCONNECT -> disconnect( (DisconnectPacket) datapacket );
                    case RECONNECT -> reconnect( (ReconnectPacket) datapacket );
                    default -> throw new RuntimeException("Unsendable datapacket type: " + datapacket.getDatapacketType() + " from: " + nickname);
                }
            }
        } catch (SocketException e) {
            throw new SocketException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            stop();
        }
    }
    public void stop() {
        running = false;
    }

    //### from client to server commands
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        addSenderNickname(commandPacket);
        serverSide.sendCommand(commandPacket);
    }
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        addSenderNickname(disconnectPacket);
        serverSide.disconnect(disconnectPacket);
    }
    @Override
    public void reconnect ( ReconnectPacket reconnectPacket ) throws Exception {
        addSenderNickname(reconnectPacket);
        serverSide.reconnect(reconnectPacket);
    }

    //### from server to client commands
    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception {
        output.writeObject(initializeModelPacket);
        output.flush();
    }
    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception {
        output.writeObject(updateModelPacket);
        output.flush();
    }
    @Override
    public void reportError ( ErrorPacket errorPacket ) throws Exception {
        output.writeObject(errorPacket);
        output.flush();
    }

    //### utils
    @Override
    public ProxyPlayer changeSubclass ( SubclassType newSubclass, VirtualServer newServerSide ) {
        switch(newSubclass){
            case GAME -> { return this; }
            case HALL -> { return new ProxyPlayerHallSocket(this, (VirtualHallServer) newServerSide, input, output); }
            case ROOM -> { return new ProxyPlayerRoomSocket(this, (VirtualRoomServer) newServerSide, input, output); }
            default -> { return this; }
        }
    }
}
