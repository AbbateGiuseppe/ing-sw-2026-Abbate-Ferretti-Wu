package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.UpdateModelElement;
import it.polimi.ingsw.gc49.server.model.QueueUpdatable;

import java.util.ArrayList;
import java.util.List;

/**
 * A test double for {@link QueueUpdatable} that simply records the elements queued to it.
 * Used to cover the {@code queueUpdater != null} branch inside each card's {@code onEventEffect}.
 */
class FakeQueueUpdatable implements QueueUpdatable {
    final List<UpdateModelElement> queued = new ArrayList<>();

    @Override
    public void queueUpdateModelElement(UpdateModelElement updateModelElement) {
        queued.add(updateModelElement);
    }
}
