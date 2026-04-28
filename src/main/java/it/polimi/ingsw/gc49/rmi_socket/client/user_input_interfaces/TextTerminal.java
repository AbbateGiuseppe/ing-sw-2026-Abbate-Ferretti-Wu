package it.polimi.ingsw.gc49.rmi_socket.client.user_input_interfaces;

import it.polimi.ingsw.gc49.controller.massi.MassiPlayerActionEnum;
import it.polimi.ingsw.gc49.rmi_socket.virtualMethods.virtualServers.VirtualServer;

import java.util.Scanner;

public class TextTerminal extends UserInputInterface {
    //protected VirtualServer virtualServer; <---From parent class
    private static int commandIndex = 0;

    public TextTerminal ( VirtualServer virtualServer, String nickname ) {
        super(virtualServer, nickname );
    }

    /*@Override  //NOT IN USE ANYMORE
    public void runInput() throws Exception {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            String inputLine = scan.nextLine();

            Datapacket dataPacket = TextParser.parse( inputLine );
            switch (dataPacket.getDatapacketType()){
                case COMMAND:
                    virtualServer.sendCommand((Command) dataPacket);
                    break;
                default:
                    break;
            }
        }
    }*/

    @Override
    public void runInput() throws Exception {
        try {
            boolean play = true;
            Scanner scanner = new Scanner(System.in);
            MassiPlayerActionEnum currentAction = MassiPlayerActionEnum.CONNECT;

            while (play) {

                ResetCommand();
                System.out.println("Try Commands");
                String textCommand = scanner.nextLine();
                textCommand.toLowerCase();
                String[] elems = textCommand.split(" ");
                int pos = 0;
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

                //TODO: virtualServer.sendCommand();
                //virtualServer.sendCommand(new CommandPacket(currentAction, 2)); //Esempio

                System.out.println("Sent " + currentAction + " to server");
            }


        } catch (Exception e) {
            System.err.println("Client exception: " + e.toString());
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
}
