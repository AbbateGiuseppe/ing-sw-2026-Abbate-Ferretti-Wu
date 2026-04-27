package it.polimi.ingsw.gc49.rmi_socket;

import java.rmi.Remote;

public interface VirtualHallClient extends Remote {
    void initializeClientHall () throws Exception;

    void updateClientHall () throws Exception;
}
