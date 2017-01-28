package com.pdx.kstn.kstn_boggle;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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
                board = new char[4][4];
                String letterdist ="eeeeeeeeeeeeeeeeeeetttttttttttttaaaaaaaaaaaarrrrrrrrrrrriiiiiiiiiiinnnnnnnnnnnooooooooooosssssssssddddddccccchhhhhlllllffffmmmmppppuuuugggyyywwbjkvxzq";
                for (int row = 0; row < board.length; row++) {
                    for (int column = 0; column < board.length; column++) {

                        //board[row][column] = (char)('A' + (int)(Math.random()*26));
                        board[row][column] = letterdist.charAt((int)(Math.random()*letterdist.length()));
                    }
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
