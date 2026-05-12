package it.polimi.ingsw.gc49.client.user_input_interfaces;

import it.polimi.ingsw.gc49.client.ClientApplication;
import it.polimi.ingsw.gc49.client.view.Mockup;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.ERROR.ErrorPacket;
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
import org.jline.terminal.Terminal;
import org.jline.utils.*;

import java.util.*;

public class TextTerminal extends UserInputInterface {
    /**The key of the commands is the string representing the command type, such as "help" or "draw",
     * the rest of the following strings are used as parameters to specify the behaviour in the function of the called TerminalCommand*/
    private static final Map<String, TerminalCommand> commands = new HashMap<>();
    private static final StringBuilder manual = new StringBuilder();
    private static final Terminal terminal = ClientApplication.terminal;
    private static final LineReader lineReader = ClientApplication.lineReader;
    private static int rows = 35;
    private static int columns = 130;
    private static final int SCROLL_REGION_HEIGHT = 12;
    private static final int LINE_PREVIOUS_MESSAGE = rows-6;
    private static final int LINE_ERROR = rows-4;

    private final Object cursorLock = new Object();

    static {
        terminal.handle(Terminal.Signal.WINCH, _ -> {
            columns = terminal.getWidth();
            rows = terminal.getHeight();
        });
    }
    private String previousMessage = "";
    private String message = "";

    private static final Map<String,Totem> totems = new HashMap<>();
    static {
        for(Totem totem : Totem.values()){
            totems.put(totem.name().toLowerCase(), totem); //for example, totems.put("orange", Totem.ORANGE);
        }
    }

