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

import java.util.ArrayList;
import java.util.List;

public class WaitingRoom extends Room implements VirtualRoomServer {

    public WaitingRoom ( Hall hall, int roomId, int maxNumOfPlayers ) {
        super(hall, roomId, maxNumOfPlayers);
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
        }else{
            new RuntimeException("La stanza è piena, non puoi entrare nella stanza " + roomId + "." );
        }
    }
    @Override
    protected boolean canEnter() {
        return maxNumOfPlayers > getNumConnectedPlayers();
    }

    @Override
    public MockupRoom giveMockupRoom () {
        return new MockupRoom(MockupRoom.RoomType.WAITING, roomId, maxNumOfPlayers, getListPlayerNicknames());
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
