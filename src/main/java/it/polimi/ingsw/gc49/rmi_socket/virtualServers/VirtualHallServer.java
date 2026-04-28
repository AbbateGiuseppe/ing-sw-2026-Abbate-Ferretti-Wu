package it.polimi.ingsw.gc49.rmi_socket.virtualServers;

import it.polimi.ingsw.gc49.datapacket.sentFromClient.HALL_COMMAND.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.sentFromClient.HALL_COMMAND.HallJoinPacket;

import java.rmi.Remote;

public interface VirtualHallServer extends VirtualServer {
    void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception;

    void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception;
}
