package it.polimi.ingsw.gc49.datapacket.UPDATE_MODEL;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.View.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.datapacket.Datapacket;

import java.util.List;

public class PlayersDatapacket extends Datapacket implements MockupModelDatapacketable {
    private final int currentPlayerIndex;
    private final List<MockupPlayer> players;

    public PlayersDatapacket ( int currentPlayerIndex, List<MockupPlayer> players ) {
        super(DatapacketType.UPDATE_MODEL);
        this.currentPlayerIndex = currentPlayerIndex;
        this.players = players;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.setCurrentPlayerIndex( currentPlayerIndex );
        game.setPlayers( players );
    }
}
