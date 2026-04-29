package it.polimi.ingsw.gc49.rmi_socket.server.rooms;

import it.polimi.ingsw.gc49.View.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_ROOM.InitializeRoomPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.Hall;
import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualRoomServer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualHallServerAdapter;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualRoomServerAdapter;

import java.util.ArrayList;
import java.util.List;

public class WaitingRoom extends Room implements VirtualRoomServer {

    public WaitingRoom ( Hall hall, int roomId, int maxNumOfPlayers ) {
        super(hall, roomId, maxNumOfPlayers);
    }


    //### Room's methods
    @Override
    public void enterPlayer ( PhasedProxyPlayer newPlayer ) {
        if(canEnter()) {
            newPlayer.changeLocalPhase(ApplicationPhase.ROOM);
            super.enterPlayer(newPlayer);
            newPlayer.setServerSideObject(new VirtualRoomServerAdapter(this));

            //send the new player the room he is in
            try {
                List<String> nicknames = new ArrayList();
                for( PhasedProxyPlayer player : players )
                {
                    nicknames.add(player.nickname);
                }
                newPlayer.initializeClientRoom( new InitializeRoomPacket(
                        new MockupRoom(MockupRoom.RoomType.WAITING, roomId, maxNumOfPlayers, nicknames)
                ) );
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }else{
            new RuntimeException("La stanza è piena, non puoi entrare nella stanza " + roomId + "." );
        }
    }
    @Override
    protected boolean canEnter() {
        return maxNumOfPlayers > getNumConnectedPlayers();
    }


    //### client's commands
    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        PhasedProxyPlayer senderPlayer = getPlayerByString(roomLeavePacket.getSenderNickname());
        if( senderPlayer != null ){
            senderPlayer.changeLocalPhase(ApplicationPhase.HALL);
            senderPlayer.setServerSideObject(new VirtualHallServerAdapter(hall));
            players.remove(senderPlayer);
        }
    }
}
