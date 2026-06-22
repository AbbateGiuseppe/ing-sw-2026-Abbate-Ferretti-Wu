package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL;

import it.polimi.ingsw.gc49.client.user_input_interfaces.UserInputInterface;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;

import java.util.*;

public class UpdateModelPacket extends Datapacket {
    private final Deque<UpdateModelElement> updatesHeap = new ArrayDeque<>();

    public UpdateModelPacket () {
        super(DatapacketType.UPDATE_MODEL, ApplicationPhase.GAME);
    }

    public void addUpdateElement ( UpdateModelElement element ) {
        updatesHeap.add(element);
    }

    public void updateTheMockupModel( MockupGame mockupGame, UserInputInterface inputInterface ) {
        while (!updatesHeap.isEmpty()) {
            inputInterface.printString(updatesHeap.peek().actionInfo); //prints the info about the update's action. On the client interface
            assert updatesHeap.peek() != null;
            updatesHeap.peek().updateMockupModel(mockupGame);
            updatesHeap.pop();
        }
    }

    public UpdateResult updateTheMockupModelAndGetResult(MockupGame mockupGame, UserInputInterface inputInterface) {
        List<Class<? extends UpdateModelElement>> changedElements = new ArrayList<>();

        while (!updatesHeap.isEmpty()) {
            UpdateModelElement element = updatesHeap.peek();

            inputInterface.printString(element.actionInfo);
            changedElements.add(element.getClass());
            element.updateMockupModel(mockupGame);
            updatesHeap.pop();
        }

        return new UpdateResult(changedElements);
    }

    public record UpdateResult(
            List<Class<? extends UpdateModelElement>> changedElements
    ) { }
}
