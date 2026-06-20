package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Totem;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MockupPlayer implements Serializable {
    private final String nickname;
    private final int playerIndex;
    private boolean connected;
    private Totem totem;
    private int food;
    private int points;
    private final List<Card> characterCards;
    private final List<Card> buildingCards;
    private int drawableUpper;
    private int drawableLower;
    private boolean ofTurn = false;

    public MockupPlayer(String nickname, int playerIndex, int food, int points, Totem totem) {
        this.nickname = nickname;
        this.playerIndex = playerIndex;
        this.food = food;
        this.points = points;
        this.totem = totem;
        this.characterCards = new ArrayList<>();
        this.buildingCards = new ArrayList<>();
        this.connected = true;
        this.drawableUpper = 0;
        this.drawableLower = 0;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public void setDrawableUpper(int drawableUpper) {
        this.drawableUpper = drawableUpper;
    }

    public void setDrawableLower(int drawableLower) {
        this.drawableLower = drawableLower;
    }

    public void setTotem(Totem totem) {
        this.totem = totem;
    }

    public void setConnected(boolean connected) {
        this.connected = connected;
    }

    public void setOfTurn(boolean ofTurn) {
        this.ofTurn = ofTurn;
    }

    public String getNickname() {
        return nickname;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public Totem getTotem() {
        return totem;
    }

    public int getFood() {
        return food;
    }

    public int getPoints() {
        return points;
    }

    public int getDrawableUpper() {
        return drawableUpper;
    }

    public int getDrawableLower() {
        return drawableLower;
    }

    public boolean isConnected() {
        return connected;
    }

    public boolean isOfTurn() {
        return ofTurn;
    }

    public List<Card> getCharacterCards() {
        return Collections.unmodifiableList(characterCards);
    }

    public List<Card> getBuildingCards() {
        return Collections.unmodifiableList(buildingCards);
    }

    public void addCharacterCard(Card card) {
        characterCards.add(card);
    }

    public void addBuildingCard(Card card) {
        buildingCards.add(card);
    }

    public AttributedString AllToAttributedString() {
        AttributedStringBuilder stringBuilder = new AttributedStringBuilder();
        if (!characterCards.isEmpty() || !buildingCards.isEmpty()) {
            stringBuilder.append(nickname).append(": \n");
            for (Card card : characterCards) {
                stringBuilder.append(card.toString()).append("\n");
            }
            for (Card card : buildingCards) {
                stringBuilder.append(card.toString()).append("\n");
            }
        } else {
            stringBuilder.append(nickname).append(" has no cards.");
        }
        return stringBuilder.toAttributedString();
    }

    public AttributedString displayAttributedStringName() {
        AttributedStringBuilder builder = new AttributedStringBuilder();
        if (totem != null) {
            builder.append(totem.getTotemAttributedString());
        } else {
            builder.append("#");
        }
        builder.append("[")
                .style(AttributedStyle.DEFAULT.foreground(ofTurn ? AttributedStyle.RED : AttributedStyle.YELLOW))
                .append(nickname)
                .style(AttributedStyle.DEFAULT)
                .append("]");
        return builder.toAttributedString();
    }

    public AttributedString displayAttributedStringStats() {
        return new AttributedStringBuilder()
                .append(String.valueOf(food))
                .append(" food ")
                .append(String.valueOf(points))
                .append(" pts")
                .toAttributedString();
    }

    @Override
    public String toString() {
        return playerIndex + ": " + nickname +
                " (food=" + food +
                ", points=" + points + ")";
    }
}
