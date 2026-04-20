package it.polimi.ingsw.gc49.rmi_socket.client;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.VirtualClient;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.UpdateModel;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface VirtualClientRmi extends Remote, VirtualClient {
    @Override
    void initializeClientModel ( MockupGame mockupGame ) throws RemoteException;

    @Override
    void updateClientModel ( UpdateModel updateModel ) throws RemoteException;

    @Override
    void reportError ( String details ) throws RemoteException;
}