    static {
        // Mostra la manuale di istruzioni per il gioco
        commands.put("help", ( _, _, _, _ ) -> terminal.writer().println(manual));

        //Chiude l'applicazione e si disconnette dal serviente
        commands.put("disconnect", ( _, _, _, terminalVirtualServer ) -> {
            terminal.writer().println("| Are you sure you want disconnect from the server?   |");
            terminal.writer().println("| You'll have to restart the application to reconnect |");
            if( lineReader.readLine("  Type YES to confirm: ").equalsIgnoreCase("yes") ){
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
                    terminal.writer().println("please, specify correctly the number of players!");
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
                    terminal.writer().println(terminalMockups.getHall());
                }else{
                    terminal.writer().println("Hall not yet loaded.");
                }
            }else if(terminalPhase == ApplicationPhase.ROOM) {
                if(terminalMockups.getRoom() != null) {
                    terminal.writer().println(terminalMockups.getRoom());
                    terminal.writer().println(terminalMockups.getRoom().toStringPlayers());
                }else{
                    terminal.writer().println("Room not yet loaded.");
                }
            }else if(terminalPhase == ApplicationPhase.GAME) {
                if(terminalMockups.getGame() != null) {
                    for (MockupPlayer p : terminalMockups.getGame().getPlayers()) {
                        terminal.writer().println(p);
                    }
                }else{
                    terminal.writer().println("Game not yet loaded.");
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
                    terminal.writer().println("please, insert a valid number!");
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
                    terminal.writer().println("please, insert a valid number!");
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
        commands.put("draw", ( terminalPhase, _, terminalParameters, terminalVirtualServer ) -> {
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
        manual.append(formatCommand("disconnect", "Disconnects you from the server and exits the application"));
        //HALL commands
        manual.append("-".repeat(125)).append("\n");
        manual.append(String.format("%70s\n", "HALL commands:"));
        manual.append(formatCommand("create (\"your room name\") (num. players)", "Creates a waiting room with the chosen room's name"));
        manual.append(formatCommand("", "(in Quotation marks for a name with whitespaces)"));
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
    }
    private static String formatCommand(String command, String description) {return String.format(" %60s | %s\n", command, description);}


    public TextTerminal (VirtualServer virtualServer , Mockup mockups, ApplicationPhase currentPhase) {
        super( virtualServer, mockups, currentPhase);
    }

    public void printString ( String string ) {
        printMessage(string);
    }
    public void printErrorPacket ( ErrorPacket errorPacket ) {
        printError(errorPacket);
    }


    @Override
    public void setCurrentPhase ( ApplicationPhase phase ) {
        super.setCurrentPhase(phase);
        switch (phase) {
            case GAME:
                printMessage("The game started.");
                break;
            case HALL:
                printMessage("You entered the hall.");
                break;
            case ROOM:
                printMessage("-You entered a room.");
                break;
        }
    }

    @Override
    public void runInput() {
        try {
            synchronized (cursorLock) {
                terminal.puts(InfoCmp.Capability.clear_screen);
                terminal.writer().println("Terminal started (type help for the list of commands):\n");

                terminal.puts(InfoCmp.Capability.cursor_address, rows - 3, 1);
                AttributedString promptSeparator = new AttributedString("━".repeat(columns - 2), AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE));
                promptSeparator.print(terminal);
                terminal.puts(InfoCmp.Capability.cursor_address, SCROLL_REGION_HEIGHT, 1);
                promptSeparator.print(terminal);
            }

            // Set scroll region from first to scrollRegionHeight
            //the scroll region is where the previous user commands can be seen in
            terminal.puts(InfoCmp.Capability.change_scroll_region, 1, SCROLL_REGION_HEIGHT - 1);


            //noinspection InfiniteLoopStatement
            while (true) {
                synchronized (cursorLock) {
                    rows = terminal.getHeight();
                    // Move cursor to prompt line to get ready for a read
                    moveCursorToPrompt();
                }

                // Read input
                String inputLine = lineReader.readLine("> ").toLowerCase();

                synchronized (cursorLock) {
                    if (!inputLine.isEmpty()) {
                        // Move cursor to scroll region, where commands should print
                        moveCursorToScroll();
                        terminal.writer().println();
                        // Print the command inserted
                        terminal.writer().println(inputLine);

                        // Split the string by whitespace
                        String[] parts = inputLine.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                        // The first word is the key
                        String action = parts[0];
                        // The remaining words are parameters
                        String[] params = Arrays.copyOfRange(parts, 1, parts.length);

                        // Look up the terminal function and execute
                        TerminalCommand command = commands.get(action);
                        if (command != null) {
                            command.execute(currentPhase, mockups, params, virtualServer);
                        } else {
                            terminal.writer().print("Unknown command: " + action);
                        }
                        terminal.flush();

                        // Restores cursor to prompt position
                        moveCursorToPrompt();
                        terminal.puts(InfoCmp.Capability.clr_eol); //clear the prompt line
                    }
                }
            }

        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }

    private void moveCursorToPrompt() {
        terminal.puts(InfoCmp.Capability.cursor_address, rows - 2, 1);
    }
    private void moveCursorToScroll() {
        terminal.puts(InfoCmp.Capability.cursor_address, SCROLL_REGION_HEIGHT - 1, 1);
    }
    private void printMessage(String newMessage){
        synchronized (cursorLock) {
            previousMessage = message;
            message = newMessage;
            terminal.puts(InfoCmp.Capability.save_cursor);
            terminal.puts(InfoCmp.Capability.cursor_address, LINE_PREVIOUS_MESSAGE, 1);
            terminal.puts(InfoCmp.Capability.clr_eol);
            terminal.writer().print("Previous message: " + previousMessage);
            terminal.puts(InfoCmp.Capability.cursor_address, LINE_PREVIOUS_MESSAGE+1, 1);
            terminal.puts(InfoCmp.Capability.clr_eol);
            terminal.writer().print("Message: " + message);
            terminal.flush();
            terminal.puts(InfoCmp.Capability.restore_cursor);
        }
    }
    private void printError(ErrorPacket errorPacket) {
        synchronized (cursorLock) {
            String errorString = new StringBuilder().append("error,").append("[").append(errorPacket.errorTitle).append("]: ").append(errorPacket.errorContent).toString();
            terminal.puts(InfoCmp.Capability.save_cursor);
            terminal.puts(InfoCmp.Capability.cursor_address, LINE_ERROR, 1);
            terminal.puts(InfoCmp.Capability.clr_eol);
            terminal.writer().print(errorString);
            terminal.flush();
            terminal.puts(InfoCmp.Capability.restore_cursor);
            if (errorPacket.forceDisconnection) {
                System.exit(-1);
            }
        }
    }
    @Override
    public void show(){
        synchronized (cursorLock) {
            terminal.puts(InfoCmp.Capability.save_cursor);
            for( int i = SCROLL_REGION_HEIGHT+1; i < LINE_PREVIOUS_MESSAGE; i++ ) {
                terminal.puts(InfoCmp.Capability.cursor_address, i, 1);
                terminal.puts(InfoCmp.Capability.clr_eol);
            }
            terminal.puts(InfoCmp.Capability.cursor_address, SCROLL_REGION_HEIGHT+1, 1);
            switch (currentPhase) {
                case HALL:
                    if(mockups.getHall() != null) {
                        terminal.writer().println(mockups.getHall());
                    }else{
                        terminal.writer().println("Hall not yet loaded.");
                    }
                    break;
                case ROOM:
                    if(mockups.getRoom() != null) {
                        terminal.writer().println(mockups.getRoom());
                        terminal.writer().println(mockups.getRoom().toStringPlayers());
                    }else{
                        terminal.writer().println("Room not yet loaded.");
                    }
                    break;
                case GAME:
                    break;
            }
            terminal.flush();
            terminal.puts(InfoCmp.Capability.restore_cursor);
        }
    }
}