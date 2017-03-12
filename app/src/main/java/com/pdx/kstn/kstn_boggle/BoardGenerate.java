package com.pdx.kstn.kstn_boggle;

/**
 * Created by thanhhoang on 1/28/17.
 */

public class BoardGenerate {

    static public final String letterdist = "eeeeeeeeeeeeeeeeeeetttttttttttttaaaaaaaaaaaarrrrrrrrrrrriiiiiiiiiiinnnnnnnnnnnooooooooooosssssssssddddddccccchhhhhlllllffffmmmmppppuuuugggyyywwbjkvxzq";

    // generate a 4x4 grid using random picking letter from letter distribution
    static public String[][] createNewBoard() {
        String[][] board = new String[4][4];

        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board.length; column++) {
                char  letter = letterdist.charAt((int)(Math.random()*letterdist.length()));
                if (letter == 'q')
                    board[row][column] = "qu";
                else
                    board[row][column] = Character.toString(letter);
            }
        }

        return board;
    }

}
