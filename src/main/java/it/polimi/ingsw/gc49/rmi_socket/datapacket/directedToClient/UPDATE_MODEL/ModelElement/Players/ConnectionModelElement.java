package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;

public class ConnectionModelElement extends UpdateModelElement {
    private final int connectionPlayerIndex;
    private final boolean connected;

    /**
     * Update used during disconnection/reconnection, its purpose it's to notify all the players of a change in someone's else connection.
     * @param disconnectionMessage, info message displayed;
     * @param connectionPlayerIndex, index of the changing connection player;
     * @param connected, is true if the player has connected, is false if the player has disconnected;
     */
    public ConnectionModelElement ( String disconnectionMessage, int connectionPlayerIndex, boolean connected ) {
        super(disconnectionMessage);
        this.connectionPlayerIndex = connectionPlayerIndex;
        this.connected = connected;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.getPlayer(connectionPlayerIndex).setConnected(connected);
    }
}
