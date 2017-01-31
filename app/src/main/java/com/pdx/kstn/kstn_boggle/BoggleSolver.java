package com.pdx.kstn.kstn_boggle;

import java.util.ArrayList;

/**
 * Created by thanhhoang on 1/30/17.
 */

public class BoggleSolver {
    private String board[][] = new String[4][4];
    static int[] row = {0, 0, 1, 1, 1, -1, -1, -1};
    static int[] col = {1, -1, 0, 1, -1, 0, 1, -1};
    static private boolean[][] visited = new boolean[4][4];

    BoggleSolver(String[][] board) {
        this.board = board;
        for (int i = 0; i < 4; i++) {
            for (int k = 0; k < 4; k++) {
                visited[i][k] = false;
            }
        }
    }

    public ArrayList<String> solve(Dictionary dictionary) {
        ArrayList<String> allValidWords = new ArrayList<String>();


        for (int i = 0; i < 4; i++) {
            for (int k = 0; i < 4; k++) {

            }
        }





        return allValidWords;
    }


    public static void search(String d[], int mat[][]) {
        boolean flag = false;



    }

}
