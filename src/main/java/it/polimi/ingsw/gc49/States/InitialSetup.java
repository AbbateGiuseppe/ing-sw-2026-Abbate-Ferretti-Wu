package it.polimi.ingsw.gc49.States;

import it.polimi.ingsw.gc49.Locks;
import it.polimi.ingsw.gc49.Model;
import it.polimi.ingsw.gc49.Player;
import it.polimi.ingsw.gc49.Track;

public class InitialSetup extends State {

    public InitialSetup ( Model model ) {
        super(model);
    }

    public State executeState () {
        int numOfPlayers = model.getNumOfPlayers();
        Player[] players = new Player[numOfPlayers];
        model.setPlayers(players);

        String[] playersNicknames = model.getPlayersNicknames();
        switch(numOfPlayers){
            case 2:
                players[0] = new Player(playersNicknames[0], 0);
                players[1] = new Player(playersNicknames[1], 1);
                break;
            case 3:
                players[0] = new Player(playersNicknames[0], 0);
                players[1] = new Player(playersNicknames[1], 1);
                players[2] = new Player(playersNicknames[2], 2);
                break;
            case 4:
                players[0] = new Player(playersNicknames[0], 0);
                players[1] = new Player(playersNicknames[1], 1);
                players[2] = new Player(playersNicknames[2], 2);
                players[3] = new Player(playersNicknames[3], 3);
                break;
            case 5:
                players[0] = new Player(playersNicknames[0], 0);
                players[1] = new Player(playersNicknames[1], 1);
                players[2] = new Player(playersNicknames[2], 2);
                players[3] = new Player(playersNicknames[3], 3);
                players[4] = new Player(playersNicknames[4], 4);
                break;
        }

        model.setTrack(new Track(numOfPlayers));

        while (model.getUsedTotems().size() < numOfPlayers) { //waits until every player has chosen a totem.
            try {
                wait();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        return new OfferChoosing(model); //goes to the offer choosing state as the next state.
    }
}
