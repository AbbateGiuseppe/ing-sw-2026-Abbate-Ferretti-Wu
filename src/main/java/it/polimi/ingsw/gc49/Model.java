package it.polimi.ingsw.gc49;

public class Model {
    private final int numOfPlayers;
    private int numOfConnectedPlayers;
    private final Player[] players;
    private final Track track;


    //### Constructors, from 2 to 5 players
    public Model ( String firstPlayerNickname, String secondPlayerNickname ) {
        this.numOfPlayers = 2;
        players = new Player[numOfPlayers];
        players[0] = new Player(firstPlayerNickname, 0);
        players[1] = new Player(secondPlayerNickname, 1);
        track = new Track(numOfPlayers);
    }
    public Model ( String firstPlayerNickname, String secondPlayerNickname, String thirdPlayerNickname ) {
        this.numOfPlayers = 3;
        players = new Player[numOfPlayers];
        players[0] = new Player(firstPlayerNickname, 0);
        players[1] = new Player(secondPlayerNickname, 1);
        players[2] = new Player(thirdPlayerNickname, 2);
        track = new Track(numOfPlayers);
    }
    public Model ( String firstPlayerNickname, String secondPlayerNickname, String thirdPlayerNickname, String fourthPlayerNickname ) {
        this.numOfPlayers = 4;
        players = new Player[numOfPlayers];
        players[0] = new Player(firstPlayerNickname, 0);
        players[1] = new Player(secondPlayerNickname, 1);
        players[2] = new Player(thirdPlayerNickname, 2);
        players[3] = new Player(fourthPlayerNickname, 3);
        track = new Track(numOfPlayers);
    }
    public Model ( String firstPlayerNickname, String secondPlayerNickname, String thirdPlayerNickname, String fourthPlayerNickname, String fifthPlayerNickname ) {
        this.numOfPlayers = 5;
        players = new Player[numOfPlayers];
        players[0] = new Player(firstPlayerNickname, 0);
        players[1] = new Player(secondPlayerNickname, 1);
        players[2] = new Player(thirdPlayerNickname, 2);
        players[3] = new Player(fourthPlayerNickname, 3);
        players[4] = new Player(fifthPlayerNickname, 4);
        track = new Track(numOfPlayers);

    }

    //### Game's execution
    public void executeCurrentState () {

    }

    //### Players' actions
    public void chooseTotem() {

    }

    public void drawUpperCharacter ( int playerIndex, int cardIndex ) {
    }

    public void drawLowerCharacter ( int playerIndex, int cardIndex ) {

    }

    public void drawUpperBuilding ( int playerIndex, int cardIndex ) {

    }

    public void drawLowerBuilding ( int playerIndex, int cardIndex ) {

    }

    public void chooseOffer ( int playerIndex, int cardIndex ) {

    }

    //### Connection methods
    public void disconnectPlayer( int playerIndex ) {

    }

    public void connectPlayer( int playerIndex ) {

    }
}
