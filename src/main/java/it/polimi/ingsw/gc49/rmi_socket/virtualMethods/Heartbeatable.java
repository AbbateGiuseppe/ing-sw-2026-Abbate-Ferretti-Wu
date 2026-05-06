package it.polimi.ingsw.gc49.rmi_socket.virtualMethods;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Heartbeatable extends Remote {
    void startHeartbeating() throws Exception;
    void sendHeartbeat() throws  Exception;
    void receiveHeartbeat() throws Exception;
}
