package it.polimi.ingsw.gc49;

public class Player {
    private String nickname;
    private final int playerIndex;
    private boolean connected;
    private boolean removedFromTrack;
    private Totem totem;
    private int food;
    private int points;
    //private ArrayList<Card> characterCards;
    //private ArrayList<Card> buildingCards;

    public final DataBank data;

    private int foodToPay;
    private int pointsToPay;

    private int drawableUpper;
    private int drawableLower;
    private boolean choseAnOffer;

    public Player (String nickname, int playerIndex) {
        this.nickname = nickname;
        this.playerIndex = playerIndex;

        food = 0;
        points = 0;

        connected = true;
        removedFromTrack = false;
        choseAnOffer = false;

        drawableUpper = 0;
        drawableLower = 0;

        data = new DataBank(this);
    }

    //### adders
    public void addFood (int addedFood) {
        food = food + addedFood;
    }
    public void addPoints (int addedPoints) {
        points = points + addedPoints;
    }
    public void addCharacterCard () {

    }
    public void addBuildingCard () {

    }

    //### setters
    public void setFood (int food) {
        this.food = food;
    }
    public void setPoints (int points) {
        this.points = points;
    }
    public void setDrawableUpper (int drawableUpper) {
        this.drawableUpper = drawableUpper;
    }
    public void setDrawableLower (int drawableLower) {
        this.drawableLower = drawableLower;
    }
    public void setTotem (Totem totem) {
        this.totem = totem;
    }

    //### getters
    public String getNickname () {
        return nickname;
    }
    public int getPlayerIndex () {
        return playerIndex;
    }
    public Totem getTotem() {
        return totem;
    }
    public int getFood () {
        return food;
    }
    public int getPoints () {
        return points;
    }
    public int getDrawableUpper () {
        return drawableUpper;
    }
    public int getDrawableLower () {
        return drawableLower;
    }

    //### event's methods
    public void setupToPay () {

    }
    public void confirmToPay () {

    }

    //### connective and exceptional states
    public boolean isConnected () {
        return connected;
    }
    public void setConnected (boolean connected) {
        this.connected = connected;
    }
    public boolean isRemovedFromTrack () {
        return removedFromTrack;
    }

    //### actions' management and checks
    public boolean hasActionsLeft () {
        boolean hasNoActionsLeft = (drawableUpper==0 && drawableLower==0);
        return !hasNoActionsLeft;
    }
    public void cleanRemainingActions () {
        drawableUpper = 0;
        drawableLower = 0;
    }

    public boolean hasChosenAnOffer () {
        return choseAnOffer;
    }
}
