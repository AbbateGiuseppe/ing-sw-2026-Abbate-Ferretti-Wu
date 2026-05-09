package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;
import it.polimi.ingsw.gc49.server.model.Totem;

public class TotemModelElement extends UpdateModelElement {
    private final int choosingPlayer;
    private final Totem chosenTotem;

    public TotemModelElement ( String actionInfo, int choosingPlayer, Totem chosenTotem ) {
        super(actionInfo);
        this.choosingPlayer = choosingPlayer;
        this.chosenTotem = chosenTotem;
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        game.getPlayer(choosingPlayer).setTotem(chosenTotem);
    }
}
