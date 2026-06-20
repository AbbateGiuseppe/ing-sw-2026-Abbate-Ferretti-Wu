package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement;

import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;

public class GameStatusModelElement extends UpdateModelElement {
    private final String phaseName;
    private final String finalStandings;
    private final int tribeDeckRemaining;
    private final int buildingDeckRemaining;

    public GameStatusModelElement(String actionInfo, String phaseName, String finalStandings,
                                  int tribeDeckRemaining, int buildingDeckRemaining) {
        super(actionInfo);
        this.phaseName = phaseName;
        this.finalStandings = finalStandings;
        this.tribeDeckRemaining = tribeDeckRemaining;
        this.buildingDeckRemaining = buildingDeckRemaining;
    }

    @Override
    public void updateMockupModel(MockupGame game) {
        game.setPhaseName(phaseName);
        game.setFinalStandings(finalStandings);
        game.setTribeDeckRemaining(tribeDeckRemaining);
        game.setBuildingDeckRemaining(buildingDeckRemaining);
    }
}
