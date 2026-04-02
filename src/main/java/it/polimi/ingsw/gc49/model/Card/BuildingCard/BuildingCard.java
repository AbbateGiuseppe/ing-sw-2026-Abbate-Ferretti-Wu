package it.polimi.ingsw.gc49.model.Card.BuildingCard;

import it.polimi.ingsw.gc49.model.*;
import it.polimi.ingsw.gc49.model.Card.Card;

public abstract class BuildingCard extends Card implements BuildingEventListener {
    private final BuildingEvent buildingEvent;
    private final int pointsEndgame;
    private final int foodPrice;
    protected Player owner;

    public BuildingCard ( BuildingEvent buildingEvent, int pointsEndgame, int foodPrice, Era era, int minNumPlayers ) {
        super(era, minNumPlayers);
        this.buildingEvent = buildingEvent;
        this.pointsEndgame = pointsEndgame;
        this.foodPrice = foodPrice;
    }

    // TODO:create the building cards with their strategies
//    public static List<BuildingCard> getAllBuildingCards(EventManager manager){
//        List<BuildingCard> list = new ArrayList<>();
//        // Setup the strategy of the card
//        BonusFoodAndPPStrategy strategy = new BonusFoodAndPPStrategy(BuildingEvent.HUNTING_EVENT);
//        strategy.setByUnit(CharacterType.Hunter,1,1);
//        // Create the card
//        BuildingCard card = new BuildingCard(strategy,BuildingEvent.HUNTING_EVENT,manager,0,0);
//        list.add(card);
//
//        return list;
//    }

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

    public void addBuildingToManager ( EventManager manager) {
        manager.addEventListener(buildingEvent,this);
    }

    public Player getOwner() {
        return owner;
    }

    public int getFoodPrice() {
        return foodPrice;
    }
}
