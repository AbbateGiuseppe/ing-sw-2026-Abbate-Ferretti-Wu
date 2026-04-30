package it.polimi.ingsw.gc49.rmi_socket.server;

import it.polimi.ingsw.gc49.View.mockupHall.MockupHall;
import it.polimi.ingsw.gc49.View.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_HALL.InitializeHallPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_HALL.UpdateHallPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.server.rooms.WaitingRoom;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualHallServer;
import it.polimi.ingsw.gc49.rmi_socket.server.rooms.PlayingRoom;
import it.polimi.ingsw.gc49.rmi_socket.server.rooms.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hall implements VirtualHallServer {
    private static final Map<String, Room> rooms = new HashMap<>();
    private static final Map<String, PhasedProxyPlayer> PlayersInHall = new HashMap<>();

    public void enterPlayer ( PhasedProxyPlayer newPlayer ) throws Exception {
        System.out.println("Il giocatore " + newPlayer.nickname + " è entrato nell'atrio.");
        PlayersInHall.put(newPlayer.nickname, newPlayer);

        //sends the new player the hall he is in
        newPlayer.initializeClientHall( new InitializeHallPacket(giveMockupHall()) );
    }

    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        Room joiningRoom = rooms.get(hallJoinPacket.roomName);
        if( joiningRoom != null ) {
            //gets the sending player
            String senderNickname = hallJoinPacket.getSenderNickname();
            PhasedProxyPlayer senderPlayer = PlayersInHall.get(senderNickname);

            joiningRoom.enterPlayer(senderPlayer); //enters the player into the room.
            PlayersInHall.remove(senderNickname); //removes the player from the hall.

            //broadcasts the new hall
            broadcastMockupHall();
        }else{
            throw new RuntimeException("Stanza non trovata");
        }
    }

    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        Room newRoom = new PlayingRoom( this, hallCreatePacket.roomName, hallCreatePacket.maxNumOfPlayers ); //Creates a new room with Id of the last room + 1.

        String senderNickname = hallCreatePacket.getSenderNickname();
        PhasedProxyPlayer senderPlayer = PlayersInHall.get(senderNickname);

        newRoom.enterPlayer(senderPlayer); //adds the player to the room.
        PlayersInHall.remove(senderNickname); //removes the player from the hall.
        rooms.put(newRoom.roomName, newRoom); //adds the room to the list of rooms.

        //broadcasts the new hall
        broadcastMockupHall();
    }

    public MockupHall giveMockupHall() {
        List<MockupRoom> mockupRooms = new ArrayList<>();
        for( Room room : rooms.values() ) {
            mockupRooms.add(room.giveMockupRoom());
        }

        return new MockupHall(
                new ArrayList<>(PlayersInHall.keySet()),
                mockupRooms
        );
    }

    private void broadcastMockupHall() throws Exception {
        UpdateHallPacket updatedHall = new UpdateHallPacket(giveMockupHall());

        for( PhasedProxyPlayer player : PlayersInHall.values() ) {
            player.updateClientHall(updatedHall);
        }
    }

    /**
     * This method converts a waiting room into a playing room and then initializes+starts the game.
     * @param WaitingRoomName, the name of the waiting room that is being change into a playing room;
     */
    public void changeRoomIntoPlaying (String WaitingRoomName) {
        WaitingRoom changingRoom = (WaitingRoom) rooms.get(WaitingRoomName); //gets the waitingRoom to change into a playing room
        PlayingRoom playingRoom = changingRoom.convertIntoPlaying(); //gets its converted version.
        rooms.remove(WaitingRoomName);
        rooms.put(playingRoom.roomName, playingRoom);

        //creates,initializes the new game, even connects the controllers.
        playingRoom.createGame();

        //starts the new game with its own thread.
        new Thread(() -> {
            playingRoom.runGame();
        }).start();
    }
}
