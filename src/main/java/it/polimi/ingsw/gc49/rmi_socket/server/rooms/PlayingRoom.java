package it.polimi.ingsw.gc49.rmi_socket.server.rooms;

import it.polimi.ingsw.gc49.View.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.controller.massi.MassiController;
import it.polimi.ingsw.gc49.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_ROOM.UpdateRoomPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.uncertain.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.model.Game;
import it.polimi.ingsw.gc49.rmi_socket.server.Hall;
import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualGameServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualGameServerAdapter;

import java.util.List;

public class PlayingRoom extends Room implements VirtualGameServer {
    private Game game;

    public PlayingRoom ( Hall hall, String roomName, int maxNumOfPlayers ) {
        super(hall, roomName, maxNumOfPlayers);
    }
    public PlayingRoom ( Hall hall, String roomName, int maxNumOfPlayers, List<PhasedProxyPlayer> players ) {
        super(hall, roomName, maxNumOfPlayers, players);
    }


    public void createGame() {
        //creates the game
        game = new Game(maxNumOfPlayers, getListPlayerNicknames());

        //creates the controllers and connects them
        int playerIndex = 0;
        for(PhasedProxyPlayer player : players){
            MassiController controller = new MassiController(playerIndex); //creates a controller with the current index
            controller.connectModel(game); //connects controller to the game
            player.setController(controller); //connects the proxy to the controller
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
            newPlayer.changePhaseClient(new ChangePhasePacket(ApplicationPhase.GAME));
            super.enterPlayer(newPlayer);
            newPlayer.setServerSideObject(new VirtualGameServerAdapter(this));
        }else{
            throw new RuntimeException("Game already started, cannot enter into " + roomName + "." );
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

    }
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {

    }
    @Override
    public void reconnect ( ReconnectPacket reconnectPacket ) throws Exception {

    }

    private void broadcastMockupRoom() throws Exception {
        UpdateRoomPacket updatedRoom = new UpdateRoomPacket(giveMockupRoom(MockupRoom.RoomType.PLAYING));
        for( PhasedProxyPlayer player : players) {
            player.updateClientRoom(updatedRoom);
        }
    }
}
