package it.polimi.ingsw.gc49.server.rooms;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.server.controller.MassiWuPeppeController;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.server.model.Game;
import it.polimi.ingsw.gc49.server.Hall;
import it.polimi.ingsw.gc49.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualGameServerAdapter;

import java.util.List;

public class PlayingRoom extends Room implements VirtualGameServer {
    private Game game;

    @SuppressWarnings("unused")
    public PlayingRoom ( ServerMultiplexer server, Hall hall, String roomName, int maxNumOfPlayers ) {
        super(server, hall, roomName, maxNumOfPlayers);
    }
    public PlayingRoom ( ServerMultiplexer server, Hall hall, String roomName, int maxNumOfPlayers, List<PhasedProxyPlayer> players ) {
        super(server, hall, roomName, maxNumOfPlayers, players);
    }


    public void createGame() throws Exception {
        //creates the game
        game = new Game(maxNumOfPlayers, getListPlayerNicknames());

        //creates the controllers and connects them
        int playerIndex = 0;
        for(PhasedProxyPlayer player : players){
            MassiWuPeppeController controller = new MassiWuPeppeController(playerIndex, player); //creates a controller with the current index
            controller.connectModel(game); //connects controller to the game
            player.setController(controller); //connects the proxy to the controller
            try {
                player.changePhaseClient(new ChangePhasePacket(ApplicationPhase.GAME));//tells them they've entered a game
                player.setServerSideObject(new VirtualGameServerAdapter(this));
            } catch (Exception e) { //some bastard disconnected
                player.forceDisconnect();
            }
            playerIndex++;
        }
    }

    public void runGame() {
        game.gameLoop();
        System.out.println("La partita nella stanza " + roomName + " è conclusa.");
    }

    public Game getGame() {
        return game;
    }

    //### Room's methods
    @Override
    public void enterPlayer ( PhasedProxyPlayer newPlayer ) throws Exception {
        //Shouldn't be possible to enter a game that already started anyway.
        if(canEnter()) {
            try {
                newPlayer.changePhaseClient(new ChangePhasePacket(ApplicationPhase.GAME));
                super.enterPlayer(newPlayer);
                newPlayer.setServerSideObject(new VirtualGameServerAdapter(this));
            } catch (Exception e) {
                newPlayer.forceDisconnect();
            }
        }else{
            throw new RuntimeException("La partita è già iniziata, non puoi entrare nella stanza " + roomName + "." );
        }
    }
    @Override
    protected boolean canEnter() {
        return false;
    }

    @Override
    public MockupRoom giveMockupRoom () {
        return new MockupRoom(MockupRoom.RoomType.PLAYING, roomName, maxNumOfPlayers, getListPlayerNicknames());
    }

    //### client's commands
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        //already directed by controller.
    }

    ///----------------------
    // disconnection
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        //player's reference remains on the server.
    }
}
