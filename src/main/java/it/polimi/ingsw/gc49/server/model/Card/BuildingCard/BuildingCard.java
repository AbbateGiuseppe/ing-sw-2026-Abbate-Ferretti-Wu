package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.*;
import javafx.util.Pair;

import it.polimi.ingsw.gc49.server.model.Card.Card;

import java.io.Serializable;


/**
 * Represents an abstract base class for all Building Cards in the game.
 * <p>
 * Building cards can be acquired by players by paying a specific food cost.
 * Once acquired, they provide endgame points and act as listeners ({@link BuildingEventListener})
 * that react to specific {@link BuildingEvent}s triggered during the game.
 */

public abstract class BuildingCard extends Card implements BuildingEventListener, Serializable {
    /** The specific event type that triggers this building's special effect. */
    private final BuildingEvent buildingEvent;

    /** The amount of points this building awards to its owner at the END of the game. */
    protected final int pointsEndgame;

    /** The base amount of food required to acquire this building. */
    protected final int foodPrice;

    /** The {@link Player} who currently owns this building. */
    protected Player owner;


    /**
     * Constructs a new {@code BuildingCard}.
     *
     * @param buildingEvent the {@link BuildingEvent} this building reacts to
     * @param pointsEndgame the points awarded at the end of the game
     * @param foodPrice     the base food cost to acquire the card
     * @param era           the {@link Era} this card belongs to
     * @param minNumPlayers the minimum number of players required to include this card
     */
    public BuildingCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers, QueueUpdatable queueUpdater ) {
        super(era, minNumPlayers, queueUpdater);
        this.buildingEvent = buildingEvent;
        this.pointsEndgame = pointsEndgame;
        this.foodPrice = foodPrice;
    }

    /**
     * Checks whether the specified player has enough food to acquire this building.
     * <p>
     * The evaluation takes into account any building discounts the player might have
     * in their databank ({@code player.data.getNumBuildingDiscount()}).
     *
     * @param player the {@link Player} attempting to acquire the card
     * @return {@code true} if the player's food is greater than or equal to the eventually discounted price, {@code false} otherwise
     */
    @Override
    public boolean canGet(Player player) {
        if (player.getFood() >= foodPrice - player.data.getNumBuildingDiscount()) {return true;}
        return false;
    }


    /**
     * Updates the player's databank by adding the endgame points provided by this building.
     *
     * @param dataBank the {@link DataBank} of the player drawing the card
     */
    @Override
    public void updateDataBank( DataBank dataBank) {
        dataBank.addNumBuildingPoints(pointsEndgame);
    }


    /**
     * Handles the logic upon drawing the card, charging the player and setting the ownership.
     * <p>
     * The method calculates the final discounted price and subtracts it from the player's food.
     * It uses {@link Math#min(int, int)} to ensure that a discount larger than the price
     * does not accidentally reward the player with extra food.
     *
     * @param player the {@link Player} acquiring the card
     */
    @Override
    public void onDraw( Player player ) {
        player.addFood(Math.min(0,- foodPrice + player.data.getNumBuildingDiscount()));
        setOwner(player);
    }

    /**
     * Assigns this building to a specific player.
     *
     * @param owner the {@link Player} who now owns the card
     */
    protected void setOwner(Player owner) {
        this.owner = owner;
    }

    /**
     * Subscribes this building to the provided {@link EventManager}.
     * <p>
     * This allows the building to "listen" and react whenever its corresponding
     * {@link BuildingEvent} is triggered during the game.
     *
     * @param owner   the {@link Player} who owns this building
     * @param manager the central {@link EventManager} of the game
     */
    public void addBuildingToManager ( Player owner, EventManager manager ) {
        manager.addEventListener(buildingEvent, new Pair<Player, BuildingEventListener>(owner, this));
    }

    /**
     * Retrieves the current owner of this building.
     *
     * @return the {@link Player} who owns the card, or {@code null} if it has not been acquired yet
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Retrieves the base food price of this building.
     *
     * @return the base food cost
     */
    public int getFoodPrice() {
        return foodPrice;
    }


    /**
     * Determines if this card can be placed on the lower line during the game setup.
     * <p>
     * Building cards can always be placed on the lower line during setup.
     *
     * @return always {@code true}
     */
    @Override
    public boolean isLowerLineOnSetup () {
        return true;
    }
}
