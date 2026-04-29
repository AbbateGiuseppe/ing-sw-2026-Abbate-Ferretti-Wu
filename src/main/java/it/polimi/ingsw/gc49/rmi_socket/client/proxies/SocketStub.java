package it.polimi.ingsw.gc49.rmi_socket.client.proxies;

import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.datapacket.uncertain.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualGameClient;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.Datapacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.GAME_phase.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SocketStub implements VirtualServer {
    final ObjectInputStream input;
    final ObjectOutputStream output;
    private volatile boolean running;

    public SocketStub(ObjectInputStream input, ObjectOutputStream output) {
        this.input = input;
        this.output = output;
    }


    public void runVirtualServer( VirtualGameClient client) throws IOException, ClassNotFoundException {
        //TODO: runVirtualServer??!
        running = true;

        Datapacket datapacket;
        try {
            while (running) { //TODO: && !Thread.currentThread().isInterrupted()
                try {
                    datapacket = (Datapacket) input.readObject();

                    switch(datapacket.getDatapacketType()){
                        case UPDATE_MODEL -> client.updateClientModel((UpdateModelPacket) datapacket);
                    }
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            stop();
        }
    }

    public void stop() {
        running = false;
    }

    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        output.writeObject(commandPacket);
        output.flush();
    }

    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        output.writeObject(disconnectPacket);
        output.flush();
    }

    @Override
    public void reconnect ( ReconnectPacket reconnectPacket ) throws Exception {
        output.writeObject(reconnectPacket);
        output.flush();
    }

    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        output.writeObject(hallJoinPacket);
        output.flush();
    }

    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        output.writeObject(hallCreatePacket);
        output.flush();
    }

    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        output.writeObject(roomLeavePacket);
        output.flush();
    }

}
