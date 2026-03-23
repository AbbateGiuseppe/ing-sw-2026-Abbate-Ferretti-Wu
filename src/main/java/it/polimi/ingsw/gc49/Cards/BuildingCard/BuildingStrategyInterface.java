package it.polimi.ingsw.gc49.Cards.BuildingCard;

import it.polimi.ingsw.gc49.PlayerDataInterface;

public interface BuildingStrategyInterface {
    /**
     *
     * @param player the player to be modified by the card effect, usually the owner of the building card
     */
    public void activateEffect(PlayerDataInterface player);
}
