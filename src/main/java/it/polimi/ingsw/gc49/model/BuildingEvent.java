package it.polimi.ingsw.gc49.model;

// The moments in which the listeners are triggered
public enum BuildingEvent {
    HUNTING_EVENT,      //called by the hunting event card
    PAINTING_EVENT,     //called by the painting event card
    RITUAL_EVENT,       //called by the ritual event card
    SUSTENANCE_EVENT,   //called by the sustenance event card
    DRAW_EVENT,         //called after a player has drawn a card.
    TURN_END,           //called at every player's turn's end.
    ROUND_END,          //called at the round's end.
    GAME_END,           //called during the end of the game, where end game points are calculated
}