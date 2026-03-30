package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.*;
import it.polimi.ingsw.gc49.Card.Card;

public abstract class BuildingCard extends Card implements BuildingEventListener{
    private final BuildingEvent buildingEvent;
    private final EventManager manager;
    private final int PPReward;
    private final int foodPrice;
    protected Player owner;

    public BuildingCard(BuildingEvent buildingEvent, EventManager manager, int PPReward, int foodPrice) {
        this.buildingEvent = buildingEvent;
        this.manager = manager;
        this.PPReward = PPReward;
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
    public void updateDataBank(DataBank dataBank) {
        dataBank.addNumBuildingPoints(PPReward);
    }

    @Override
    public void onDraw( Player player ) {
        // Subtract the food price from the food of the player
        player.addFood(Math.min(0,- foodPrice + player.data.getNumBuildingDiscount()));
        setOwner(player);
    }

    private void setOwner(Player owner) {
        this.owner = owner;
        if (buildingEvent == BuildingEvent.SETCOMPLETE_EVENT) {
            // Record the current number of complete character card sets for future comparision
            owner.data.recordCharaSet();
        } else if (buildingEvent == BuildingEvent.SAMEINVENTION_EVENT) {
            // Record the current number of the same pair inventions for future comparision
            owner.data.recordInventions();
        }
        // Put the card to listen for its buildingEvent
        manager.addEventListener(buildingEvent,this);
    }

    public Player getOwner() {
        return owner;
    }

    public int getPPReward() {
        return PPReward;
    }

    public int getFoodPrice() {
        return foodPrice;
    }
}
