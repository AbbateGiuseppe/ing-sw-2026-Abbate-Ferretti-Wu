package it.polimi.ingsw.gc49;

public class Model {
    private final int numOfPlayers;
    private int numOfConnectedPlayers;

    public Model (int numOfPlayers) {
        this.numOfPlayers = numOfPlayers;
    }

    //### Game's execution
    public void executeCurrentState () {

    }

    //### Players' actions
    public void chooseTotem() {

    }

    public void drawUpperCharacter (int playerIndex, int cardIndex) {
    }

    public void drawLowerCharacter (int playerIndex, int cardIndex) {

    }

    public void drawUpperBuilding (int playerIndex, int cardIndex) {
    }

    public void drawLowerBuilding (int playerIndex, int cardIndex) {

    }

    public void chooseOffer (int playerIndex, int cardIndex) {

    }

    //### Connection methods
    public void disconnectPlayer(int playerIndex) {

    }

    public void connectPlayer(int playerIndex) {

    }
}
