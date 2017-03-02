package com.pdx.kstn.kstn_boggle;

import java.util.ArrayList;

/**
 * Created by thanhhoang on 2/27/17.
 */

public class MessageConverter {
    // this class turn objects (board, list of valid words to string (message)
    // or extract board, list of valid words from message

    public static String boardToMessage (String[][] board) {
        String message = "";

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (board[i][j] == "qu")
                    message += "q";
                else
                    message += board[i][j];
            }
        }

        return message;
    }

    public static String[][] messageToBoard(String message) {
        String[][] board = new String[4][4];
        char c;

        if (message.length() >= 16) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    if ((c=message.charAt(i*4+j)) == 'q')
                        board[i][j] = "qu";
                    else
                        board[i][j] = Character.toString(c);
                }
            }
        }

        System.out.println("Extract board: " + message);

        return board;
    }

    public static String listToMessage(ArrayList<String> list) {
        String message = "";

        for (String item: list) {
            message = item + " ";
        }

        return message;
    }

    public static ArrayList<String> messageToList(String message) {
        ArrayList<String> list = new ArrayList<String>();
        String[] temp = message.split(" ");

        for (String word: temp) {
            list.add(word);
        }

        return list;
    }
}
