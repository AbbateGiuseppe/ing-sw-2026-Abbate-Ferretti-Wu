package it.polimi.ingsw.gc49.rmi_socket;

import java.rmi.Remote;

public interface VirtualRoomServer extends Remote {
    void leaveRoom () throws Exception;
}
