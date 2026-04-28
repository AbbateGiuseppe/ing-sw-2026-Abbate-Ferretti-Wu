package it.polimi.ingsw.gc49.datapacket.sentFromServer.UPDATE_MODEL;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.View.mockupModel.MockupPlayer;

import java.util.List;

public class PlayersDatapacket extends UpdateModelElement {
    private final int currentPlayerIndex;
    private final List<MockupPlayer> players;

    public PlayersDatapacket ( int currentPlayerIndex, List<MockupPlayer> players ) {
        super();
        this.currentPlayerIndex = currentPlayerIndex;
        this.players = players;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.setCurrentPlayerIndex( currentPlayerIndex );
        game.setPlayers( players );
    }
}
