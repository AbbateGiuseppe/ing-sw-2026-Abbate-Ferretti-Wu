package it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inRoom;

import it.polimi.ingsw.gc49.datapacket.sentFromClient.ROOM_COMMAND.RoomCommandPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.ROOM_RETURN.ClientRoomInitializePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.ROOM_RETURN.ClientRoomUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.ROOM_COMMAND.RoomLeavePacket;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inGame.ProxyPlayerGameSocket;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inHall.ProxyPlayerHallSocket;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualHallServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualRoomServer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

public class ProxyPlayerRoomSocket extends ProxyPlayer {
    private final VirtualRoomServer serverSide;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private volatile boolean running;

    public ProxyPlayerRoomSocket ( ServerMultiplexer server, String nickname,
                                   VirtualRoomServer serverSide,
                                   ObjectInputStream input, ObjectOutputStream output ) {
        super( ConnectionType.SOCKET, SubclassType.ROOM, server, nickname );
        this.serverSide = serverSide;
        this.input = input;
        this.output = output;
    }
    public ProxyPlayerRoomSocket ( ProxyPlayer OldProxyPlayer,
                                   VirtualRoomServer serverSide,
                                   ObjectInputStream input, ObjectOutputStream output ) {
        super( ConnectionType.SOCKET, SubclassType.ROOM, OldProxyPlayer.server, OldProxyPlayer.nickname );
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
                    case ROOM_COMMAND -> {
                        switch (((RoomCommandPacket)datapacket).commandType) {
                            case LEAVE -> leaveRoom((RoomLeavePacket) datapacket);
                            default -> throw new RuntimeException("Unrecognized ROOM_COMMAND type");
                        }
                    }
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
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        addSenderNickname(roomLeavePacket);
        serverSide.leaveRoom(roomLeavePacket);
    }

    //### from server to client commands
    @Override
    public void initializeClientRoom ( ClientRoomInitializePacket clientRoomInitializePacket ) throws Exception {
        output.writeObject(clientRoomInitializePacket);
        output.flush();
    }
    @Override
    public void updateClientRoom ( ClientRoomUpdatePacket clientRoomUpdatePacket ) throws Exception {
        output.writeObject(clientRoomUpdatePacket);
        output.flush();
    }

    //### utils
    @Override
    public ProxyPlayer changeSubclass ( SubclassType newSubclass, VirtualServer newServerSide ) {
        switch(newSubclass){
            case GAME -> { return new ProxyPlayerGameSocket(this, (VirtualGameServer) newServerSide, input, output); }
            case HALL -> { return new ProxyPlayerHallSocket(this, (VirtualHallServer) newServerSide, input, output); }
            case ROOM -> { return this; }
            default -> { return this; }
        }
    }
}
