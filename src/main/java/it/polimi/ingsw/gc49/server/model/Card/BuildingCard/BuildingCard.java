package it.polimi.ingsw.gc49.server.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.server.model.*;
import javafx.util.Pair;

import it.polimi.ingsw.gc49.server.model.Card.Card;

public abstract class BuildingCard extends Card implements BuildingEventListener {
    private final BuildingEvent buildingEvent;
    protected final int pointsEndgame;
    protected final int foodPrice;
    protected Player owner;

    public BuildingCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
        this.buildingEvent = buildingEvent;
        this.pointsEndgame = pointsEndgame;
        this.foodPrice = foodPrice;
    }

    @Override
    public boolean canGet(Player player) {
        if (player.getFood() >= foodPrice - player.data.getNumBuildingDiscount()) {return true;}
        return false;
    }

    @Override
    public void updateDataBank( DataBank dataBank) {
        dataBank.addNumBuildingPoints(pointsEndgame);
    }

    @Override
    public void onDraw( Player player ) {
        // Subtract the food price from the food of the player
        player.addFood(Math.min(0,- foodPrice + player.data.getNumBuildingDiscount()));
        setOwner(player);
    }

    protected void setOwner(Player owner) {
        this.owner = owner;
        // Put the card to listen for its buildingEvent
    }

    public void addBuildingToManager ( Player owner, EventManager manager ) {
        manager.addEventListener(buildingEvent, new Pair<Player, BuildingEventListener>(owner, this));
    }

    public Player getOwner() {
        return owner;
    }

    public int getFoodPrice() {
        return foodPrice;
    }
}
