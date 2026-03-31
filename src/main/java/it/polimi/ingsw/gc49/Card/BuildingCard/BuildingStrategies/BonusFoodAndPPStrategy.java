package it.polimi.ingsw.gc49.Card.BuildingCard.BuildingStrategies;

import it.polimi.ingsw.gc49.Card.BuildingCard.AbsBuildingStrategy;
import it.polimi.ingsw.gc49.BuildingEvent;
import it.polimi.ingsw.gc49.CharacterType;
import it.polimi.ingsw.gc49.EventManager;
import it.polimi.ingsw.gc49.Player;

// Effect 1, 5, 8, 10, 11, 12, 14
public class BonusFoodAndPPStrategy extends AbsBuildingStrategy {
    CharacterType unit;
    int foodPerUnit;
    int ppPerUnit;
    int constFood;
    int constPP;
    private  boolean initialized = false;

    // BuildingEvent:SETCOMPLETE_EVENT, SAMEINVENTION_EVENT, HUNTING_EVENT, PAINTING_EVENT,GAMA_END
    public BonusFoodAndPPStrategy(BuildingEvent event) {
        super(event);
    }

    // this method is universal for seven effects implementations
    @Override
    public void effect(Player player) {
        int unitCount = player.data.getCharacterCount(unit);
        // this conditional is for effect num 1 where we need to compare how many sets of character cards
        // are completed SINCE the acquisition of the card,getCurrentNumCompleteCharacterSets() returns the
        // number of complete sets at the acquisition
        if(event == BuildingEvent.SETCOMPLETE_EVENT) {
            unitCount -= player.data.getCurrentNumCompleteCharacterSets();
            player.data.addCurrentNumCompleteCharacterSets(unitCount);
        }
        player.addFood(foodPerUnit*unitCount+constFood);
        player.addPoints(ppPerUnit*unitCount+constPP);
    }

    @Override
    protected boolean condition(Player player) {
        return true;
    }

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
