package it.polimi.ingsw.gc49.client.user_input_interfaces;

import it.polimi.ingsw.gc49.client.view.Mockup;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.server.controller.PlayerActionEnum;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.server.model.Totem;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.terminal.Terminal;
import org.jline.terminal.TerminalBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class TextTerminal extends UserInputInterface {
    /**The key of the commands is the string representing the command type, such as "help" or "draw",
     * the rest of the following strings are used as parameters to specify the behaviour in the function of the called TerminalCommand*/
    private static final Map<String, TerminalCommand> commands = new HashMap<>();
    private static final StringBuilder manual = new StringBuilder();
    private static final Terminal terminal;
    static {
        try {
            terminal = TerminalBuilder.builder().build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static final LineReader scanner = LineReaderBuilder.builder().terminal(terminal).build();
    private static final Map<String,Totem> totems = new HashMap<>();
    static {
        totems.put("orange", Totem.ORANGE);
        totems.put("white", Totem.WHITE);
        totems.put("blue", Totem.BLUE);
        totems.put("black", Totem.BLACK);
        totems.put("yellow", Totem.YELLOW);
    }

    static {
        // Mostra la manuale di istruzioni per il gioco
        commands.put("help", ( _, _, _, _ ) -> printManual());

        //Chiude l'applicazione e si disconnette dal serviente
        commands.put("disconnect", ( _, _, _, terminalVirtualServer ) -> {
            System.out.println("| Are you sure you want disconnect from the server?   |");
            System.out.println("| You'll have to restart the application to reconnect |");
            if( scanner.readLine("  Type YES to confirm: ").equalsIgnoreCase("yes") ){
                terminalVirtualServer.disconnect(new DisconnectPacket());
                System.exit(0);
            }
        });

        // Creare una stanza
        commands.put("create", ( terminalPhase, terminalMockups, terminalParameters, terminalVirtualServer ) -> {
            if(terminalPhase == ApplicationPhase.HALL && terminalParameters.length >= 2) {
                String roomName = terminalParameters[0];
                try {
                    int maxNumOfPlayers = Integer.parseInt(terminalParameters[1]);
                    if ( maxNumOfPlayers <= 5 && maxNumOfPlayers >= 2 ) {
                        // Controlla se la stanza è gia stata creata
                        if (terminalMockups.getHall().rooms.stream().map(r -> r.roomName).noneMatch(s -> s.equals(roomName))) {
                            HallCreatePacket hallCreatePacket = new HallCreatePacket(roomName,maxNumOfPlayers);
                            terminalVirtualServer.createRoom(hallCreatePacket);
                        }
                    }
                } catch (NumberFormatException e) {
                    System.out.println("please, specify correctly the number of players!");
                }
            }
        });
        // Entrare una stanza
        commands.put("join", ( terminalPhase, terminalMockups, terminalParameters, terminalVirtualServer ) -> {
            if(terminalPhase == ApplicationPhase.HALL && terminalParameters.length >= 1) {
                String roomName = terminalParameters[0];
                // Controlla se la stanza esiste
                if (terminalMockups.getHall().rooms.stream().map(r -> r.roomName).anyMatch(s -> s.equals(roomName))) {
                    HallJoinPacket hallJoinPacket = new HallJoinPacket(roomName);
                    terminalVirtualServer.joinRoom(hallJoinPacket);
                }
            }
        });
        // Mostrare tutte le informazioni importanti del luogo attuale
        commands.put("show", ( terminalPhase, terminalMockups, _, _ ) -> {
            if(terminalPhase == ApplicationPhase.HALL) {
                if(terminalMockups.getHall() != null) {
                    System.out.println(terminalMockups.getHall());
                }else{
                    System.out.println("Hall not yet loaded.");
                }
            }else if(terminalPhase == ApplicationPhase.ROOM) {
                if(terminalMockups.getRoom() != null) {
                    System.out.println(terminalMockups.getRoom());
                    System.out.println(terminalMockups.getRoom().toStringPlayers());
                }else{
                    System.out.println("Room not yet loaded.");
                }
            }else if(terminalPhase == ApplicationPhase.GAME) {
                if(terminalMockups.getGame() != null) {
                    for (MockupPlayer p : terminalMockups.getGame().getPlayers()) {
                        System.out.println(p);
                    }
                }else{
                    System.out.println("Game not yet loaded.");
                }
            }
        });


        // Uscire da una stanza
        commands.put("leave", ( terminalPhase, _, _, terminalVirtualServer ) -> {
            if(terminalPhase == ApplicationPhase.ROOM) {
                terminalVirtualServer.leaveRoom(new RoomLeavePacket());
                // TODO:need change the phase to HALL
            }
        });


        // Mostra le carte di un giocatore dato il suo indice
        commands.put("cards", ( terminalPhase, terminalMockups, terminalParameters, _ ) -> {
            if(terminalPhase == ApplicationPhase.GAME && terminalParameters.length >= 1) {
                try {
                    int index = Integer.parseInt(terminalParameters[0]);
                    if(index < terminalMockups.getGame().getPlayers().size() && index >= 0) {
                        terminalMockups.getGame().getPlayer(index).printCards();
                    }else{
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    System.out.println("please, insert a valid number!");
                }
            }
        });
        // Sceglie una offerta
        commands.put("offer", ( terminalPhase, _, terminalParameters, terminalVirtualServer ) -> {
            if(terminalPhase == ApplicationPhase.GAME && terminalParameters.length >= 1) {
                try {
                    int offerIndex = Integer.parseInt(terminalParameters[0]);
                    CommandPacket commandPacket = new CommandPacket(PlayerActionEnum.CHOOSE_OFFER, offerIndex);
                    terminalVirtualServer.sendCommand(commandPacket);
                } catch (NumberFormatException e) {
                    System.out.println("please, insert a valid number!");
                }
            }
        });
        // Sceglie un totem
        commands.put("totem", ( terminalPhase, _, terminalParameters, terminalVirtualServer ) -> {
            if(terminalPhase == ApplicationPhase.GAME && terminalParameters.length >= 1) {
                if (totems.containsKey(terminalParameters[0].toLowerCase())) {
                    Totem totem = totems.get(terminalParameters[0].toLowerCase());
                    CommandPacket commandPacket = new CommandPacket(PlayerActionEnum.CHOOSE_TOTEM,totem);
                    terminalVirtualServer.sendCommand(commandPacket);
                }
            }
        });
        // Sceglie una carta
        commands.put("card", ( terminalPhase, _, terminalParameters, terminalVirtualServer ) -> {
            if(terminalPhase == ApplicationPhase.GAME && terminalParameters.length >= 3) {
                try {
                    int cardIndex = Integer.parseInt(terminalParameters[2]);
                    PlayerActionEnum action = null;
                    if (terminalParameters[0].equalsIgnoreCase("lower")) {
                        if (terminalParameters[1].equalsIgnoreCase("character")) {
                            action = PlayerActionEnum.DRAW_LOWER_CHARACTER;
                        } else if (terminalParameters[1].equalsIgnoreCase("building")) {
                            action = PlayerActionEnum.DRAW_LOWER_BUILDING;
                        }
                    } else if (terminalParameters[0].equalsIgnoreCase("upper")) {
                        if (terminalParameters[1].equalsIgnoreCase("character")) {
                            action = PlayerActionEnum.DRAW_UPPER_CHARACTER;
                        } else if (terminalParameters[1].equalsIgnoreCase("building")) {
                            action = PlayerActionEnum.DRAW_UPPER_BUILDING;
                        }
                    }
                    if (action != null) {
                        CommandPacket commandPacket = new CommandPacket(action, cardIndex);
                        terminalVirtualServer.sendCommand(commandPacket);
                    }else{
                        terminal.writer().println("not a valid card choice!");
                    }
                } catch (NumberFormatException e) {
                    terminal.writer().println("please, insert a valid number!");
                }
            }
        });
    }

    static {
        // Commands section
        manual.append("-".repeat(125)).append("\n");

        // Command entries
        manual.append(String.format("%72s\n", "GENERAL commands:"));
        manual.append(formatCommand("help", "Displays this help message"));
        manual.append(formatCommand("show", "Displays the info of the current place you are in"));
        //HALL commands
        manual.append("-".repeat(125)).append("\n");
        manual.append(String.format("%70s\n", "HALL commands:"));
        manual.append(formatCommand("create (\"your room name\") (num. players)", "Creates a waiting room with the chosen room's name (in Quotation marks)"));
        manual.append(formatCommand("join (\"chosen room name\")", "Makes you join the room with your chosen name (in Quotation marks)"));
        //ROOM commands
        manual.append("-".repeat(125)).append("\n");
        manual.append(String.format("%70s\n", "ROOM commands:"));
        manual.append(formatCommand("leave", "Leaves the current room"));
        //GAME commands
        manual.append("-".repeat(125)).append("\n");
        manual.append(String.format("%70s\n", "GAME commands:"));
        manual.append(formatCommand("cards [player index]", "Display the cards of the specified player"));
        manual.append(formatCommand("draw [lower/upper] [character/building] [card index]", "Draw the specified card"));
        manual.append(formatCommand("offer [offer index]", "Choose the specified offer"));
        manual.append(formatCommand("totem [orange/white/blue/black/yellow]", "Choose the specified totem"));
        manual.append(formatCommand("disconnect", "Disconnects you from the game"));
    }
    private static void printManual() {System.out.println(manual);}
    public void printString ( String string ) {
        scanner.printAbove(string);
    }
    private static String formatCommand(String command, String description) {return String.format(" %60s | %s\n", command, description);}

    public TextTerminal ( VirtualServer virtualServer , Mockup mockups, ApplicationPhase currentPhase) {
        super( virtualServer, mockups, currentPhase);
    }

    @Override
    public void setCurrentPhase ( ApplicationPhase phase ) {
        super.setCurrentPhase(phase);
        switch (phase) {
            case GAME:
                scanner.printAbove("-The game started.");
                break;
            case HALL:
                scanner.printAbove("-You entered the hall.");
                break;
            case ROOM:
                scanner.printAbove("-You entered a room.");
                break;
        }
    }

    @Override
    public void runInput() {
        try {

            /*// Start a background thread to print messages
            new Thread(() -> {
                try {
                    for (int i = 0; i < 10; i++) {
                        Thread.sleep(1000);
                        scanner.printAbove("-");
                        scanner.printAbove("Notification #" + i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();*/

            System.out.println("\nTerminal started (type help for the list of commands):\n");

            while (true) {
                // TODO:when does the loop stop?
                //System.out.print(">");
                String lineCommand = scanner.readLine(">");

                // 1. Split the string by whitespace
                String[] parts = lineCommand.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                if (parts.length == 0) continue;

                // 2. The first word is the key
                String action = parts[0].toLowerCase();

                // 3. The remaining words are parameters
                String[] params = Arrays.copyOfRange(parts, 1, parts.length);

                // 4. Look up the terminal function and execute
                TerminalCommand command = commands.get(action);
                if (command != null) {
                    command.execute(currentPhase, mockups, params, virtualServer);
                } else {
                    terminal.writer().println("Unknown command: " + action);
                }

            }
        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }
}