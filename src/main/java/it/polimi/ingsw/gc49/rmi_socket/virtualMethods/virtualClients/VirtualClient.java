package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients;

import it.polimi.ingsw.gc49.datapacket.STRING.StringPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.Heartbeatable;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualClient extends Remote, VirtualGameClient, VirtualHallClient, VirtualRoomClient {
    void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception;

    void sendString ( StringPacket stringPacket ) throws Exception;
}
