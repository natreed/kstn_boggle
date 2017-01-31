package com.pdx.kstn.kstn_boggle;

import java.util.ArrayList;

/**
 * Created by thanhhoang on 1/30/17.
 */

public class BoggleSolver {
    //private String board[][] = new String[4][4];
    static int[] row = {0, 0, 1, 1, 1, -1, -1, -1};
    static int[] col = {1, -1, 0, 1, -1, 0, 1, -1};
    static private boolean[][] visited_ = new boolean[][] {
            { false, false, false, false},
            { false, false, false, false},
            { false, false, false, false},
            { false, false, false, false}
    };


    static public void findWords(char[][] board, boolean[][] visited, int i, int j,
                                 Node current, String word, ArrayList<String> list) {
        if (visited[i][j] == true)      // stop checking if this block is visited
            return;

        visited[i][j] = true;

        String str = Character.toString(board[i][j]);
        if (board[i][j] == 'q')
            str = "qu";             // special case in dictionary

        current = Dictionary.nextNode(str, current);
        word += str;

        if (current != null) {
            if (current.isWord == true)
                list.add(word);

            // check adjacent bock
            for (int w = 0; w < 8; w++) {
                int k = i + row[w];
                int l = j + col[w];

                if (k < 0 || l < 0 || k > 3 || l > 3)
                    break;

                findWords(board, visited, k, l, current, word, list);
            }
        }

        return;
    }

    static public ArrayList<String> solver(char[][] board, Dictionary dictionary) {
        ArrayList<String> listWords = new ArrayList<String>();
        boolean flag = false;
        boolean[][] visited = visited_;
        Node current = dictionary.getRootDictionary();

        String str = "";

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 4; j++)
                findWords(board, visited, i, j, current, str, listWords);

        return listWords;
    }

}
