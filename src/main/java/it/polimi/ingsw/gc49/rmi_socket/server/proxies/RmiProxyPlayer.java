package it.polimi.ingsw.gc49.rmi_socket.server.proxies;

import it.polimi.ingsw.gc49.datapacket.directedToClient.CHANGE_PHASE.ChangePhasePacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_MODEL.InitializeModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_MODEL.UpdateModelPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_HALL.InitializeHallPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_HALL.UpdateHallPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.INITIALIZE_ROOM.InitializeRoomPacket;
import it.polimi.ingsw.gc49.datapacket.directedToClient.UPDATE_ROOM.UpdateRoomPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.datapacket.uncertain.RECONNECT.ReconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.server.ServerMultiplexer;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualClients.VirtualClient;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.adapters.VirtualServerAdapter;

import java.rmi.RemoteException;

public class RmiProxyPlayer extends PhasedProxyPlayer {
    public RmiProxyPlayer ( ServerMultiplexer server, String nickname,
                            ApplicationPhase startingPhase,
                            VirtualServerAdapter serverSide, VirtualClient clientSide ) {
        super(server, nickname, startingPhase, serverSide, clientSide, null, null);
    }

    // In RmiProxyPlayer
    public void updateClientStub(VirtualClient newClientStub) {
        this.clientSide = newClientStub; // Aggiorna il riferimento remoto
        this.running = true;            // Riattiva il proxy
    }

    //###################
    //### VirtualClient
    @Override
    public void changePhaseClient ( ChangePhasePacket changePhasePacket ) throws Exception {
        changeLocalPhase(changePhasePacket.newPhase);
        clientSide.changePhaseClient(changePhasePacket);
    }
    //#######################
    //### VirtualGameClient
    @Override
    public void initializeClientModel ( InitializeModelPacket initializeModelPacket ) throws Exception {
        if(assureRightPhase(initializeModelPacket)){
            clientSide.initializeClientModel(initializeModelPacket);
        }
    }
    @Override
    public void updateClientModel ( UpdateModelPacket updateModelPacket ) throws Exception {
        if(assureRightPhase(updateModelPacket)){
            clientSide.updateClientModel(updateModelPacket);
        }
    }
    @Override
    public void reportError ( ErrorPacket errorPacket ) throws Exception {
        if(assureRightPhase(errorPacket)){
            clientSide.reportError(errorPacket);
        }
    }
    //### VirtualGameServer
    @Override
    public void sendCommand ( CommandPacket commandPacket ) throws Exception {
        if(assureRightPhase(commandPacket)){
            addSenderNickname(commandPacket);
            //executes the command on the controller
            if(controller != null) {
                controller.executeCommand(commandPacket);
            }
        }
    }
    @Override
    public void disconnect ( DisconnectPacket disconnectPacket ) throws Exception {
        if(assureRightPhase(disconnectPacket)){
            addSenderNickname(disconnectPacket);
            serverSide.disconnect(disconnectPacket);
        }
    }

    //### VirtualHallClient
    @Override
    public void initializeClientHall ( InitializeHallPacket initializeHallPacket ) throws Exception {
        if(assureRightPhase(initializeHallPacket)){
            clientSide.initializeClientHall(initializeHallPacket);
        }
    }
    @Override
    public void updateClientHall ( UpdateHallPacket updateHallPacket ) throws Exception {
        if(assureRightPhase(updateHallPacket)){
            clientSide.updateClientHall(updateHallPacket);
        }
    }
    //### VirtualHallServer
    @Override
    public void joinRoom ( HallJoinPacket hallJoinPacket ) throws Exception {
        if(assureRightPhase(hallJoinPacket)){
            addSenderNickname(hallJoinPacket);
            serverSide.joinRoom(hallJoinPacket);
        }
    }
    @Override
    public void createRoom ( HallCreatePacket hallCreatePacket ) throws Exception {
        if(assureRightPhase(hallCreatePacket)){
            addSenderNickname(hallCreatePacket);
            serverSide.createRoom(hallCreatePacket);
        }
    }
    //### VirtualRoomClient
    @Override
    public void initializeClientRoom ( InitializeRoomPacket initializeRoomPacket ) throws Exception {
        if(assureRightPhase(initializeRoomPacket)){
            clientSide.initializeClientRoom(initializeRoomPacket);
        }
    }
    @Override
    public void updateClientRoom ( UpdateRoomPacket updateRoomPacket ) throws Exception {
        if(assureRightPhase(updateRoomPacket)){
            clientSide.updateClientRoom(updateRoomPacket);
        }
    }
    //### VirtualRoomServer
    @Override
    public void leaveRoom ( RoomLeavePacket roomLeavePacket ) throws Exception {
        if(assureRightPhase(roomLeavePacket)){
            addSenderNickname(roomLeavePacket);
            serverSide.leaveRoom(roomLeavePacket);
        }
    }

    @Override
    public void ping() throws Exception {
        // call method on clientStub
        clientSide.receiveHeartbeat();
    }

    // client tell server that it's still alive
    @Override
    public void receiveHeartbeat() throws RemoteException {
        reportActivity(); // Reset timeout on server
    }

    @Override
    public void syncPlayer(PhasedProxyPlayer p) throws Exception {

    }
}
