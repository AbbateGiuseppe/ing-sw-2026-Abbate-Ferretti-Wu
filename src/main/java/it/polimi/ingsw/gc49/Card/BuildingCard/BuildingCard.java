package it.polimi.ingsw.gc49.Card.BuildingCard;

import it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies.BonusFoodAndPPStrategy;
import it.polimi.ingsw.gc49.Card.Card;
import it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard.CharacterType;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;
import it.polimi.ingsw.gc49.Totem;

import java.util.ArrayList;
import java.util.List;

public class BuildingCard extends Card implements BuildingEventListener{
    public static List<BuildingCard> getAllBuildingCards(EventManager manager){
        List<BuildingCard> list = new ArrayList<>();
        //TODO all initialization,the following is one example
        BonusFoodAndPPStrategy strategy = new BonusFoodAndPPStrategy(BuildingEvent.HUNTING_EVENT,manager);
        strategy.setByUnit(CharacterType.Hunter,1,1);

        BuildingCard card = new BuildingCard(strategy,0,0);
        list.add(card);
        return list;
    }

    private final BuildingStrategyInterface strategy;
    private final int PPReward;
    private final int foodPrice;
    private Player owner;

    public BuildingCard(BuildingStrategyInterface strategy, int PPReward, int foodPrice) {
        this.strategy = strategy;
        this.PPReward = PPReward;
        this.foodPrice = foodPrice;
    }

    @Override
    public boolean canGet(Player player) {
        return (player.getFood()-player.GetBuildingDiscount()) >= foodPrice;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
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
