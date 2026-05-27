package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Cardboard;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;
import it.polimi.ingsw.gc49.server.model.Card.Card;

import java.util.List;

public class UpperDrawModelElement extends UpdateModelElement {
    private final List<Card> upperLine;
    private final List<Card> upperBuilding;
    private final int drawingPlayerIndex;
    private final Card drawnCard;
    private final boolean isCharacterCard;

    public UpperDrawModelElement ( String actionInfo, List<Card> upperLine, List<Card> upperBuilding,
                                   int drawingPlayerIndex, Card drawnCard, boolean isCharacterCard) {
        super(actionInfo);
        this.upperLine = upperLine;
        this.upperBuilding = upperBuilding;
        this.drawingPlayerIndex = drawingPlayerIndex;
        this.drawnCard = drawnCard;
        this.isCharacterCard = isCharacterCard;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.setUpperLine(upperLine);
        game.setUpperBuilding(upperBuilding);
        if(isCharacterCard) {
            game.getPlayer(drawingPlayerIndex).addCharacterCard(drawnCard);
        }else{
            game.getPlayer(drawingPlayerIndex).addBuildingCard(drawnCard);
        }
    }
}
