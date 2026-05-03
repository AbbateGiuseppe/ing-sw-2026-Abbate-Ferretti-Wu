package it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_MODEL;

import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.model.Card.Card;
import it.polimi.ingsw.gc49.model.Era;

import java.util.ArrayList;
import java.util.List;

public class CardboardDatapacket extends UpdateModelElement {
    private final List<Card> discards;
    private final Era deckTopEra;
    private final ArrayList<Card> upperLine;
    private final ArrayList<Card> lowerLine;
    private final ArrayList<Card> upperBuilding;
    private final ArrayList<Card> lowerBuilding;

    public CardboardDatapacket ( List<Card> discard, Era deckTopEra, ArrayList<Card> upperLine,
                                 ArrayList<Card> lowerLine, ArrayList<Card> upperBuilding, ArrayList<Card> lowerBuilding) {
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
