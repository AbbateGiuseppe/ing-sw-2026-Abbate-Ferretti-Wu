package it.polimi.ingsw.gc49.rmi_socket;

import java.rmi.Remote;

public interface VirtualHallServer extends Remote {
    void joinRoom () throws Exception;

    void createRoom () throws Exception;
}
