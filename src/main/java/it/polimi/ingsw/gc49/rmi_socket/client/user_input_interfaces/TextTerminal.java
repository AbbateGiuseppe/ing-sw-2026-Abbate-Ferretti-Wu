package it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces;

import it.polimi.ingsw.gc49.View.Mockup;
import it.polimi.ingsw.gc49.View.mockupHall.MockupRoom;
import it.polimi.ingsw.gc49.View.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.controller.massi.MassiPlayerActionEnum;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.model.Totem;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextTerminal extends UserInputInterface {
    /**The key of the commands is the string representing the command type, such as "help" or "draw",
     * the rest of the following strings are used as parameters to specify the behaviour in the function of the called TerminalCommand*/
    private static final Map<String, TerminalCommand> commands = new HashMap<>();
    private static StringBuilder manual = new StringBuilder();
    private static Map<String,Totem> totems = Map.of(
            "orange", Totem.ORANGE,
            "yellow", Totem.YELLOW,
            "black", Totem.BLACK,
            "white",Totem.WHITE
    );

    static {
        // Creare una stanza
        commands.put("create", ( terminalPhase, terminalMockups, terminalParameters ) -> {
            if(terminalPhase == ApplicationPhase.HALL) {
                String roomName = terminalParameters[0];
                int maxNumOfPlayers = Integer.parseInt(terminalParameters[1]);
                if ( maxNumOfPlayers <= 5) {
                    // Controlla se la stanza è gia stata creata
                    if (terminalMockups.getHall().rooms.stream().map(r -> r.roomName).noneMatch(s -> s.equals(roomName))) {
                        HallCreatePacket hallCreatePacket = new HallCreatePacket(roomName,maxNumOfPlayers);
                        virtualServer.createRoom(hallCreatePacket);
                        // TODO:need change the phase to ROOM
                    }
                }
            }
        });
        // Entrare una stanza
        commands.put("join", ( terminalPhase, terminalMockups, terminalParameters ) -> {
            if(terminalPhase == ApplicationPhase.HALL) {
                String roomName = terminalParameters[0];
                // Controlla se la stanza esiste
                if (terminalMockups.getHall().rooms.stream().map(r -> r.roomName).anyMatch(s -> s.equals(roomName))) {
                    HallJoinPacket hallJoinPacket = new HallJoinPacket(roomName);
                    virtualServer.joinRoom(hallJoinPacket);
                    // TODO:change the phase to ROOM
                }
            }
        });
        // Mostrare tutte le stanze
        commands.put("rooms", ( terminalPhase, terminalMockups, terminalParameters ) -> {
            if(terminalPhase == ApplicationPhase.HALL) {
                for (MockupRoom room : terminalMockups.getHall().rooms) {
                    System.out.println(room);
                }
            }
        });


        // Uscire da una stanza
        commands.put("leave", ( terminalPhase, terminalMockups, terminalParameters ) -> {
            if(terminalPhase == ApplicationPhase.ROOM) {
                virtualServer.leaveRoom(new RoomLeavePacket());
                // TODO:need change the phase to HALL
            }
        });


        // Mostra la manuale di istruzioni per il gioco
        commands.put("help", ( terminalPhase, terminalMockups, terminalParameters ) ->  {
            if(terminalPhase == ApplicationPhase.GAME){
                printManual();
            }}
        );
        // Mostra i cibi e i punti di tutti i giocatori
        commands.put("status", ( terminalPhase, terminalMockups, terminalParameters ) -> {
            if(terminalPhase == ApplicationPhase.GAME) {
                for (MockupPlayer p : terminalMockups.getGame().getPlayers()) {
                    System.out.println(p);
                }
            }
        });
        // Mostra le carte di un giocatore dato il suo indice
        commands.put("cards", ( terminalPhase, terminalMockups, terminalParameters ) -> {
            if(terminalPhase == ApplicationPhase.GAME) {
                int index = Integer.parseInt(terminalParameters[0]);
                terminalMockups.getGame().getPlayer(index).printCards();
            }
        });
        // Sceglie una offerta
        commands.put("offer", ( terminalPhase, terminalMockups, terminalParameters ) -> {
            if(terminalPhase == ApplicationPhase.GAME) {
                int offerIndex = Integer.parseInt(terminalParameters[0]);
                CommandPacket commandPacket = new CommandPacket(MassiPlayerActionEnum.CHOOSE_OFFER,offerIndex);
                virtualServer.sendCommand(commandPacket);
            }
        });
        // Sceglie un totem
        commands.put("totem", ( terminalPhase, terminalMockups, terminalParameters ) -> {
            if(terminalPhase == ApplicationPhase.GAME) {
                if (totems.containsKey(terminalParameters[0].toLowerCase())) {
                    Totem totem = totems.get(terminalParameters[0].toLowerCase());
                    CommandPacket commandPacket = new CommandPacket(MassiPlayerActionEnum.CHOOSE_TOTEM,totem);
                    virtualServer.sendCommand(commandPacket);
                }
            }
        });
        // Sceglie una carta
        commands.put("card", ( terminalPhase, terminalMockups, terminalParameters ) -> {
            if(terminalPhase == ApplicationPhase.GAME) {
                int cardIndex = Integer.parseInt(terminalParameters[2]);
                MassiPlayerActionEnum action = null;
                if (terminalParameters[0].equalsIgnoreCase("lower")) {
                    if (terminalParameters[1].equalsIgnoreCase("character")) {
                        action = MassiPlayerActionEnum.DRAW_LOWER_CHARACTER;
                    } else if (terminalParameters[1].equalsIgnoreCase("building")) {
                        action = MassiPlayerActionEnum.DRAW_LOWER_BUILDING;
                    }
                } else if (terminalParameters[0].equalsIgnoreCase("upper")) {
                    if (terminalParameters[1].equalsIgnoreCase("character")) {
                        action = MassiPlayerActionEnum.DRAW_UPPER_CHARACTER;
                    } else if (terminalParameters[1].equalsIgnoreCase("building")) {
                        action = MassiPlayerActionEnum.DRAW_UPPER_BUILDING;
                    }
                }
                if (action != null) {
                    CommandPacket commandPacket = new CommandPacket(action,cardIndex);
                    virtualServer.sendCommand(commandPacket);
                }
            }
        });
        // Disconnete dal gioco
        commands.put("disconnect", ( terminalPhase, terminalMockups, terminalParameters ) -> {
            if(terminalPhase == ApplicationPhase.GAME) {
                virtualServer.disconnect(new DisconnectPacket(terminalParameters[0]));
                // TODO:need change the phase to ??
            }
        });
        // TODO:reconnect command
    }

    static {
        // Commands section
        manual.append("COMMANDS:\n");
        manual.append("-".repeat(60)).append("\n");

        // Command entries
        manual.append(formatCommand("help", "Displays this help message"));
        manual.append(formatCommand("status", "Display the food and points of each player"));
        manual.append(formatCommand("cards [player index]", "Display the cards of the specified player"));
        manual.append(formatCommand("draw [lower/upper] [character/building] [card index]", "Draw the specified card"));
        manual.append(formatCommand("offer [offer index]", "Choose the specified offer"));
        manual.append(formatCommand("totem [totem color]", "Choose the specified totem"));
    }
    private static void printManual() {System.out.println(manual);}
    private static String formatCommand(String command, String description) {return String.format(" %60s | %s\n", command, description);}

    public TextTerminal ( VirtualServer virtualServer , Mockup mockups, ApplicationPhase currentPhase) {
        super( virtualServer, mockups, currentPhase);
    }

    @Override
    public void runInput() throws Exception {
        try {
            Scanner scanner = new Scanner(System.in);
            System.out.println("\nTerminal started:\ntype help for the list of commands");
            while (true) {
                // TODO:when does the loop stop?
                System.out.print(">");
                String textCommand = scanner.nextLine();

                // 1. Split the string by whitespace
                String[] parts = textCommand.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length == 0) continue;

                // 2. The first word is the key
                String action = parts[0].toLowerCase();

                // 3. The remaining words are parameters
                String[] params = Arrays.copyOfRange(parts, 1, parts.length);

                // 4. Look up the terminal function and execute
                TerminalCommand command = commands.get(action);
                if (command != null) {
                    command.execute(currentPhase, mockups, params);
                } else {
                    System.out.println("Unknown command: " + action);
                }

            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}