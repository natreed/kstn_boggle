package com.pdx.kstn.kstn_boggle;

/**
 * Created by thanhhoang on 1/28/17.
 */

public class BoardGenerate {
    String letterdist = "eeeeeeeeeeeeeeeeeeetttttttttttttaaaaaaaaaaaarrrrrrrrrrrriiiiiiiiiiinnnnnnnnnnnooooooooooosssssssssddddddccccchhhhhlllllffffmmmmppppuuuugggyyywwbjkvxzq";
    public char[][] board = new char[4][4];

    public char[][] createNewBoard() {
        for (int row = 0; row < board.length; row++) {
            for (int column = 0; column < board.length; column++) {

                //board[row][column] = (char)('A' + (int)(Math.random()*26));
                board[row][column] = letterdist.charAt((int)(Math.random()*letterdist.length()));
            }
        }

        return board;
    }

    public String[] validWords() {
        return null;
    }


}
