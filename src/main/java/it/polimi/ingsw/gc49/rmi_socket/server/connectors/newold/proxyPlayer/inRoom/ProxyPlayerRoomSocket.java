package it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.proxyPlayer.inRoom;

import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.RoomCommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.INITIALIZE.RoomClientInitializePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.UPDATE.RoomClientUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.proxyPlayer.inGame.ProxyPlayerGameSocket;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.proxyPlayer.inHall.ProxyPlayerHallSocket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualHallServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualRoomServer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.newold.ProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

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
    public void initializeClientRoom ( RoomClientInitializePacket roomClientInitializePacket ) throws Exception {
        output.writeObject(roomClientInitializePacket);
        output.flush();
    }
    @Override
    public void updateClientRoom ( RoomClientUpdatePacket roomClientUpdatePacket ) throws Exception {
        output.writeObject(roomClientUpdatePacket);
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
