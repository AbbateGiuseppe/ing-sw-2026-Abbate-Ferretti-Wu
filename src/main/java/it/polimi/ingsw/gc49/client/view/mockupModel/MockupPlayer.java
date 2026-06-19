package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Totem;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.Serializable;
import java.util.ArrayList;
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

    /** set by the mockupGame at every change, it's used to highlight to the user this player if it's of turn*/
    private boolean ofTurn = false;

    /**
     *
     * @param nickname the player's nickname;
     * @param playerIndex this player index in the model's array of players;
     */
    public MockupPlayer ( String nickname, int playerIndex, int food, int points, Totem totem ) {
        this.nickname = nickname;
        this.playerIndex = playerIndex;

        this.food = food;
        this.points = points;
        this.totem = totem;
        characterCards = new ArrayList<>();
        buildingCards = new ArrayList<>();

        connected = true;

        drawableUpper = 0;
        drawableLower = 0;
    }

    //### setters
    public void setFood (int food) {
        this.food = food;
    }
    public void setPoints (int points) {
        this.points = points;
    }
    public void setDrawableUpper (int drawableUpper) {
        this.drawableUpper = drawableUpper;
    }
    public void setDrawableLower (int drawableLower) {
        this.drawableLower = drawableLower;
    }
    public void setTotem (Totem totem) {
        this.totem = totem;
    }
    public void setConnected (boolean connected) {
        this.connected = connected;
    }

    //### getters
    public String getNickname () {
        return nickname;
    }
    public int getPlayerIndex () {
        return playerIndex;
    }
    public Totem getTotem() {
        return totem;
    }
    public int getFood () {
        return food;
    }
    public int getPoints () {
        return points;
    }
    public int getDrawableUpper () {
        return drawableUpper;
    }
    public int getDrawableLower () {
        return drawableLower;
    }
    public boolean isConnected () {
        return connected;
    }
    public boolean isOfTurn () {
        return ofTurn;
    }

    //### adders
    public void addCharacterCard ( Card card ) {
        characterCards.add(card);
    }
    public void addBuildingCard ( Card card ) {
        buildingCards.add(card);
    }

    public AttributedString AllToAttributedString () {
        AttributedStringBuilder stringBuilder = new AttributedStringBuilder();
        if(!characterCards.isEmpty() || !buildingCards.isEmpty()) {
            stringBuilder.append(nickname).append(": \n");

            // Guarda toString() delle carte
            for (Card c : characterCards) {
                stringBuilder.append(c.toString());
            }

            for (Card c : buildingCards) {
                stringBuilder.append(c.toString());
            }
        }else{
            stringBuilder.append(nickname).append(" has no cards.");
        }
        return stringBuilder.toAttributedString();
    }
    public AttributedString displayAttributedStringName() {
        if(totem != null) {
            if(ofTurn){
                return new AttributedStringBuilder().append(totem.getTotemAttributedString())
                        .append("[").style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED))
                        .append(nickname).style(AttributedStyle.DEFAULT)
                        .append("]")
                        .toAttributedString();
            } else if (connected){
                return new AttributedStringBuilder().append(totem.getTotemAttributedString())
                        .append("[").style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW))
                        .append(nickname).style(AttributedStyle.DEFAULT)
                        .append("]")
                        .toAttributedString();
            } else {
                return new AttributedStringBuilder().append(totem.getTotemAttributedString())
                        .append("[").style(AttributedStyle.BOLD.foreground(AttributedStyle.BLACK))
                        .append(nickname).style(AttributedStyle.DEFAULT)
                        .append("]")
                        .toAttributedString();
            }
        }else{
            if(ofTurn){
                return new AttributedStringBuilder().append("░")
                        .append("[").style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED))
                        .append(nickname).style(AttributedStyle.DEFAULT)
                        .append("]")
                        .toAttributedString();
            } else if (connected) {
                return new AttributedStringBuilder().append("░")
                        .append("[").style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW))
                        .append(nickname).style(AttributedStyle.DEFAULT)
                        .append("]")
                        .toAttributedString();
            } else {
                return new AttributedStringBuilder().append("░")
                        .append("[").style(AttributedStyle.BOLD.foreground(AttributedStyle.BLACK))
                        .append(nickname).style(AttributedStyle.DEFAULT)
                        .append("]")
                        .toAttributedString();
            }

        }
    }
    public AttributedString displayAttributedStringStats() {
        return new AttributedStringBuilder()
                .append(String.valueOf(food)).append("♥/").append(String.valueOf(points)).append("♦")
                .toAttributedString();
    }

    @Override
    public String toString() {
        return playerIndex + ": " + nickname +
                " (food=" + food +
                ", points=" + points + ")";
    }

    public void setOfTurn ( boolean ofTurn ) {
        this.ofTurn = ofTurn;
    }
}
