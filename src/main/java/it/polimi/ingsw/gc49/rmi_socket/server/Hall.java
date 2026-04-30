package it.polimi.ingsw.gc49.rmi_socket.server;

import it.polimi.ingsw.gc49.View.mockupHall.MockupHall;
import it.polimi.ingsw.gc49.View.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_HALL.InitializeHallPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_HALL.UpdateHallPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualHallServer;
import it.polimi.ingsw.gc49.rmi_socket.server.rooms.PlayingRoom;
import it.polimi.ingsw.gc49.rmi_socket.server.rooms.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Hall implements VirtualHallServer {
    private static final List<Room> rooms = new ArrayList<>();
    private static final Map<String, PhasedProxyPlayer> PlayersInHall = new HashMap<>();

    public void enterPlayer ( PhasedProxyPlayer newPlayer ) throws Exception {
        PlayersInHall.put(newPlayer.nickname, newPlayer);

        //sends the new player the hall he is in
        newPlayer.initializeClientHall( new InitializeHallPacket(giveMockupHall()) );
    }

    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        Room joiningRoom = getRoomById(hallJoinPacket.roomId);
        if( joiningRoom != null ) {
            String senderNickname = hallJoinPacket.getSenderNickname();
            PhasedProxyPlayer senderPlayer = PlayersInHall.get(senderNickname);

            joiningRoom.enterPlayer(senderPlayer); //Enters the player with the nickname of the joinPacket.
            PlayersInHall.remove(senderNickname); //removes the player from the hall.

            //broadcasts the new hall
            broadcastMockupHall();
        }else{
            throw new RuntimeException("Stanza non trovata");
        }
    }

    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        Room newRoom = new PlayingRoom( this, rooms.getLast().roomId + 1, hallCreatePacket.maxNumOfPlayers ); //Creates a new room with Id of the last room + 1.

        String senderNickname = hallCreatePacket.getSenderNickname();
        PhasedProxyPlayer senderPlayer = PlayersInHall.get(senderNickname);

        newRoom.enterPlayer(senderPlayer); //adds the player to the room.
        PlayersInHall.remove(senderNickname); //removes the player from the hall.
        rooms.add(newRoom); //adds the room to the list of rooms.

        //broadcasts the new hall
        broadcastMockupHall();
    }

    public MockupHall giveMockupHall() {
        List<MockupRoom> mockupRooms = new ArrayList<>();
        for( Room room : rooms ) {
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

    private Room getRoomById ( int roomId ) {
        for( Room room : rooms ) {
            if( room.roomId == roomId ) {
                return room;
            }
        }
        return null;
    }

    public void changeRoomIntoPlaying (int roomId) {
        for( Room room : rooms ) {
            if( room.roomId == roomId ) {
                room = new PlayingRoom( this, roomId, room.maxNumOfPlayers );
                //TODO: actually change the reference in the list.
            }
        }
    }
}
