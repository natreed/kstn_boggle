package com.pdx.kstn.kstn_boggle;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AlertDialogLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.graphics.Color;
import android.util.Log;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Sharmistha on 1/27/2017.
 */
public class PlayerActivity extends MainActivity {
    char[][] board;
    boolean isCounterRunning = false;
    public CountDownTimer timer = null;
    String foundWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);
        final String foundWord = "";
        String [] solvedWordlist = null;

        // menu toolbar

//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        //string to test listview
        String[] cars = {"dodge", "chevy", "toyota", "subaru", "hyundai", "nissan"};
        ArrayAdapter<String> carsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cars);
        ListView carsList = (ListView) findViewById(R.id.list_foundWords);
        carsList.setAdapter(carsAdapter);

        final Button button_shake = (Button) findViewById(R.id.shake);
        final Button button_new_game = (Button) findViewById(R.id.button_new_game);
        final Button button_submit_word = (Button) findViewById(R.id.button_submitWord);
        final TextView timer_text = (TextView) findViewById(R.id.time_remaining);

        button_new_game.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                finish();
            }
        });

        button_shake.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // BoggleBoard();

                timer = new CountDownTimer(180000, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {
                        timer_text.setText( millisUntilFinished/60000 + ":" + millisUntilFinished/1000 % (millisUntilFinished/60000*60));
                    }

                    @Override
                    public void onFinish() {
                        isCounterRunning = false;
                    }
                };


                board = new char[4][4];
                String letterdist ="eeeeeeeeeeeeeeeeeeetttttttttttttaaaaaaaaaaaarrrrrrrrrrrriiiiiiiiiiinnnnnnnnnnnooooooooooosssssssssddddddccccchhhhhlllllffffmmmmppppuuuugggyyywwbjkvxzq";
                for (int row = 0; row < board.length; row++) {
                    for (int column = 0; column < board.length; column++) {

                        //board[row][column] = (char)('A' + (int)(Math.random()*26));
                        board[row][column] = letterdist.charAt((int)(Math.random()*letterdist.length()));
                    }
                }

                final Button BoardButton[] = new Button[16];
                BoardButton[0] = (Button) findViewById(R.id.button0);
                BoardButton[1] = (Button) findViewById(R.id.button1);
                BoardButton[2] = (Button) findViewById(R.id.button2);
                BoardButton[3] = (Button) findViewById(R.id.button3);
                BoardButton[4] = (Button) findViewById(R.id.button4);
                BoardButton[5] = (Button) findViewById(R.id.button5);
                BoardButton[6] = (Button) findViewById(R.id.button6);
                BoardButton[7] = (Button) findViewById(R.id.button7);
                BoardButton[8] = (Button) findViewById(R.id.button8);
                BoardButton[9] = (Button) findViewById(R.id.button9);
                BoardButton[10] = (Button) findViewById(R.id.button10);
                BoardButton[11] = (Button) findViewById(R.id.button11);
                BoardButton[12] = (Button) findViewById(R.id.button12);
                BoardButton[13] = (Button) findViewById(R.id.button13);
                BoardButton[14] = (Button) findViewById(R.id.button14);
                BoardButton[15] = (Button) findViewById(R.id.button15);


                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        final int ButtonNum = i*4 + j;
                        BoardButton[ButtonNum].setTextColor(Color.WHITE);
                        BoardButton[ButtonNum].setText(String.valueOf(board[i][j]));
                        BoardButton[ButtonNum].setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                BoardButton[ButtonNum].setBackgroundColor(Color.RED);
                                PlayerActivity.this.foundWord += Log.v("EditText", BoardButton[ButtonNum].getText().toString());
                            }

                        });
                    }
                }

                button_submit_word.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View arg0) {
                        if (isWord(foundWord) == 1) {

                        }
                        else {
                            //print a message
                        }
                    }
                });


               if (!isCounterRunning) {
                   isCounterRunning = true;
                   timer.start();
               }
               else {
                   timer.cancel();
                   timer.start();
               }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.item_choose_game:
                new AlertDialog.Builder(this)
                        .setTitle("")
                        .setMessage("Do you want to quit current game?")
                        .setPositiveButton(android.R.string.yes, null)
                        .setNegativeButton(android.R.string.no, null).show();

                return true;

            case R.id.item_high_score:
                return true;

            case R.id.item_instruction:
                return true;

            case R.id.item_about_us:

                return true;

        }
        return true;
    }

    //just a prototype
    private int isWord(String word) {
        return 2;
    }


    private void BoggleBoard(){

    }
}
