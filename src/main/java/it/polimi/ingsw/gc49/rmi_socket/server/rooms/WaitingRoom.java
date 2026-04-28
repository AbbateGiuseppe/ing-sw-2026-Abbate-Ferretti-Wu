package it.polimi.ingsw.gc49.rmi_socket.server.rooms;

import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ROOM_phase.ROOM_CLIENT.INITIALIZE.RoomClientInitializePacket;
import it.polimi.ingsw.gc49.rmi_socket.ConnectionProxy;
import it.polimi.ingsw.gc49.rmi_socket.server.Hall;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualRoomServer;
import it.polimi.ingsw.gc49.rmi_socket.server.connectors.ReferencedProxyPlayer;

public class WaitingRoom extends Room implements VirtualRoomServer {

    public WaitingRoom ( Hall hall, int roomId, int maxNumOfPlayers ) {
        super(hall, roomId, maxNumOfPlayers);
    }


    //### Room's methods
    @Override
    public void enterPlayer ( ReferencedProxyPlayer newPlayer ) {
        if(canEnter()) {
            newPlayer.changeSubclass(ConnectionProxy.SubclassType.ROOM, this);
            super.enterPlayer(newPlayer);
            try {
                newPlayer.getProxy().initializeClientRoom( new RoomClientInitializePacket() );
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
        ReferencedProxyPlayer senderPlayer = getPlayerByString(roomLeavePacket.getSenderNickname());
        if( senderPlayer != null ){
            senderPlayer.changeSubclass(ConnectionProxy.SubclassType.HALL, hall);
            players.remove(senderPlayer);
            numConnectedPlayers--;
        }
    }
}
