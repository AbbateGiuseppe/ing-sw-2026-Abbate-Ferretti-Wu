package it.polimi.ingsw.gc49.rmi.client;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.VirtualClient;
import it.polimi.ingsw.gc49.VirtualServer;
import it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL.MockupModelDatapacketable;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface VirtualClientRmi extends Remote, VirtualClient {
    @Override
    void initializeClientModel ( MockupGame mockupGame ) throws RemoteException;

    @Override
    void updateClientModel ( List<MockupModelDatapacketable> updatesList ) throws RemoteException;

    @Override
    void reportError ( String details ) throws RemoteException;
}
