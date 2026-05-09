package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;

import java.util.List;

public class PlayersModelElement extends UpdateModelElement {
    private final int currentPlayerIndex;
    private final List<MockupPlayer> players;

    public PlayersModelElement ( String actionInfo, int currentPlayerIndex, List<MockupPlayer> players ) {
        super(actionInfo);
        this.currentPlayerIndex = currentPlayerIndex;
        this.players = players;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.setCurrentPlayerIndex( currentPlayerIndex );
        game.setPlayers( players );
    }
}
