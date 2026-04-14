package it.polimi.ingsw.gc49.View.mockupModel;

import it.polimi.ingsw.gc49.model.Card.Card;
import it.polimi.ingsw.gc49.model.Totem;

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

    /**
     *
     * @param nickname the player's nickname;
     * @param playerIndex this player index in the model's array of players;
     */
    public MockupPlayer ( String nickname, int playerIndex, int food, int points ) {
        this.nickname = nickname;
        this.playerIndex = playerIndex;

        this.food = food;
        this.points = points;
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

    //### adders
    public void addCharacterCard ( Card card ) {
        characterCards.add(card);
    }
    public void addBuildingCard ( Card card ) {
        buildingCards.add(card);
    }
}
