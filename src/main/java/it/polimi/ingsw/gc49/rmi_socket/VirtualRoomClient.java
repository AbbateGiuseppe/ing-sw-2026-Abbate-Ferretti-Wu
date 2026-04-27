package it.polimi.ingsw.gc49.rmi_socket;

import java.rmi.Remote;

public interface VirtualRoomClient extends Remote {
    void initializeClientRoom () throws Exception;

    void updateClientRoom () throws Exception;
}
