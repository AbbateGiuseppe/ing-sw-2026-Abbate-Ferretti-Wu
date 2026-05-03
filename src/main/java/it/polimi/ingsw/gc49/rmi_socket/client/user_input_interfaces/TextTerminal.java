package it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces;

import it.polimi.ingsw.gc49.View.Mockup;
import it.polimi.ingsw.gc49.View.mockupModel.MockupPlayer;
import it.polimi.ingsw.gc49.controller.massi.MassiPlayerActionEnum;
import it.polimi.ingsw.gc49.datapacket.directedToServer.GAME_phase.COMMAND.CommandPacket;
import it.polimi.ingsw.gc49.model.Totem;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.ApplicationPhase;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class TextTerminal extends UserInputInterface {
    private static int commandIndex = 0;
    /**The key of the commands is the string representing the command type, such as "help" or "draw",
     * the rest of the following strings are used as parameters to specify the behaviour in the function of the called TerminalCommand*/
    private static final Map<String, TerminalCommand> commands = new HashMap<>();
    private static StringBuilder manual = new StringBuilder();

    /*
    public void processInput(String input) {
        // 1. Split the string by whitespace
        String[] parts = input.split("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
        if (parts.length == 0) return;

        // 2. The first word is the key
        String action = parts[0].toLowerCase();

        // 3. The remaining words are parameters
        String[] params = Arrays.copyOfRange(parts, 1, parts.length);

        // 4. Look up and execute
        TerminalCommand command = commands.get(action);
        if (command != null) {
            command.execute(mockups, params);
        } else {
            System.out.println("Unknown command: " + action);
        }
    }
 */
    static {
        commands.put("help", ( terminalPhase, terminalMockups, terminalParameters ) ->  {
            if(terminalPhase == ApplicationPhase.GAME){
                printManual();
            }}
        );
        // Mostra i cibi e i punti di tutti i giocatori
        commands.put("status", ( terminalPhase, terminalMockups, terminalParameters ) -> {
            if(terminalPhase == ApplicationPhase.GAME) {
                for (MockupPlayer p : terminalMockups.getGame().getPlayers()) {
                    // Guarda toString() di MockupPlayer
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

    }

    public TextTerminal ( VirtualServer virtualServer , Mockup mockups, ApplicationPhase currentPhase) {
        super( virtualServer, mockups, currentPhase);
    }

    @Override
    public void runInput() throws Exception {
        try {
            boolean play = true;
            Scanner scanner = new Scanner(System.in);
            MassiPlayerActionEnum currentAction = MassiPlayerActionEnum.CONNECT;
            int index;
            Map<String,Totem> totems = Map.of(
                    "orange", Totem.ORANGE,
                    "yellow", Totem.YELLOW,
                    "black", Totem.BLACK,
                    "white",Totem.WHITE
            );

            System.out.println("\nTerminal started:\ntype help for the list of commands");
            while (play) {

                ResetCommand();
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


                /*
                // I seguenti comandi sono azioni di gioco proprie
                if(checkCommand("draw",elems)){
                    if(checkCommand("upper",elems)){
                        if(checkCommand("character",elems)) {
                            currentAction = MassiPlayerActionEnum.DRAW_UPPER_CHARACTER;
                        }
                        else if(checkCommand("building",elems)) {
                            currentAction = MassiPlayerActionEnum.DRAW_UPPER_BUILDING;
                        }
                    }else if(checkCommand("lower",elems)){
                        if(checkCommand("character",elems)) {
                            currentAction = MassiPlayerActionEnum.DRAW_LOWER_CHARACTER;
                        }
                        else if(checkCommand("building",elems)) {
                            currentAction = MassiPlayerActionEnum.DRAW_LOWER_BUILDING;
                        }
                    }
                }
                else if(checkCommand("offer",elems)){
                    currentAction = MassiPlayerActionEnum.CHOOSE_OFFER;
                }
                else if(checkCommand("totem",elems)){
                    currentAction = MassiPlayerActionEnum.CHOOSE_TOTEM;
                }
                else if(checkCommand("leave",elems)) {
                    currentAction = MassiPlayerActionEnum.DISCONNECT;
                    play = false;
                    break;
                }
                // TODO:mancano azioni per prepartita

                command = new CommandPacket(currentAction);

                if(checkCommand("draw",elems) || checkCommand("offer",elems)) {
                    index = Integer.parseInt(elems[elems.length - 1]);
                    command = new CommandPacket(currentAction,index);
                } else if(checkCommand("totem",elems)) {
                    Totem totem = totems.get(elems[elems.length - 1].toLowerCase());
                    command = new CommandPacket(currentAction,totem);
                }

                virtualServer.sendCommand(command);
                System.out.println("Sent " + currentAction + " to server");

                 */
            }


        } catch (Exception e) {
            System.err.println("Client exception: " + e);
            e.printStackTrace();
        }
    }

    private static boolean checkCommand(String command, String[] input){
        if(command.indexOf(input[commandIndex])==0){
            commandIndex++;
            return true;
        }
        return false;
    }

    private static void ResetCommand(){
        commandIndex = 0;
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
    private static void printManual() {
        System.out.println(manual);
    }

    private static String formatCommand(String command, String description) {
        return String.format(" %60s | %s\n", command, description);
    }
}