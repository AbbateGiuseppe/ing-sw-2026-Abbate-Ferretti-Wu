package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

public class BonusFoodAndPPStrategy extends AbsBuildingStrategy {
    CharacterType unit;
    int foodPerUnit;
    int ppPerUnit;
    int constFood;
    int constPP;

    public BonusFoodAndPPStrategy(BuildingEvent event, EventManager manager) {
        super(event, manager);
    }

    @Override
    public void effect(Player player) {
        int unitCount = player.data.getCharacterCount(unit);

        player.addFood(foodPerUnit*unitCount+constFood);
        player.addPoints(ppPerUnit*unitCount+constPP);
    }

    @Override
    protected boolean condition(Player player) {
        return true;
    }

    private  boolean initialized = false;

    public void setConst(int pp,int food){
        if(initialized)return;
        unit = CharacterType.None;
        constFood = food;
        constPP = pp;
        initialized = true;
    }
    public void setByUnit(CharacterType type,int pp,int food){
        if(initialized)return;
        foodPerUnit = food;
        ppPerUnit = pp;
        initialized = true;
    }

}
