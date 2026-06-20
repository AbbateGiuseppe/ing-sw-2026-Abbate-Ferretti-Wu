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
    private final int drawableUpper;
    private final int drawableLower;
    private final int newFood;
    private final int newPoints;

    public LowerDrawModelElement ( String actionInfo, List<Card> lowerLine, List<Card> lowerBuilding,
                                   int drawingPlayerIndex, Card drawnCard, boolean isCharacterCard) {
        this(actionInfo, lowerLine, lowerBuilding, drawingPlayerIndex, drawnCard, isCharacterCard, -1, -1);
    }

    public LowerDrawModelElement ( String actionInfo, List<Card> lowerLine, List<Card> lowerBuilding,
                                   int drawingPlayerIndex, Card drawnCard, boolean isCharacterCard,
                                   int drawableUpper, int drawableLower) {
        this(actionInfo, lowerLine, lowerBuilding, drawingPlayerIndex, drawnCard, isCharacterCard,
                drawableUpper, drawableLower, Integer.MIN_VALUE, Integer.MIN_VALUE);
    }

    public LowerDrawModelElement ( String actionInfo, List<Card> lowerLine, List<Card> lowerBuilding,
                                   int drawingPlayerIndex, Card drawnCard, boolean isCharacterCard,
                                   int drawableUpper, int drawableLower, int newFood, int newPoints) {
        super(actionInfo);
        this.lowerLine = lowerLine;
        this.lowerBuilding = lowerBuilding;
        this.drawingPlayerIndex = drawingPlayerIndex;
        this.drawnCard = drawnCard;
        this.isCharacterCard = isCharacterCard;
        this.drawableUpper = drawableUpper;
        this.drawableLower = drawableLower;
        this.newFood = newFood;
        this.newPoints = newPoints;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.setLowerLine(lowerLine);
        game.setLowerBuilding(lowerBuilding);
        if (drawableUpper >= 0 && drawableLower >= 0) {
            game.getPlayer(drawingPlayerIndex).setDrawableUpper(drawableUpper);
            game.getPlayer(drawingPlayerIndex).setDrawableLower(drawableLower);
        }
        if (newFood != Integer.MIN_VALUE && newPoints != Integer.MIN_VALUE) {
            game.getPlayer(drawingPlayerIndex).setFood(newFood);
            game.getPlayer(drawingPlayerIndex).setPoints(newPoints);
        }
        if(isCharacterCard) {
            game.getPlayer(drawingPlayerIndex).addCharacterCard(drawnCard);
        }else{
            game.getPlayer(drawingPlayerIndex).addBuildingCard(drawnCard);
        }
    }
}
