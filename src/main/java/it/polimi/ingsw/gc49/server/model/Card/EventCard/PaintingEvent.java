package it.polimi.ingsw.gc49.server.model.Card.EventCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsAllModelElement;
import it.polimi.ingsw.gc49.server.model.*;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.List;

/**
 * Represents the Painting Event card in the game (Pitture Rupestri).
 * <p>
 * To earn points with this Event, you must
 * have a minimum number of Artists, as indicated
 * on the Event card.
 * If you have the number of Artists indicated on the
 * top row of the Event card, you lose the indicated
 * Prestige Points.
 */
public class PaintingEvent extends EventCard {
    /** The minimum number of Artist cards required to gain points instead of losing them. */
    private final int threshold;

    /** The amount of points awarded per Artist if the threshold is met. */
    private final int plusPoints;

    /** The flat point penalty inflicted if the player has fewer Artists than the threshold. */
    private final int minusPoints;

    /**
     * Constructs a new {@code PaintingEvent} card.
     *
     * @param threshold    the minimum number of Artists needed for a successful event
     * @param plusPoints   the points awarded per Artist upon success
     * @param minusPoints  the point penalty upon failure
     * @param eventManager the {@link EventManager} used to trigger related building effects
     * @param era          the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card in the deck
     */
    public PaintingEvent( int threshold, int plusPoints, int minusPoints, EventManager eventManager, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(eventManager, era, minNumPlayers, queueUpdater);
        this.threshold = threshold;
        this.plusPoints = plusPoints;
        this.minusPoints = minusPoints;
    }

    /**
     * Resolves the logic and applies the effects of the Painting event to all players.
     * <p>
     * This execution evaluates each player's databank against the {@link #threshold}:
     * <ul>
     * <li>If the player's Artist count is below the threshold, they are assigned a penalty
     * equal to {@link #minusPoints}.</li>
     * <li>If the player's Artist count meets or exceeds the threshold, they are assigned a
     * reward (negative penalty value) equal to {@link #plusPoints} multiplied by their Artist count.</li>
     * </ul>
     * After evaluating the base event, it invokes {@link BuildingEvent#PAINTING_EVENT} to allow
     * buildings (like the BonusPaintingCard) to apply further modifiers, and finally confirms
     * the transactions for all players.
     *
     * @param players the list of {@link Player}s participating in the event
     */
    @Override
    public void resolveEvent(List<Player> players) {
        for(Player player : players) {
            player.setFoodToPay(0);
            if (player.data.getCharacterCount(CharacterType.Artist) < threshold) {
                player.setPointsToPay(minusPoints);
            } else {
                player.setPointsToPay(-plusPoints * player.data.getCharacterCount(CharacterType.Artist));
            }
        }
        // Effect num 10
        eventManager.invokeEvent(BuildingEvent.PAINTING_EVENT);

        for(Player player : players) {
            player.confirmToPay();
        }

        if(queueUpdater != null) {
            queueUpdater.queueUpdateModelElement(new FoodAndPointsAllModelElement(
                    "La carta evento " + simpleToString() + " si è attivata fornendo "
                            + plusPoints + " punti per artista, a chi possiede almeno " + (threshold+1) + " artisti, altrimenti -" + minusPoints,
                    FoodAndPointsAllModelElement.getNewFood(players),
                    FoodAndPointsAllModelElement.getNewPoints(players)
            ));
        }
    }


    /**
     * Provides a detailed string representation of the event card, including its stats.
     *
     * @return a multi-line {@link String} showing the era, threshold, reward multiplier, and penalty
     */
    @Override
    public String toString() {
        return "Paintings {\n" +
                " era = " + era +
                ", threshold = " + threshold +
                ", plusPoints = " + plusPoints +
                ", minusPoints = " + minusPoints +
                "\n}";
    }

    /**
     * Provides a localized, concise name for this event card.
     *
     * @return a simple one-line {@link String} ("PITTURE RUPESTRI")
     */
    @Override
    public String simpleToString () {
        return "PITTURE RUPESTRI";
    }


    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 4 and a width of 5. It uses a magenta
     * border to visually distinguish Event cards, and includes the specific painting
     * symbol (│¤│).
     *
     * @return a {@link RectangleAttributedString} containing the colored terminal UI graphics
     */
    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("│¤│")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("║")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("└─┘")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("║")
                .append("╚═══╝").toAttributedString();
        int height = 4;
        int width = 5;
        return new RectangleAttributedString(height, width, attributedString);
    }

    @Override
    public ItaEngRectangleAttributedString getItaEngRectangleAttributedString () {
        RectangleAttributedString globalRectangle = getRectangleAttributedString();
        return new ItaEngRectangleAttributedString(
                globalRectangle.height, globalRectangle.width, globalRectangle.attributedString,
                globalRectangle.height, globalRectangle.width, globalRectangle.attributedString);
    }
}
