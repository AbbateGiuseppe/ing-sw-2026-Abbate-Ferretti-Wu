package it.polimi.ingsw.gc49.client.user_input_interfaces;

import it.polimi.ingsw.gc49.client.ClientApplication;
import it.polimi.ingsw.gc49.client.view.RectangleAttributedString;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupGame;
import it.polimi.ingsw.gc49.client.view.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToClient.ERROR.ErrorPacket;
import it.polimi.ingsw.gc49.server.controller.PlayerActionEnum;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ANY_phase.DISCONNECT.DisconnectPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.CREATE.HallCreatePacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.HALL_phase.HALL_COMMAND.JOIN.HallJoinPacket;
import it.polimi.ingsw.gc49.rmi_socket.datapacket.directedToServer.ROOM_phase.ROOM_COMMAND.LEAVE.RoomLeavePacket;
import it.polimi.ingsw.gc49.server.model.Card.Card;
import it.polimi.ingsw.gc49.server.model.Totem;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;
import org.jline.reader.LineReader;
import org.jline.terminal.Cursor;
import org.jline.terminal.Terminal;
import org.jline.utils.*;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TextTerminal extends UserInputInterface {
    /**The key of the commands is the string representing the command type, such as "help" or "draw",
     * the rest of the following strings are used as parameters to specify the behaviour in the function of the called TerminalCommand*/
    private static final Map<String, TerminalCommand> commands = new HashMap<>();
    private static final StringBuilder manual = new StringBuilder();
    private static final AttributedStringBuilder legend = new AttributedStringBuilder();
    private static final Terminal terminal = ClientApplication.terminal;
    private static final LineReader lineReader = ClientApplication.lineReader;
    private static final ScheduledExecutorService helpScheduler = Executors.newSingleThreadScheduledExecutor();
    private static final int HELP_TIMEOUT = 10;
    private static int rows = 35;
    private static int columns = 130;
    private static final int SCROLL_REGION_HEIGHT = 12;
    private static final int LINE_SHOW = SCROLL_REGION_HEIGHT+1;
    private static final int LINE_PREVIOUS_MESSAGE = rows-6;
    private static final int LINE_ERROR = rows-4;

    private static final Object cursorLock = new Object();

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
        commands.put("help", ( terminalMethods, _, _, _, _ ) -> terminalMethods.printManual());

        //Chiude l'applicazione e si disconnette dal serviente
        commands.put("disconnect", ( terminalMethods, _, _, _, terminalVirtualServer ) -> {
            terminalMethods.printScroll("| Are you sure you want disconnect from the server? You'll have to restart the application to reconnect |");
            terminalMethods.printScroll("|                                     Type YES to confirm                                               |");
            if (terminalMethods.readInput().equalsIgnoreCase("yes")) {
                terminalVirtualServer.disconnect(new DisconnectPacket());
                System.exit(0);
            }
        });

        // Creare una stanza
        commands.put("create", ( terminalMethods, terminalPhase, terminalMockups, terminalParameters, terminalVirtualServer ) -> {
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
                    terminalMethods.printScroll("please, specify correctly the number of players!");
                }
            }
        });
        // Entrare una stanza
        commands.put("join", ( _, terminalPhase, terminalMockups, terminalParameters, terminalVirtualServer ) -> {
            if(terminalPhase == ApplicationPhase.HALL && terminalParameters.length >= 1) {
                String roomName = terminalParameters[0];
                // Controlla se la stanza esiste
                if (terminalMockups.getHall().rooms.stream().map(r -> r.roomName).anyMatch(s -> s.equals(roomName))) {
                    HallJoinPacket hallJoinPacket = new HallJoinPacket(roomName);
                    terminalVirtualServer.joinRoom(hallJoinPacket);
                }
            }
        });


        // Uscire da una stanza
        commands.put("leave", ( _, terminalPhase, _, _, terminalVirtualServer ) -> {
            if(terminalPhase == ApplicationPhase.ROOM) {
                terminalVirtualServer.leaveRoom(new RoomLeavePacket());
            }
        });


        // Mostra le carte di un giocatore dato il suo indice
        commands.put("cards", ( terminalMethods, terminalPhase, terminalMockups, terminalParameters, _ ) -> {
            if(terminalPhase == ApplicationPhase.GAME && terminalParameters.length >= 1) {
                try {
                    int index = Integer.parseInt(terminalParameters[0]);
                    if(index < terminalMockups.getGame().getPlayers().size() && index >= 0) {
                        terminalMockups.getGame().getPlayer(index).printCards();
                    }else{
                        throw new NumberFormatException();
                    }
                } catch (NumberFormatException e) {
                    terminalMethods.printScroll("please, insert a valid number!");
                }
            }
        });
        // Sceglie una carta
        commands.put("draw", ( terminalMethods, terminalPhase, _, terminalParameters, terminalVirtualServer ) -> {
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
                        terminalMethods.printScroll("not a valid card choice!");
                    }
                } catch (NumberFormatException e) {
                    terminalMethods.printScroll("please, insert a valid number!");
                }
            }
        });
        // Sceglie un totem
        commands.put("legend", ( terminalMethods, terminalPhase, _, _, _ ) -> {
            if(terminalPhase == ApplicationPhase.GAME) {
                terminalMethods.printLegend();
            }
        });
        // Sceglie una offerta
        commands.put("offer", ( terminalMethods, terminalPhase, _, terminalParameters, terminalVirtualServer ) -> {
            if(terminalPhase == ApplicationPhase.GAME && terminalParameters.length >= 1) {
                try {
                    int offerIndex = Integer.parseInt(terminalParameters[0]);
                    CommandPacket commandPacket = new CommandPacket(PlayerActionEnum.CHOOSE_OFFER, offerIndex);
                    terminalVirtualServer.sendCommand(commandPacket);
                } catch (NumberFormatException e) {
                    terminalMethods.printScroll("please, insert a valid number!");
                }
            }
        });
        // Sceglie un totem
        commands.put("totem", ( _, terminalPhase, _, terminalParameters, terminalVirtualServer ) -> {
            if(terminalPhase == ApplicationPhase.GAME && terminalParameters.length >= 1) {
                if (totems.containsKey(terminalParameters[0].toLowerCase())) {
                    Totem totem = totems.get(terminalParameters[0].toLowerCase());
                    CommandPacket commandPacket = new CommandPacket(PlayerActionEnum.CHOOSE_TOTEM,totem);
                    terminalVirtualServer.sendCommand(commandPacket);
                }
            }
        });
    }

    static {
        // Command entries
        //GENERAL/ANY commands
        manual.append(String.format("%72s\n", "GENERAL commands:"));
        manual.append(manualFormat("help", "Displays this help message for " + HELP_TIMEOUT + " seconds"));
        manual.append(manualFormat("disconnect", "Disconnects you from the server and exits the application"));
        //HALL commands
        manual.append(manualSeparator()).append("\n");
        manual.append(String.format("%70s\n", "HALL commands:"));
        manual.append(manualFormat("create/join [\"your room name\"] (num. players)", "Creates/Joins a waiting room"));
        //ROOM commands
        manual.append(manualSeparator()).append("\n");
        manual.append(String.format("%70s\n", "ROOM commands:"));
        manual.append(manualFormat("leave", "Leaves the current room"));
        //GAME commands
        manual.append(manualSeparator()).append("\n");
        manual.append(String.format("%70s\n", "GAME commands:"));
        manual.append(manualFormat("totem [orange/white/blue/black/yellow]", "Choose the specified totem"));
        manual.append(manualFormat("offer [offer index]", "Choose the specified offer"));
        manual.append(manualFormat("cards [player index]", "Displays the cards of the specified player"));
        manual.append(manualFormat("draw [lower/upper] [character/building] [card index]", "Draw the specified card"));

        manual.append(manualSeparator());
    }
    private static String manualSeparator () { return " "+"-".repeat(127); }
    private static String manualFormat ( String command, String description) {return " " + String.format(" %60s | %s\n", command, description);}
    static {
        legend.append(" Symbols meaning:\n");
        legend.append(" ♥ food    | ░ free space                       | card types by color:\n");
        legend.append(" ♦ points  | ▓ space occupied by totem          | ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append("╬")
                .style(AttributedStyle.DEFAULT).append(" character card\n");
        legend.append(" ✶ stars   |__                                  | ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN)).append("╬")
                .style(AttributedStyle.DEFAULT).append(" building card\n");
        legend.append(" ≥ at end game|_________________________________| ")
                .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA)).append("╬")
                .style(AttributedStyle.DEFAULT).append(" event card\n");
        legend.append(" ● at set completion   |  character types:      |\n");
        legend.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.RED)).append(" ▼")
                .style(AttributedStyle.DEFAULT).append(" drawable lower cards|  A = artist            |\n");
        legend.style(AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN)).append(" ▲")
                .style(AttributedStyle.DEFAULT).append(" drawable upper cards|  B = builder           |\n");
        legend.append(" ¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯¯|  G = gatherer          |\n");
        legend.append(" event types:          |  H = hunter            |\n");
        legend.append(" ¤ painting            |  I = inventor          |\n");
        legend.append(" § ritual              |  S = shaman            |\n");
        legend.append(" % hunt                |\n");
        legend.append(" € sustenance          |\n");
    }

    public TextTerminal (VirtualServer virtualServer, ApplicationPhase currentPhase) {
        super( virtualServer, currentPhase);
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
                printStatus();
                break;
            case HALL:
                printMessage("You entered the hall.");
                printStatus();
                break;
            case ROOM:
                printMessage("You entered a room.");
                printStatus();
                break;
        }
    }

    @Override
    public void runInput() {
        try {
            synchronized (cursorLock) {
                terminal.puts(InfoCmp.Capability.clear_screen);
                terminal.writer().println("Terminal started (type help for the list of commands):\n");

                // Set scroll region from first to scrollRegionHeight
                //the scroll region is where the previous user commands can be seen in
                terminal.puts(InfoCmp.Capability.change_scroll_region, 1, SCROLL_REGION_HEIGHT - 1);

                // Draw the separators between the terminal's sections
                terminal.puts(InfoCmp.Capability.cursor_address, rows - 3, 1);
                AttributedString promptSeparator = new AttributedString("━".repeat(columns - 2), AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE));
                promptSeparator.print(terminal);
                terminal.puts(InfoCmp.Capability.cursor_address, SCROLL_REGION_HEIGHT, 1);
                promptSeparator.print(terminal);
            }

            printStatus();
            show();

            // Initialization finished, start the input loop
            //noinspection InfiniteLoopStatement
            while (true) {

                // Read input
                String inputLine = readInput();


                if (!inputLine.isEmpty()) {
                    // Print the inserted input
                    printScroll(inputLine);


                    // Split the string by whitespace
                    String[] parts = splitInput(inputLine);
                    // The first word is the key
                    assert parts != null;
                    String action = parts[0];
                    // The remaining words are parameters
                    String[] params = Arrays.copyOfRange(parts, 1, parts.length);

                    // Look up the terminal function and execute
                    TerminalCommand command = commands.get(action);
                    if (command != null) {
                        command.execute(this, currentPhase, mockups, params, virtualServer);
                    } else {
                        printScroll("Unknown command: " + action);
                    }
                    printScroll("");
                }

            }

        } catch (Exception e) {
            printScroll("Client exception: " + e);
            e.printStackTrace();
        }
    }

    private String readInput() {
        synchronized (cursorLock) {
            // Move cursor to prompt line to get ready for a read
            rows = terminal.getHeight();
            terminal.puts(InfoCmp.Capability.cursor_address, rows - 2, 1);
            terminal.writer().print("> ");
            terminal.flush();
        }
        String input = lineReader.readLine("").toLowerCase();
        synchronized (cursorLock) {
            // Clear the prompt line
            rows = terminal.getHeight();
            terminal.puts(InfoCmp.Capability.cursor_address, rows - 2, 0);
            terminal.puts(InfoCmp.Capability.clr_eol);
        }
        return input;
    }
    private String[] splitInput(String input) {
        if(input.isEmpty()) return null;

        List<String> parts = new ArrayList<>();
        StringBuilder currentPart = new StringBuilder();

        boolean quoting = false; //activated when a quotation is opened
        for(int i=0; i<input.length(); i++) {
            char c = input.charAt(i);

            // Open or Close quotation
            if( c == '\"' ){
                quoting = !quoting;
            }
            else if(Character.isWhitespace(c) && !quoting) {
                // Close and store the current part, because it has been separated without quotation
                parts.add(currentPart.toString());
                currentPart.setLength(0); //reset the buffer
            }
            else{
                // Append the character to the current part
                currentPart.append(c);
            }
        }
        // Add the final computed part
        if (!currentPart.isEmpty()) {
            parts.add(currentPart.toString());
        }

        return parts.toArray(new String[0]);
    }
    private void printRectangleString( RectangleAttributedString rectangleAttributedString ){
        synchronized (cursorLock) {

            Cursor cursor = terminal.getCursorPosition(_ -> {});
            int initialY = cursor.getY();
            int initialX = cursor.getX();
            int height = rectangleAttributedString.height;
            int width = rectangleAttributedString.width;
            for (int currentLine = 0; currentLine < height; currentLine++) {
                terminal.puts(InfoCmp.Capability.cursor_address, initialY + currentLine, initialX);
                rectangleAttributedString.attributedString.subSequence(currentLine * width, (currentLine * width) + width).print(terminal);
            }

            terminal.puts(InfoCmp.Capability.cursor_address, initialY, initialX);
        }
    }
    private void printScroll(String toPrint){
        synchronized (cursorLock) {
            terminal.puts(InfoCmp.Capability.save_cursor);
            // Move cursor to scroll region, where commands should print
            terminal.puts(InfoCmp.Capability.cursor_address, SCROLL_REGION_HEIGHT - 1, 1);
            // Print the string inserted
            terminal.writer().println(toPrint);
            // Restores cursor position
            terminal.puts(InfoCmp.Capability.restore_cursor);
        }
    }
    private void printManual () {
        synchronized (cursorLock) {
            terminal.puts(InfoCmp.Capability.cursor_address, LINE_SHOW, 0);
            terminal.writer().println(manual);
            helpScheduler.schedule(this::show, HELP_TIMEOUT, TimeUnit.SECONDS);
        }
    }
    private void printLegend () {
        synchronized (cursorLock) {
            terminal.puts(InfoCmp.Capability.cursor_address, LINE_SHOW, 0);
            legend.toAttributedString().print(terminal);
            helpScheduler.schedule(this::show, HELP_TIMEOUT, TimeUnit.SECONDS);
        }
    }
    private void printMessage(String newMessage){
        synchronized (cursorLock) {
            previousMessage = message;
            message = newMessage;
            terminal.puts(InfoCmp.Capability.save_cursor);
            terminal.puts(InfoCmp.Capability.cursor_address, LINE_PREVIOUS_MESSAGE, 1);
            terminal.puts(InfoCmp.Capability.clr_eol);
            new AttributedStringBuilder()
                    .style(AttributedStyle.DEFAULT.underline()).append("Previous message")
                    .style(AttributedStyle.DEFAULT).append(": ").append(previousMessage)
                    .toAttributedString().print(terminal);
            terminal.puts(InfoCmp.Capability.cursor_address, LINE_PREVIOUS_MESSAGE+1, 1);
            terminal.puts(InfoCmp.Capability.clr_eol);
            new AttributedStringBuilder()
                    .style(AttributedStyle.DEFAULT.underline()).append("Last message")
                    .style(AttributedStyle.DEFAULT).append(": ").append(message)
                    .toAttributedString().print(terminal);
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
            terminal.puts(InfoCmp.Capability.restore_cursor);
            if (errorPacket.forceDisconnection) {
                System.exit(-1);
            }
        }
    }
    private void printStatus() {
        synchronized (cursorLock) {
            AttributedString status = new AttributedStringBuilder()
                    .append("[").style(AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW)).append(nickname)
                    .style(AttributedStyle.DEFAULT.foreground(AttributedStyle.WHITE)).append("]")
                    .append(" | position: ").style(AttributedStyle.DEFAULT.bold().foreground(AttributedStyle.RED))
                    .append(currentPhase.toString())
                    .toAttributedString();
            terminal.puts(InfoCmp.Capability.save_cursor);
            terminal.puts(InfoCmp.Capability.cursor_address, rows-1, 1);
            terminal.puts(InfoCmp.Capability.clr_eol);
            status.print(terminal);
            terminal.puts(InfoCmp.Capability.restore_cursor);
        }
    }
    @Override
    public void show(){
        synchronized (cursorLock) {
            terminal.puts(InfoCmp.Capability.save_cursor);
            for( int i = LINE_SHOW; i < LINE_PREVIOUS_MESSAGE; i++ ) {
                terminal.puts(InfoCmp.Capability.cursor_address, i, 0);
                terminal.puts(InfoCmp.Capability.clr_eol);
            }
            terminal.puts(InfoCmp.Capability.cursor_address, LINE_SHOW, 1);
            switch (currentPhase) {
                case HALL:
                    if(mockups.getHall() != null) {
                        mockups.getHall().toAttributedString().println(terminal);
                    }else{
                        terminal.writer().println("Hall not yet loaded.");
                    }
                    break;
                case ROOM:
                    if(mockups.getRoom() != null) {
                        mockups.getRoom().toAttributedString().println(terminal);
                        mockups.getRoom().toAttributedStringPlayers().println(terminal);
                    }else{
                        terminal.writer().println("Room not yet loaded.");
                    }
                    break;
                case GAME:
                    if(mockups.getGame() != null) {
                        printGame();
                    }else{
                        terminal.writer().println("Game not yet loaded.");
                    }
                    break;
            }
            terminal.puts(InfoCmp.Capability.restore_cursor);
        }
    }

    private void printGame() {
        synchronized (cursorLock) {
            MockupGame mockupGame = mockups.getGame();
            Cursor cursor = terminal.getCursorPosition(_ -> {});
            int startingCursorY = cursor.getY();
            int startingCursorX = cursor.getX();
            int cursorY;
            int cursorX;

            //Draw upperLine
            cursorY = startingCursorY;
            cursorX = startingCursorX + 14;
            terminal.puts(InfoCmp.Capability.cursor_address, cursorY, cursorX);
            if(mockupGame.getUpperLine() != null) {
                for (Card card : mockupGame.getUpperLine()) {
                    printRectangleString(card.getRectangleAttributedString());
                    cursorX = cursorX + card.getRectangleAttributedString().width;
                    terminal.puts(InfoCmp.Capability.cursor_address, cursorY, cursorX);
                }
            }else{
                terminal.writer().println("Non esiste la fila superiore");
            }
            //Draw lowerLine
            cursorY = startingCursorY + 10;
            cursorX = startingCursorX + 14;
            terminal.puts(InfoCmp.Capability.cursor_address, cursorY, cursorX);
            if(mockupGame.getLowerLine() != null) {
                for (Card card : mockupGame.getLowerLine()) {
                    printRectangleString(card.getRectangleAttributedString());
                    cursorX = cursorX + card.getRectangleAttributedString().width;
                    terminal.puts(InfoCmp.Capability.cursor_address, cursorY, cursorX);
                }
            }else{
                terminal.writer().println("Non esiste la fila inferiore");
            }


            //Draw orderSlots
            cursorY = startingCursorY+4;
            cursorX = startingCursorX+3;
            terminal.puts(InfoCmp.Capability.cursor_address, cursorY, cursorX);
            terminal.writer().print("Order");
            terminal.puts(InfoCmp.Capability.cursor_address, ++cursorY, cursorX);
            terminal.writer().print("╔════════╗");
            int playerNum = 1;
            cursorX = startingCursorX;
            for (RectangleAttributedString rectangleAttributedString : mockupGame.getOrderBoardRectangleStrings()) {
                terminal.puts(InfoCmp.Capability.cursor_address, ++cursorY, cursorX);
                terminal.writer().print(playerNum + ". ║");
                printRectangleString(rectangleAttributedString);
                terminal.puts(InfoCmp.Capability.cursor_address, cursorY, cursorX+12);
                terminal.writer().print("║");
                playerNum++;
            }
            cursorX = startingCursorX+3;
            terminal.puts(InfoCmp.Capability.cursor_address, ++cursorY, cursorX);
            terminal.writer().print("╚════════╝");

            //Draw offerBoard
            cursorY = startingCursorY+4;
            cursorX = startingCursorX+14;
            terminal.puts(InfoCmp.Capability.cursor_address, cursorY, cursorX);
            terminal.writer().print("Offers");
            terminal.puts(InfoCmp.Capability.cursor_address, ++cursorY, cursorX);
            for (RectangleAttributedString rectangleAttributedString : mockupGame.getOfferBoardRectangleStrings()) {
                printRectangleString(rectangleAttributedString);
                cursorX = cursorX + rectangleAttributedString.width;
                terminal.puts(InfoCmp.Capability.cursor_address, cursorY, cursorX);
            }

            //Draw player stats
            cursorY = startingCursorY + 14;
            cursorX = startingCursorX;
            terminal.puts(InfoCmp.Capability.cursor_address, cursorY, cursorX);
            for(MockupPlayer mockupPlayer : mockupGame.getPlayers()){
                mockupPlayer.displayAttributedString().print(terminal);
            }
            //01.              ╔═══╗╔═══╗╔═══╗╔═══╗╔═══╗╔═══╗╔═══╗╔═══╗╔═══╗   ╔hthtrhtr╗
            //02.              ║ I ║║B2♥║║ G ║║   ║║ S ║║ H ║║│%│║║│€│║║ H ║ │ ╚grgrgrgr╝[kuykyuky][hthgeqnz]
            //03.              ║(3)║║ 3♦║║3♥ ║║ P ║║3✶ ║║♥♥♥║║└─┘║║└─┘║║   ║ │ ╔12345678╗[htfhhgtr]
            //04.              ╚═══╝╚═══╝╚═══╝╚═══╝╚═══╝╚═══╝╚═══╝╚═══╝╚═══╝   ╚90123456╝
            //05.   Ordine     Offerte                             Mazzo
            //06.   ╔════════╗ ╔═══╗╔═══╗╔═══╗╔═══╗╔═══╗╔═══╗╔═══╗ ╔═══╗
            //07.1. ║░ 3♥    ║ ║ ░ ║║ ░ ║║ ░ ║║ ░ ║║ ░ ║║ ░ ║║ ░ ║ ║Era║
            //08.2. ║░ 1♥    ║ ║ 3♥║║ 1▼║║ 1▲║║ 2▼║║ 1▼║║ 2▲║║ 1▼║ ║ 3 ║
            //09.3. ║░       ║ ║   ║║   ║║   ║║   ║║ 1▲║║   ║║ 2▲║ ║   ║
            //10.4. ║░       ║ ╚═══╝╚═══╝╚═══╝╚═══╝╚═══╝╚═══╝╚═══╝ ╚═══╝
            //11.5. ║░-1♥/-2♦║ ╔═══╗╔═══╗╔═══╗╔═══╗╔═══╗╔═══╗                  Carte edificio inf.
            //12.   ╚════════╝ ║ A ║║│¤│║║ G ║║│§│║║ G ║║●3♥║                │ [gregrjyt][jyjetjru]
            //13.              ║   ║║└─┘║║3♥ ║║└─┘║║3♥ ║║ 2♦║                │
            //14.              ╚═══╝╚═══╝╚═══╝╚═══╝╚═══╝╚═══╝
            //15.▓[Mario118]: 3♥/5♦ |▓[Robertoni]: 3♥/5♦ |▓[RoxxoXxX]: 3♥/5♦
            //16.▓[CaioSempronio]: 3♥/5♦ |▓[Marta]: 3♥/5♦
        }
    }
}