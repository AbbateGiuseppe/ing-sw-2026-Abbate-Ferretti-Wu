package it.polimi.ingsw.gc49.server.rooms;

import it.polimi.ingsw.gc49.client.view.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.INITIALIZE_ROOM.InitializeRoomPacket;
import it.polimi.ingsw.gc49.server.Hall;
import it.polimi.ingsw.gc49.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualRoomServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualRoomServerAdapter;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static it.polimi.ingsw.gc49.server.ServerMultiplexer.saveState;

public class WaitingRoom extends Room implements VirtualRoomServer {
    private static final int TIME_BEFORE_GAME_START = 10;

    private transient final ScheduledExecutorService startingGameScheduler = Executors.newSingleThreadScheduledExecutor();

    public WaitingRoom ( ServerMultiplexer server, Hall hall, String roomName, int maxNumOfPlayers ) {
        super(server, hall, roomName, maxNumOfPlayers);
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

    ///----------------------
    // disconnection
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        String disconnectedNickname = disconnectPacket.getSenderNickname();
        //disconnects the player from the room
        players.removeIf(player -> player.nickname.equals(disconnectedNickname));
        broadcastMockupRoom(); // updates the rooms view
        server.disconnect(disconnectPacket); //removes the player reference from the server
    }

    ///---------------------------
    //### Room's methods
    @Override
    public void enterPlayer ( PhasedProxyPlayer newPlayer ) throws Exception {
        if(canEnter()) {
            try {
                newPlayer.changePhaseClient(new ChangePhasePacket(ApplicationPhase.ROOM));

                super.enterPlayer(newPlayer);
                newPlayer.setServerSideObject(new VirtualRoomServerAdapter(this));

                //sends the new player the room he is in
                newPlayer.initializeClientRoom(new InitializeRoomPacket(giveMockupRoom()));

                //broadcasts the new room
                broadcastMockupRoom();

                //starts the countdown to game start if the number of necessary players was reached
                if (maxNumOfPlayers == getNumConnectedPlayers()) {
                    scheduleGameStart();
                }
            } catch (Exception e) {
                newPlayer.forceDisconnect();
            }
        }else{
            throw new RuntimeException("La stanza è piena, non puoi entrare nella stanza " + roomName + ".");
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
        return new PlayingRoom(server, hall, roomName, maxNumOfPlayers, players);
    }


    //### client's commands
    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        PhasedProxyPlayer senderPlayer = getPlayerByString(roomLeavePacket.getSenderNickname());
        if( senderPlayer != null ){
            //enter the hall
            players.remove(senderPlayer);
            hall.enterPlayer(senderPlayer);

            //broadcasts the new room
            broadcastMockupRoom();
        }
    }
}
