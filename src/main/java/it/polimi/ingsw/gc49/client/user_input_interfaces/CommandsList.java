package it.polimi.ingsw.gc49.client.user_input_interfaces;

import it.polimi.ingsw.gc49.ItaEngString;

/** 'p' stands for parameter, 'r' stands for reply, 'et' stands for error type, 'em' stands for error message */
public class CommandsList {
    public final static ItaEngString HELP = new ItaEngString("aiuto", "help");

    public final static ItaEngString DISCONNECT = new ItaEngString("disconnetti", "disconnect");
    public final static ItaEngString DISCONNECT_r_01 = new ItaEngString("| Sei sicuro di volerti disconnettere dal serviente? Dovrai riavviare l'applicazione per riconnetterti |", "| Are you sure you want disconnect from the server? You'll have to restart the application to reconnect |");
    public final static ItaEngString DISCONNECT_r_02 = new ItaEngString("|                                   Premi sì per confermare                                             |", "|                                     Type YES to confirm                                               |");
    public final static ItaEngString DISCONNECT_p_01 = new ItaEngString("si", "yes");
    public final static ItaEngString DISCONNECT_p_02 = new ItaEngString("sì", "yes");

    public final static ItaEngString CREATE = new ItaEngString("crea", "create");
    public final static ItaEngString CREATE_et = new ItaEngString("EccezioneFormatoNumero", "NumberFormatException");
    public final static ItaEngString CREATE_em = new ItaEngString("si prega di specificare correttamente il numero di giocatori!", "please, specify correctly the number of players!");

    public final static ItaEngString JOIN = new ItaEngString("entra", "join");

    public final static ItaEngString LEAVE = new ItaEngString("esci", "leave");

    public final static ItaEngString PLAYER = new ItaEngString("giocatore", "player");
    public final static ItaEngString PLAYER_et = CREATE_et;
    public final static ItaEngString PLAYER_em = new ItaEngString("si prega di inserire un numero valido!", "please, insert a valid number!");

    public final static ItaEngString DRAW = new ItaEngString("pesca", "draw");
    public final static ItaEngString DRAW_p_01 = new ItaEngString("i", "l");
    public final static ItaEngString DRAW_p_02 = new ItaEngString("s", "u");
    public final static ItaEngString DRAW_p_03 = new ItaEngString("p", "c");
    public final static ItaEngString DRAW_p_04 = new ItaEngString("e", "b");
    public final static ItaEngString DRAW_et_01 = new ItaEngString("Errore ortografico", "Line misspelling");
    public final static ItaEngString DRAW_em_01 = new ItaEngString("scelta della carta non valida!", "not a valid card choice!");
    public final static ItaEngString DRAW_et_02 = PLAYER_et;
    public final static ItaEngString DRAW_em_02 = PLAYER_em;
    public final static ItaEngString DRAW_et_03 = new ItaEngString("Parametri mancanti", "Missing parameters");
    public final static ItaEngString DRAW_em_03 = new ItaEngString("hai sorvolato dei parametri necessari col tuo comando", "missed a few necessary parameters in your command");

    public final static ItaEngString READ = new ItaEngString("leggi", "read");
    public final static ItaEngString READ_p_01 = DRAW_p_01;
    public final static ItaEngString READ_p_02 = DRAW_p_02;
    public final static ItaEngString READ_p_03 = DRAW_p_03;
    public final static ItaEngString READ_p_04 = DRAW_p_04;
    public final static ItaEngString READ_et_01 = new ItaEngString("EccezioneIndiceFuoriLimite", "IndexOutOfBoundsException");
    public final static ItaEngString READ_em_01 = new ItaEngString("l'indice è fuori dai limiti!", "index out of bounds!");

    public final static ItaEngString LEGEND = new ItaEngString("legenda", "legend");

    public final static ItaEngString OFFER = new ItaEngString("offerta", "offer");
    public final static ItaEngString OFFER_et_01 = PLAYER_et;
    public final static ItaEngString OFFER_em_01 = PLAYER_em;

    public final static ItaEngString TOTEM = new ItaEngString("totemo", "totem");

    public final static ItaEngString IDIOT = new ItaEngString("idiota", "idiot");
    public final static ItaEngString IDIOT_et_01 = new ItaEngString("ATTENZIONE: idiota", "ALERT: idiot");
    public final static ItaEngString IDIOT_em_01 = new ItaEngString("tu SEI un IDIOTA :) ", "you ARE an IDIOT :) ");
}
