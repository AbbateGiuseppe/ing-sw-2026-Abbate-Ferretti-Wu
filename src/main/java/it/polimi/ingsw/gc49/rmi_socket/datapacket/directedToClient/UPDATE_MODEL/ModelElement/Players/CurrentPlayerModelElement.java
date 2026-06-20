package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;

public class CurrentPlayerModelElement extends UpdateModelElement {
    private final int currentPlayerIndex;
    private final int drawableUpper;
    private final int drawableLower;

    public CurrentPlayerModelElement ( String actionInfo, int currentPlayerIndex ) {
        this(actionInfo, currentPlayerIndex, -1, -1);
    }

    public CurrentPlayerModelElement ( String actionInfo, int currentPlayerIndex, int drawableUpper, int drawableLower ) {
        super(actionInfo);
        this.currentPlayerIndex = currentPlayerIndex;
        this.drawableUpper = drawableUpper;
        this.drawableLower = drawableLower;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.setCurrentPlayerIndex(currentPlayerIndex);
        if (currentPlayerIndex >= 0 && drawableUpper >= 0 && drawableLower >= 0) {
            game.getPlayer(currentPlayerIndex).setDrawableUpper(drawableUpper);
            game.getPlayer(currentPlayerIndex).setDrawableLower(drawableLower);
        }
    }
}
