package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.*;
import it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies.BonusFoodAndPPStrategy;
import it.polimi.ingsw.gc49.Card.Card;

import java.util.ArrayList;
import java.util.List;

public class BuildingCard extends Card implements BuildingEventListener{
    private final BuildingStrategyInterface strategy;
    private final BuildingEvent buildingEvent;
    private final EventManager manager;
    private final int PPReward;
    private final int foodPrice;
    private Player owner;

    public BuildingCard(BuildingStrategyInterface strategy, BuildingEvent buildingEvent, EventManager manager, int PPReward, int foodPrice) {
        this.buildingEvent = buildingEvent;
        this.manager = manager;
        this.strategy = strategy;
        this.PPReward = PPReward;
        this.foodPrice = foodPrice;
    }

    // TODO:create the building cards with their strategies
    public static List<BuildingCard> getAllBuildingCards(EventManager manager){
        List<BuildingCard> list = new ArrayList<>();
        // One example
        BonusFoodAndPPStrategy strategy = new BonusFoodAndPPStrategy(BuildingEvent.HUNTING_EVENT);
        strategy.setByUnit(CharacterType.Hunter,1,1);
        BuildingCard card = new BuildingCard(strategy,BuildingEvent.HUNTING_EVENT,manager,0,0);
        list.add(card);

        return list;
    }

    @Override
    public boolean canGet(Player player) {
        if (player.getFood() >= foodPrice - player.data.getNumBuildingDiscount()) {
            player.addFood(Math.min(0,- foodPrice + player.data.getNumBuildingDiscount()));
            setOwner(player);
            return true;
        } else {return false;}
    }

    private void setOwner(Player owner) {
        this.owner = owner;
        owner.data.addNumBuildingPoints(PPReward);
        if (buildingEvent == BuildingEvent.SETCOMPLETE_EVENT) {
             owner.data.recordCharaSet();
        } else if (buildingEvent == BuildingEvent.SAMEINVENTION_EVENT) {
            owner.data.recordInventions();
        }
        manager.addEventListener(buildingEvent,this);
    }

    public Player getOwner() {
        return owner;
    }

    public BuildingStrategyInterface getStrategy() {
        return strategy;
    }

    public int getPPReward() {
        return PPReward;
    }

    public int getFoodPrice() {
        return foodPrice;
    }

    @Override
    public void onEventEffect(Totem totem) {
        if(isOwner((totem))){
            strategy.activateEffect(owner);
        }
    }

    private boolean isOwner(Totem totem){
        return owner.getTotem() == totem;
    }
}
