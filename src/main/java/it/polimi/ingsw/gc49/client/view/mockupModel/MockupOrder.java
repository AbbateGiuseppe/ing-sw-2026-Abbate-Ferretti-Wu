package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.client.view.Rectangable;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import org.jline.utils.AttributedStringBuilder;

import java.io.Serializable;

public class MockupOrder implements Serializable, Rectangable {
    private MockupGame game;
    private Integer assignedPlayerIndex;
    private final int foodGain;
    private final boolean payFood;
    private final int foodToPay;
    private final int removedPointsOnStarvation;

    public MockupOrder(int foodGain, boolean payFood, int foodToPay, int removedPointsOnStarvation, Integer assignedPlayerIndex) {
        this.foodGain = foodGain;
        this.payFood = payFood;
        this.foodToPay = foodToPay;
        this.removedPointsOnStarvation = removedPointsOnStarvation;
        this.assignedPlayerIndex = assignedPlayerIndex;
    }

    public void setAssignedPlayerIndex(Integer assignedPlayerIndex) {
        this.assignedPlayerIndex = assignedPlayerIndex;
    }

    public void setGame(MockupGame game) {
        this.game = game;
    }

    public Integer getAssignedPlayerIndex() {
        return assignedPlayerIndex;
    }

    public int getFoodGain() {
        return foodGain;
    }

    public boolean isPayFood() {
        return payFood;
    }

    public int getFoodToPay() {
        return foodToPay;
    }

    public int getRemovedPointsOnStarvation() {
        return removedPointsOnStarvation;
    }

    @Override
    public RectangleAttributedString getRectangleAttributedString() {
        String owner = assignedPlayerIndex == null ? "-" : game.getPlayer(assignedPlayerIndex).getNickname();
        String text = "order owner=" + owner;
        return new RectangleAttributedString(1, text.length(), new AttributedStringBuilder().append(text).toAttributedString());
    }
}
