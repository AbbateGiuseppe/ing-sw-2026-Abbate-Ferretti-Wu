package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Cardboard;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;
import it.polimi.ingsw.gc49.server.model.Card.Card;

import java.util.List;

public class LowerDrawModelElement extends UpdateModelElement {
    private final List<Card> lowerLine;
    private final List<Card> lowerBuilding;
    private final int drawingPlayerIndex;
    private final Card drawnCard;
    private final boolean isCharacterCard;

    public LowerDrawModelElement ( String actionInfo, List<Card> lowerLine, List<Card> lowerBuilding,
                                   int drawingPlayerIndex, Card drawnCard, boolean isCharacterCard) {
        super(actionInfo);
        this.lowerLine = lowerLine;
        this.lowerBuilding = lowerBuilding;
        this.drawingPlayerIndex = drawingPlayerIndex;
        this.drawnCard = drawnCard;
        this.isCharacterCard = isCharacterCard;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.setLowerLine(lowerLine);
        game.setLowerBuilding(lowerBuilding);
        if(isCharacterCard) {
            game.getPlayer(drawingPlayerIndex).addCharacterCard(drawnCard);
        }else{
            game.getPlayer(drawingPlayerIndex).addBuildingCard(drawnCard);
        }
    }
}
