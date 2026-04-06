package it.polimi.ingsw.gc49.controller;

import it.polimi.ingsw.gc49.ActionData;
import it.polimi.ingsw.gc49.model.*;
import javafx.scene.control.FocusModel;

import java.util.HashMap;

public class WuController implements ViewListener {
    private Game game;
    @Override
    public void playerAction(ActionData data) {
        switch (data.getAction()){
            case ChooseOffer -> ChooseOffer();//TODO example
        }
    }

    public void SetGame(Game game){
        this.game = game;
    }

    public WuController(){

    }

    private void ChooseOffer(){
        game.chooseOffer(0,0);//TODO example, I guess this is why I wouldn't recommend anyway to use indexes, player index here is just so dirty
    }
}
