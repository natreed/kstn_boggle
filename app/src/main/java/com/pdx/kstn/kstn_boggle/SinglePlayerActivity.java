package com.pdx.kstn.kstn_boggle;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Sharmistha on 1/27/2017.
 */
public class SinglePlayerActivity extends MainActivity {
    char[][] board;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);





        final Button shake = (Button) findViewById(R.id.shake);
        shake.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // BoggleBoard();



//                board = new char[4][4];
//                String letterdist ="eeeeeeeeeeeeeeeeeeetttttttttttttaaaaaaaaaaaarrrrrrrrrrrriiiiiiiiiiinnnnnnnnnnnooooooooooosssssssssddddddccccchhhhhlllllffffmmmmppppuuuugggyyywwbjkvxzq";
//                for (int row = 0; row < board.length; row++) {
//                    for (int column = 0; column < board.length; column++) {
//
//                        //board[row][column] = (char)('A' + (int)(Math.random()*26));
//                        board[row][column] = letterdist.charAt((int)(Math.random()*letterdist.length()));
//                    }
//                }

                board = new char[][] {
                        {'w', 'a', 't', 'd'},
                        {'m', 'e', 'j', 't'},
                        {'h', 'n', 't', 'i'},
                        {'c', 'l', 'l', 'p'}
                };

                try {
                    Resources res = getResources();
                    InputStream ins = res.openRawResource(R.raw.dictionary);
                    Dictionary dic = new Dictionary();
                    dic.createDictionary(ins);

                    System.out.print("Come here!\n");

                    if (dic.isWord("student"))
                        System.out.printf("student is in dictionary\n");

                    if (dic.isWord("school"))
                        System.out.print("school is in dictionary\n");

                    String[] words = new String []{
                            "amen", "ament", "ate", "awe", "cheat", "chew",
                            "eat",  "eta", "heat", "hem", "hen", "hent",
                            "het", "hew", "ill", "item", "jam", "jaw",
                             "jet", "jew", "jill", "jilt", "lip", "lit",
                            "lite", "litten", "mae", "mat", "mate", "matt",
                            "maw", "meat", "men", "met", "meta", "mew",
                            "neat", "nema", "net", "nett", "new" , "pill",
                            "pit" ,   "tae" , "taj" , "tam", "tame" , "taw",
                            "tea" , "team", "teat" , "ten", "tench" , "tent",
                            "tet", "tew", "til" , "till", "tilt" , "tip",
                            "tit", "wae" , "wame" , "wat" , "wate" , "watt",
                             "wen" , "wench" , "went" , "wet"
                    };

//                    for (int i = 0; i < words.length; i++)
//                        dic.insert(words[i]);

//
//                    for (int i = 0; i < words.length; i++) {
//                        if (dic.isWord(words[i]) == false)
//                            System.out.println(words[i] + ": not found");
//
//                        //board[row][column] = (char)('A' + (int)(Math.random()*26));
//                        board[row][column] = letterdist.charAt((int)(Math.random()*(letterdist.length()-1)));
//
//                    }

                    long start = System.currentTimeMillis();
                    ArrayList<String> list = BoggleSolver.solver(board, dic);
                    long end = System.currentTimeMillis() - start;

                    System.out.println("Total words found:  " + list.size());
                    System.out.println("Total time: " + (int) end + " miliseconds");

                    for (String word: list)
                        System.out.println(word);

                    System.out.println("A B C D E F ");

                } catch (Exception e) {

                }


                // 1st row
                Button button0 = (Button) findViewById(R.id.button0);
                button0.setText(String.valueOf(board[0][0]));

                Button button1 = (Button) findViewById(R.id.button1);
                button1.setText(String.valueOf(board[0][1]));

                Button button2 = (Button) findViewById(R.id.button2);
                button2.setText(String.valueOf(board[0][2]));

                Button button3 = (Button) findViewById(R.id.button3);
                button3.setText(String.valueOf(board[0][3]));
                //2nd row
                Button button4 = (Button) findViewById(R.id.button4);
                button4.setText(String.valueOf(board[1][0]));

                Button button5 = (Button) findViewById(R.id.button5);
                button5.setText(String.valueOf(board[1][1]));

                Button button6 = (Button) findViewById(R.id.button6);
                button6.setText(String.valueOf(board[1][2]));

                Button button7 = (Button) findViewById(R.id.button7);
                button7.setText(String.valueOf(board[1][3]));

                //3rd row
                Button button8 = (Button) findViewById(R.id.button8);
                button8.setText(String.valueOf(board[2][0]));

                Button button9 = (Button) findViewById(R.id.button9);
                button9.setText(String.valueOf(board[2][1]));

                Button button10 = (Button) findViewById(R.id.button10);
                button10.setText(String.valueOf(board[2][2]));

                Button button11 = (Button) findViewById(R.id.button11);
                button11.setText(String.valueOf(board[2][3]));

                //4th row
                Button button12 = (Button) findViewById(R.id.button12);
                button12.setText(String.valueOf(board[3][0]));

                Button button13 = (Button) findViewById(R.id.button13);
                button13.setText(String.valueOf(board[3][1]));

                Button button14 = (Button) findViewById(R.id.button14);
                button14.setText(String.valueOf(board[3][2]));

                Button button15 = (Button) findViewById(R.id.button15);
                button15.setText(String.valueOf(board[3][3]));


            }
        });
    }
    private void BoggleBoard(){

    }
}
