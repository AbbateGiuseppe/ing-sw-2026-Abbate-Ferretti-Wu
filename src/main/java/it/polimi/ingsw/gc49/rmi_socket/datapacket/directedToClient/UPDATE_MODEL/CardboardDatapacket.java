package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Era;

import java.util.List;

public class CardboardDatapacket extends UpdateModelElement {
    private final List<Card> discards;
    private final Era deckTopEra;
    private final List<Card> upperLine;
    private final List<Card> lowerLine;
    private final List<Card> upperBuilding;
    private final List<Card> lowerBuilding;

    public CardboardDatapacket ( List<Card> discard, Era deckTopEra, List<Card> upperLine,
                                 List<Card> lowerLine, List<Card> upperBuilding, List<Card> lowerBuilding) {
        super();
        this.discards = discard;
        this.deckTopEra = deckTopEra;
        this.upperLine = upperLine;
        this.lowerLine = lowerLine;
        this.upperBuilding = upperBuilding;
        this.lowerBuilding = lowerBuilding;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.addDiscards(discards);
        game.setDeckTopEra(deckTopEra);
        game.setUpperLine(upperLine);
        game.setLowerLine(lowerLine);
        game.setUpperBuilding(upperBuilding);
        game.setLowerBuilding(lowerBuilding);
    }
}
