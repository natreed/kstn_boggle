package com.pdx.kstn.kstn_boggle;

/**
 * Created by thanhhoang on 1/28/17.
 */

public class BoardGenerate {
    static public String letterdist = "eeeeeeeeeeeeeeeeeeetttttttttttttaaaaaaaaaaaarrrrrrrrrrrriiiiiiiiiiinnnnnnnnnnnooooooooooosssssssssddddddccccchhhhhlllllffffmmmmppppuuuugggyyywwbjkvxzq";
    public char[][] board = new char[4][4];

    static public char[][] createNewBoard() {
        char[][] board = new char[4][4];
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board.length; column++) {

                board[row][column] = letterdist.charAt((int)(Math.random()*letterdist.length()));
            }
        }

        return board;
    }

}
