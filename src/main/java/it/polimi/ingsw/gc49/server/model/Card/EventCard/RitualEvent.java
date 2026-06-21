package it.polimi.ingsw.gc49.server.model.Card.EventCard;

import it.polimi.ingsw.gc49.client.view.ItaEngRectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.UPDATE_MODEL.ModelElement.Players.FoodAndPointsAllModelElement;
import it.polimi.ingsw.gc49.server.model.*;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStringBuilder;
import org.jline.utils.AttributedStyle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents the Ritual Event card in the game.
 * <p>
 * The player with the most icons in their tribe
 * gains the indicated Prestige Points.
 * The player with the fewest icons in their tribe
 * loses the indicated Prestige Points.
 * In the event of a tie, all tied players
 * gain or lose the indicated Prestige Points.
 * Note: In the rare event of a tie among all players, everyone first
 * gains PP and then loses PP. This is important if any
 * Building cards are in play.
 */
public class RitualEvent extends EventCard {

    /** The amount of bonus points awarded to the winner(s) of the ritual. */
    private final int plusPoints;

    /** The amount of point penalties inflicted on the loser(s) of the ritual. */
    private final int minusPoints;

    /**
     * Constructs a new {@code RitualEvent} card.
     *
     * @param plusPoints   the points awarded to the players with the most stars
     * @param minusPoints  the point penalty for the players with the fewest stars
     * @param eventManager the {@link EventManager} used to trigger related building effects
     * @param era          the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card in the deck
     */
       public RitualEvent( int plusPoints, int minusPoints, EventManager eventManager, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(eventManager, era, minNumPlayers, queueUpdater);
        this.plusPoints = plusPoints;
        this.minusPoints = minusPoints;
    }

    /**
     * Resolves the logic and applies the effects of the Shamanic Ritual event to all players.
     * <p>
     * This execution follows a specific sequence:
     * <ol>
     * <li>Identifies the losers and assigns them a point penalty ({@link #minusPoints}).</li>
     * <li>Identifies the winners and sets their pending reward (a negative {@link #plusPoints} value).</li>
     * <li>Checks if there is a solitary winner and sets the unique winner flag accordingly
     * (crucial for cards like {@code DoubleShamanPointsCard}).</li>
     * <li>Invokes {@link BuildingEvent#RITUAL_EVENT} to allow defensive or rewarding buildings to activate.</li>
     * <li>Finalizes the changes by calling {@link Player#confirmToPay()} on all players.</li>
     * </ol>
     *
     * @param players the list of {@link Player}s participating in the event
     */
    @Override
    public void resolveEvent(List<Player> players) {
        List<Player> losers = determineLosers(players);
        for(Player player : losers) {
            player.setFoodToPay(0);
            player.setPointsToPay(minusPoints);
        }

        List<Player> winners = determineWinners(players);
        for(Player player : winners) {
            player.setFoodToPay(0);
             player.setPointsToPay(-plusPoints);
        }

        // If there is only one winner then set the flag to true for effect num 7
        if(winners.size() == 1) {
             winners.getFirst().setUniqueWinner(true);
        }

        // Effect num 3 and 7
        eventManager.invokeEvent(BuildingEvent.RITUAL_EVENT);

        for(Player player : players) {
            player.confirmToPay();
        }

        if(queueUpdater != null) {
            queueUpdater.queueUpdateModelElement(new FoodAndPointsAllModelElement(
                    "La carta evento " + simpleToString() + " si è attivata fornendo "
                            + plusPoints + " punti al primario di stelle, -" + minusPoints + " all'ultimo",
                    FoodAndPointsAllModelElement.getNewFood(players),
                    FoodAndPointsAllModelElement.getNewPoints(players)
            ));
        }
    }

    /**
     * Determines which players have the highest number of shamanic stars.
     *
     * @param players the list of participating players
     * @return a {@link List} containing the player(s) tied for the highest star count
     */
    private List<Player> determineWinners(List<Player> players) {
        int maxScore = players.stream()
                .mapToInt(p -> p.data.getNumStars())
                .max()
                .getAsInt();
        return players.stream()
                .filter(p -> p.data.getNumStars() == maxScore)
                .collect(Collectors.toList());
    }

    /**
     * Determines which players have the lowest number of shamanic stars.
     *
     * @param players the list of participating players
     * @return a {@link List} containing the player(s) tied for the lowest star count
     */
    private List<Player> determineLosers(List<Player> players) {
        int minScore = players.stream()
                .mapToInt(p -> p.data.getNumStars())
                .min()
                .getAsInt();
        return players.stream()
                .filter(p -> p.data.getNumStars()  == minScore)
                .collect(Collectors.toList());
    }

    /**
     * Provides a detailed string representation of the event card, including its stats.
     *
     * @return a multi-line {@link String} showing the era, winning points, and losing points
     */
    @Override
    public String toString() {
        return "Shamanic ritual {\n" +
                " era = " + era +
                ", plusPoints = " + plusPoints +
                ", minusPoints = " + minusPoints +
                "\n}";
    }

    /**
     * Provides a localized, concise name for this event card.
     *
     * @return a simple one-line {@link String} ("RITUALE SCIAMANICO")
     */
    @Override
    public String simpleToString () {
        return "RITUALE SCIAMANICO";
    }

    /**
     * Generates a visually formatted ASCII-art representation of the card for the terminal UI.
     * <p>
     * The generated drawing has a fixed height of 4 and a width of 5. It uses a magenta
     * border to visually distinguish Event cards, and includes the specific shamanic
     * ritual symbol (│§│).
     *
     * @return a {@link RectangleAttributedString} containing the colored terminal UI graphics
     */
    @Override
    public RectangleAttributedString getRectangleAttributedString () {
        AttributedString attributedString = new AttributedStringBuilder()
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("╔═══╗")
                .append("║")
                .style(AttributedStyle.DEFAULT).append("│§│")
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
