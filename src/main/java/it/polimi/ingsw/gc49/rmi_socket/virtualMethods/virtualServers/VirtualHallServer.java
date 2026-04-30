package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers;

import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;

import java.rmi.Remote;

public interface VirtualHallServer extends Remote {
    void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception;

    void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception;
}
