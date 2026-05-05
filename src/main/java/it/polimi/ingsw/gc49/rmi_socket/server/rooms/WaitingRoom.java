package it.polimi.ingsw.gc49.rmi_socket.server.rooms;

import it.polimi.ingsw.gc49.View.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_ROOM.UpdateRoomPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_ROOM.InitializeRoomPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.Hall;
import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualRoomServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualHallServerAdapter;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualRoomServerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class WaitingRoom extends Room implements VirtualRoomServer {
    private static final int TIME_BEFORE_GAME_START = 10;

    private ScheduledExecutorService startingGameScheduler = Executors.newSingleThreadScheduledExecutor();

    public WaitingRoom ( Hall hall, String roomName, int maxNumOfPlayers ) {
        super(hall, roomName, maxNumOfPlayers);
    }


    private void scheduleGameStart() {
        try {
            startingGameScheduler.schedule(() -> {
                try {
                    startGame();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }, TIME_BEFORE_GAME_START, TimeUnit.SECONDS);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }
    }
    private void startGame() throws Exception {
        if(maxNumOfPlayers == getNumConnectedPlayers()){
            System.out.println("La partita nella stanza " + roomName + " sta iniziando...");
            //creates the game
            hall.changeRoomIntoPlaying(roomName);
        }
    }

    //### Room's methods
    @Override
    public void enterPlayer ( PhasedProxyPlayer newPlayer ) throws Exception {
        if(canEnter()) {
            newPlayer.changePhaseClient(new ChangePhasePacket(ApplicationPhase.ROOM));

            super.enterPlayer(newPlayer);
            newPlayer.setServerSideObject(new VirtualRoomServerAdapter(this));

            //sends the new player the room he is in
            newPlayer.initializeClientRoom( new InitializeRoomPacket(giveMockupRoom()) );

            //broadcasts the new room
            broadcastMockupRoom();

            //starts the countdown to game start if the number of necessary players was reached
            if(maxNumOfPlayers == getNumConnectedPlayers()){
                scheduleGameStart();
            }
        }else{
            new RuntimeException("La stanza è piena, non puoi entrare nella stanza " + roomName + "." );
        }
    }
    @Override
    protected boolean canEnter() {
        return maxNumOfPlayers > getNumConnectedPlayers();
    }

    @Override
    public MockupRoom giveMockupRoom () {
        return new MockupRoom(MockupRoom.RoomType.WAITING, roomName, maxNumOfPlayers, getListPlayerNicknames());
    }

    public PlayingRoom convertIntoPlaying () {
        return new PlayingRoom(hall, roomName, maxNumOfPlayers, players);
    }

    private void broadcastMockupRoom() throws Exception {
        UpdateRoomPacket updatedRoom = new UpdateRoomPacket(giveMockupRoom());

        for( PhasedProxyPlayer player : players ){
            player.updateClientRoom(updatedRoom);
        }
    }


    //### client's commands
    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        PhasedProxyPlayer senderPlayer = getPlayerByString(roomLeavePacket.getSenderNickname());
        if( senderPlayer != null ){
            senderPlayer.changePhaseClient(new ChangePhasePacket(ApplicationPhase.HALL));
            senderPlayer.setServerSideObject(new VirtualHallServerAdapter(hall));
            players.remove(senderPlayer);

            //broadcasts the new room
            broadcastMockupRoom();
        }
    }
}
