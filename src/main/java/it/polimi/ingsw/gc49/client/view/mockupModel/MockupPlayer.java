package it.polimi.ingsw.gc49.client.view.mockupModel;

import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Totem;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;


/**
 * The {@code MockupPlayer} class represents the client-side snapshot of a specific player.
 * It stores all the publicly or locally known information about the player, including
 * their resources (food, points), their assigned totem, their connection status, and
 * the cards they have played (characters and buildings).
 * It also provides utility methods to render this information gracefully on a CLI using JLine.
 */
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

    /** * Flag set by the {@link MockupGame} at every turn change.
     * It's used by the UI to highlight this player when it is their turn to play.
     */
    private boolean ofTurn = false;

    /**
     * Constructs a new {@code MockupPlayer} with initial stats.
     *
     * @param nickname    the player's chosen nickname.
     * @param playerIndex the player's index in the model's array of players.
     * @param food        the starting amount of food.
     * @param points      the starting amount of victory points.
     * @param totem       the {@link Totem} assigned to this player.
     */
    public MockupPlayer ( String nickname, int playerIndex, boolean connected, Totem totem, int food, int points,
                          List<Card> characterCards, List<Card> buildingCards, int drawableUpper, int drawableLower ) {
        this.nickname = nickname;
        this.playerIndex = playerIndex;
        this.connected = connected;

        this.totem = totem;

        this.food = food;
        this.points = points;

        this.characterCards = characterCards;
        this.buildingCards = buildingCards;

        this.drawableUpper = drawableUpper;
        this.drawableLower = drawableLower;
    }

    // ============================================================
    // ### SETTERS
    // ============================================================

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

    /**
     * Sets the turn flag for this player.
     * This is typically managed automatically by {@link MockupGame#setCurrentPlayerIndex(int)}.
     *
     * @param ofTurn {@code true} if it is this player's turn, {@code false} otherwise.
     */
    public void setOfTurn ( boolean ofTurn ) {
        this.ofTurn = ofTurn;
    }

    // ============================================================
    // ### GETTERS
    // ============================================================
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
    public List<Card> getCharacterCards() {
        return Collections.unmodifiableList(characterCards);
    }

    public List<Card> getBuildingCards() {
        return Collections.unmodifiableList(buildingCards);
    }
    public boolean isConnected () {
        return connected;
    }
    public boolean isOfTurn () {
        return ofTurn;
    }

    // ============================================================
    // ### ADDERS
    // ============================================================

    /**
     * Adds a newly acquired character card to the player's personal board.
     *
     * @param card the character {@link Card} to add.
     */
    public void addCharacterCard ( Card card ) {
        characterCards.add(card);
    }

    /**
     * Adds a newly acquired building card to the player's personal board.
     *
     * @param card the building {@link Card} to add.
     */
    public void addBuildingCard ( Card card ) {
        buildingCards.add(card);
    }

    // ============================================================
    // ### RENDERING METHODS
    // ============================================================

    /**
     * Generates a stylized string listing the player's nickname and all their cards.
     *
     * @return an {@link AttributedString} containing the full inventory of the player.
     */

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

    /**
     * Generates a stylized string for the player's name, color-coded based on their status:
     * <ul>
     * <li><b>Red:</b> It is currently this player's turn.</li>
     * <li><b>Yellow:</b> The player is connected and waiting.</li>
     * <li><b>Black (Bold):</b> The player is disconnected.</li>
     * </ul>
     * It also prepends the player's colored totem symbol (or a placeholder if null).
     *
     * @return the formatted {@link AttributedString} for the player's display name.
     */
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

    /**
     * Generates a stylized string displaying the player's current resources (Food and Points).
     * Format example: {@code 3♥/5♦}
     *
     * @return the formatted {@link AttributedString} of the player's stats.
     */
    public AttributedString displayAttributedStringStats() {
        return new AttributedStringBuilder()
                .append(String.valueOf(food)).append("♥/").append(String.valueOf(points)).append("♦")
                .toAttributedString();
    }

    /**
     * Returns a standard plain-text representation of the player's basic stats.
     *
     * @return a string formatted as "Index: Nickname (food=X, points=Y)".
     */
    @Override
    public String toString() {
        return playerIndex + ": " + nickname +
                " (food=" + food +
                ", points=" + points + ")";
    }



}
