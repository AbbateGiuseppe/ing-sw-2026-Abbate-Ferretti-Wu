package it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL;

import it.polimi.ingsw.gc49.client.user_input_interfaces.UserInputInterface;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.Datapacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.OfferOrderboard.ReturnModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsAllModelElement;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsOneModelElement;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Card.EventCard.EventCard;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class UpdateModelPacket extends Datapacket {
    private final Deque<UpdateModelElement> updatesHeap = new ArrayDeque<>();

    public UpdateModelPacket () {
        super(DatapacketType.UPDATE_MODEL, ApplicationPhase.GAME);
    }

    public void addUpdateElement ( UpdateModelElement element ) {
        updatesHeap.add(element);
    }

    public void updateTheMockupModel( MockupGame mockupGame, UserInputInterface inputInterface ) {
        updateTheMockupModelAndGetResult(mockupGame, inputInterface);
    }

    public List<Class<? extends UpdateModelElement>> updateTheMockupModelAndGetChangedElements(
            MockupGame mockupGame, UserInputInterface inputInterface) {
        return updateTheMockupModelAndGetResult(mockupGame, inputInterface).changedElements();
    }

    public UpdateResult updateTheMockupModelAndGetResult(MockupGame mockupGame, UserInputInterface inputInterface) {
        List<Class<? extends UpdateModelElement>> changedElements = new ArrayList<>();
        List<String> actionInfos = new ArrayList<>();
        List<String> foodAndPointSummaries = new ArrayList<>();
        List<String> eventResolutionSummaries = new ArrayList<>();

        while (!updatesHeap.isEmpty()) {
            UpdateModelElement element = updatesHeap.peek();
            if (element == null) {
                updatesHeap.pop();
                continue;
            }

            String actionInfo = element.getActionInfo();
            if (inputInterface != null) {
                inputInterface.printString(actionInfo);
            }
            mockupGame.setLatestAction(actionInfo);
            changedElements.add(element.getClass());
            actionInfos.add(actionInfo);
            foodAndPointSummaries.addAll(describeFoodAndPointChanges(mockupGame, element));
            eventResolutionSummaries.addAll(describeEventResolution(mockupGame, element));
            element.updateMockupModel(mockupGame);
            updatesHeap.pop();
        }

        return new UpdateResult(changedElements, actionInfos, foodAndPointSummaries, eventResolutionSummaries);
    }

    private List<String> describeFoodAndPointChanges(MockupGame game, UpdateModelElement element) {
        List<String> summaries = new ArrayList<>();
        if (element instanceof FoodAndPointsOneModelElement foodAndPoints) {
            addSummary(summaries, game.getPlayer(foodAndPoints.getPlayerIndex()),
                    foodAndPoints.getNewFood(), foodAndPoints.getNewPoints());
        } else if (element instanceof ReturnModelElement returnedPlayer) {
            addSummary(summaries, game.getPlayer(returnedPlayer.getPlayerIndex()),
                    returnedPlayer.getNewFood(), returnedPlayer.getNewPoints());
        } else if (element instanceof FoodAndPointsAllModelElement foodAndPoints) {
            List<Integer> newFood = foodAndPoints.getNewFood();
            List<Integer> newPoints = foodAndPoints.getNewPoints();
            int size = Math.min(game.getPlayers().size(), Math.min(newFood.size(), newPoints.size()));
            for (int i = 0; i < size; i++) {
                addSummary(summaries, game.getPlayer(i), newFood.get(i), newPoints.get(i));
            }
        }
        return summaries;
    }

    private List<String> describeEventResolution(MockupGame game, UpdateModelElement element) {
        List<String> lines = new ArrayList<>();
        if (!(element instanceof FoodAndPointsAllModelElement foodAndPoints)) {
            return lines;
        }

        String eventName = eventName(game, element.getActionInfo());
        if (!isEventCardResolution(element.getActionInfo()) && eventName.isBlank()) {
            return lines;
        }

        lines.add("Event: " + eventName);
        List<Integer> newFood = foodAndPoints.getNewFood();
        List<Integer> newPoints = foodAndPoints.getNewPoints();
        int size = Math.min(game.getPlayers().size(), Math.min(newFood.size(), newPoints.size()));
        for (int i = 0; i < size; i++) {
            MockupPlayer player = game.getPlayer(i);
            int foodDelta = newFood.get(i) - player.getFood();
            int pointsDelta = newPoints.get(i) - player.getPoints();
            lines.add(player.getNickname()
                    + ": Food " + newFood.get(i) + " (" + signed(foodDelta) + ")"
                    + ", Points " + newPoints.get(i) + " (" + signed(pointsDelta) + ")");
        }
        return lines;
    }

    private boolean isEventCardResolution(String actionInfo) {
        return actionInfo != null && actionInfo.startsWith("La carta evento ");
    }

    private String eventName(MockupGame game, String actionInfo) {
        String lowerLineEventName = lowerLineEventName(game, actionInfo);
        return lowerLineEventName.isBlank() ? eventNameFromActionInfo(actionInfo) : lowerLineEventName;
    }

    private String lowerLineEventName(MockupGame game, String actionInfo) {
        if (game == null || game.getLowerLine() == null) {
            return "";
        }

        List<String> eventNames = new ArrayList<>();
        for (Card card : game.getLowerLine()) {
            if (card instanceof EventCard eventCard) {
                String name = eventCard.simpleToString();
                if (name != null && !name.isBlank()) {
                    eventNames.add(name);
                }
            }
        }

        if (eventNames.isEmpty()) {
            return "";
        }
        if (actionInfo != null) {
            for (String name : eventNames) {
                if (actionInfo.contains(name)) {
                    return name;
                }
            }
        }
        return eventNames.get(0);
    }

    private String eventNameFromActionInfo(String actionInfo) {
        String prefix = "La carta evento ";
        if (actionInfo == null || !actionInfo.startsWith(prefix)) {
            return "";
        }
        String rest = actionInfo.substring(prefix.length());
        int activationIndex = rest.indexOf(" si ");
        if (activationIndex >= 0) {
            return rest.substring(0, activationIndex).trim();
        }
        return rest.trim();
    }

    private void addSummary(List<String> summaries, MockupPlayer player, int newFood, int newPoints) {
        int foodDelta = newFood - player.getFood();
        int pointsDelta = newPoints - player.getPoints();
        if (foodDelta == 0 && pointsDelta == 0) {
            return;
        }
        summaries.add(player.getNickname()
                + ": Food " + signed(foodDelta) + " (" + player.getFood() + " -> " + newFood + ")"
                + ", Points " + signed(pointsDelta) + " (" + player.getPoints() + " -> " + newPoints + ")");
    }

    private String signed(int value) {
        return value > 0 ? "+" + value : String.valueOf(value);
    }

    public record UpdateResult(
            List<Class<? extends UpdateModelElement>> changedElements,
            List<String> actionInfos,
            List<String> foodAndPointSummaries,
            List<String> eventResolutionSummaries
    ) {
    }
}
