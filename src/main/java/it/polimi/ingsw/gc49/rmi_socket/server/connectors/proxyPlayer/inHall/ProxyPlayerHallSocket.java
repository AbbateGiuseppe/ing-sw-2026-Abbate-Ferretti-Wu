package it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inHall;

import it.polimi.ingsw.gc49.datapacket.sentFromServer.HALL_RETURN.ClientHallInitializePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromServer.HALL_RETURN.ClientHallUpdatePacket;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.HALL_COMMAND.HallCommandPacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.HALL_COMMAND.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.HALL_COMMAND.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inGame.ProxyPlayerGameSocket;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.proxyPlayer.inRoom.ProxyPlayerRoomSocket;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualHallServer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualRoomServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualServers.VirtualServer;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.SocketException;

public class ProxyPlayerHallSocket extends ProxyPlayer {
    private final VirtualHallServer serverSide;
    private final ObjectInputStream input;
    private final ObjectOutputStream output;
    private volatile boolean running;

    public ProxyPlayerHallSocket ( ServerMultiplexer server, String nickname,
                                   VirtualHallServer serverSide,
                                   ObjectInputStream input, ObjectOutputStream output ) {
        super( ConnectionType.SOCKET, SubclassType.HALL, server, nickname );
        this.serverSide = serverSide;
        this.input = input;
        this.output = output;
    }
    public ProxyPlayerHallSocket ( ProxyPlayer OldProxyPlayer,
                                   VirtualHallServer serverSide,
                                   ObjectInputStream input, ObjectOutputStream output ){
        super ( ConnectionType.SOCKET, SubclassType.HALL, OldProxyPlayer.server, OldProxyPlayer.nickname );
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
                    case HALL_COMMAND -> {
                        switch (((HallCommandPacket)datapacket).commandType) {
                            case JOIN -> joinRoom((HallJoinPacket) datapacket);
                            case CREATE -> createRoom((HallCreatePacket) datapacket);
                            default -> throw new RuntimeException("Unrecognized HALL_COMMAND type");
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
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        addSenderNickname(hallJoinPacket);
        serverSide.joinRoom(hallJoinPacket);
    }
    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        addSenderNickname(hallCreatePacket);
        serverSide.createRoom(hallCreatePacket);
    }

    //### from server to client commands
    @Override
    public void initializeClientHall ( ClientHallInitializePacket clientHallInitializePacket ) throws Exception {
        output.writeObject(clientHallInitializePacket);
        output.flush();
    }
    @Override
    public void updateClientHall ( ClientHallUpdatePacket clientHallUpdatePacket ) throws Exception {
        output.writeObject(clientHallUpdatePacket);
        output.flush();
    }

    //### utils
    @Override
    public ProxyPlayer changeSubclass ( SubclassType newSubclass, VirtualServer newServerSide ) {
        switch(newSubclass){
            case GAME -> { return new ProxyPlayerGameSocket(this, (VirtualGameServer) newServerSide, input, output); }
            case HALL -> { return this; }
            case ROOM -> { return new ProxyPlayerRoomSocket(this, (VirtualRoomServer) newServerSide, input, output); }
            default -> { return this; }
        }
    }
}
