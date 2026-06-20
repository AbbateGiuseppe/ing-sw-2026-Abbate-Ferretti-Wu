package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.client.view.Rectangable;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import org.jline.utils.AttributedStringBuilder;

import java.io.Serializable;

public class MockupOffer implements Serializable, Rectangable {
    private MockupGame game;
    private Integer assignedPlayerIndex;
    private final int foodGain;
    private final int upperDraw;
    private final int lowerDraw;

    public MockupOffer(int foodGain, int upperDraw, int lowerDraw, Integer assignedPlayerIndex) {
        this.foodGain = foodGain;
        this.upperDraw = upperDraw;
        this.lowerDraw = lowerDraw;
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

    public int getUpperDraw() {
        return upperDraw;
    }

    public int getLowerDraw() {
        return lowerDraw;
    }

    @Override
    public RectangleAttributedString getRectangleAttributedString() {
        String owner = assignedPlayerIndex == null ? "-" : game.getPlayer(assignedPlayerIndex).getNickname();
        String text = "offer owner=" + owner + " food=" + foodGain + " up=" + upperDraw + " low=" + lowerDraw;
        return new RectangleAttributedString(1, text.length(), new AttributedStringBuilder().append(text).toAttributedString());
    }
}
