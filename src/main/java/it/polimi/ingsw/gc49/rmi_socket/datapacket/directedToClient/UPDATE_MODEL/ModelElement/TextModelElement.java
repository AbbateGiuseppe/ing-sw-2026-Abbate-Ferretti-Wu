package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;

public class TextModelElement extends UpdateModelElement {

    public TextModelElement ( String actionInfo ) {
        super(actionInfo);
    }

    @Override
    public void updateMockupModel ( MockupGame game ) {
        //nothing changed, sent just text
    }
}
