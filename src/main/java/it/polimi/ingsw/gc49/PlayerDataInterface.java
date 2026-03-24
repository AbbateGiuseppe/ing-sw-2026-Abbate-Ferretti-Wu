package it.polimi.ingsw.gc49;

import it.polimi.ingsw.gc49.Card.TribeCards.CharacterCard.CharacterType;

//All I need to safely get data
public interface PlayerDataInterface {

    /**
     *
     * @param type the type of card unit to be counted, can also be a chara set
     * @return the counted number of card, pair, or set of chara cards
     */
    public int GetCharaCount(CharacterType type);

    public int GetBuildingDiscount();

    boolean IsGettingBonusFood();

    void addFood(int i);

    void addPoints(int i);

    void setupToPay(int food, int pp);

    int GetBuilderPP();

    int getReward();

}
