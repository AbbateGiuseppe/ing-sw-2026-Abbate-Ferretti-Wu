package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Disconnectable;

import java.rmi.Remote;

public interface VirtualHallServer extends Remote, Disconnectable {
    void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception;

    void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception;
}
