package it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters;

import it.polimi.ingsw.gc49.View.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.View.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_ROOM.InitializeRoomPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.datapacket.uncertain.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.proxies.PhasedProxyPlayer;
import it.polimi.ingsw.gc49.rmi_socket.server.rooms.PlayingRoom;
import it.polimi.ingsw.gc49.rmi_socket.server.rooms.Room;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualRoomServer;

public class VirtualRoomServerAdapter extends VirtualServerAdapter {
    private final VirtualRoomServer adaptee;

    public VirtualRoomServerAdapter(VirtualRoomServer adaptee) {
        this.adaptee = adaptee;
    }

    //### VirtualGameServer
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {}
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {}
    @Override
    public void reconnect ( ReconnectPacket reconnectPacket ) throws Exception {}
    //### VirtualHallServer
    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {}
    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {}
    //### VirtualRoomServer
    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        adaptee.leaveRoom(roomLeavePacket);
    }
    @Override
    public void syncPlayer(PhasedProxyPlayer p) throws Exception {
        Room room = (Room) this.adaptee;

        p.initializeClientRoom(new InitializeRoomPacket(
                room.giveMockupRoom(MockupRoom.RoomType.PLAYING)
        ));

        if (room instanceof PlayingRoom) {
            PlayingRoom playingRoom = (PlayingRoom) room;

            p.changePhaseClient(new ChangePhasePacket(ApplicationPhase.GAME));

            MockupGame gameSnapshot = playingRoom.getGame().giveMockupModel();
            p.initializeClientModel(new InitializeModelPacket(gameSnapshot));

            System.out.println("[REJOIN] Sync completed for " + p.nickname);
        }

        p.setCurrentRoom(room);
        p.setOldRoom(null);
    }

}
