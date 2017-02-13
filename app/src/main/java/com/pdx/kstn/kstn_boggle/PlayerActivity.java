package com.pdx.kstn.kstn_boggle;

import android.content.DialogInterface;
import android.content.Intent;
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.pdx.kstn.kstn_boggle.ChooseModeMainActivity.selectedDifficultyLevel;

/**
 * Created by Sharmistha on 1/27/2017.
 */
public class PlayerActivity extends MainActivity {
    char[][] board;
    boolean isCounterRunning = false;
    public CountDownTimer timer = null;

    public Player player = new Player();
    public Dictionary dictionary = new Dictionary();
    public ArrayList<String> allValidWords = new ArrayList<String>();

    public int pressCount = 0;
    public int lastRow = 0;
    public int lastCol = 0;
    boolean[][] buttonStatus = new boolean[4][4];
    String inputWord = "";
    String[] foundWords = {};
    long minute = 0;
    long second = 0;
    long totalTime = 180000;

    // get difficulty level



    public void setDifficulty(int getDifficulty) {

        switch(getDifficulty) {
            case 0:
                passingwords = 2;
                break;
            case 1:
                passingwords = 5;
                break;
            case 2:
                passingwords =7;
                break;
        }
    }









    int passingwords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);
        System.out.println(selectedDifficultyLevel);

        setDifficulty(selectedDifficultyLevel);



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

        final Button button_next_round = (Button) findViewById(R.id.button_nextRound);
        final Button button_submit_word = (Button) findViewById(R.id.button_submitWord);
        final TextView p1_score = (TextView) findViewById(R.id.text_player1_score);
        final TextView text_round_count = (TextView) findViewById(R.id.roundCount);
        final TextView timer_text = (TextView) findViewById(R.id.time_remaining);

        ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(PlayerActivity.this, android.R.layout.simple_list_item_1, foundWords);
        ListView wordList = (ListView) findViewById(R.id.list_foundWords);
        wordList.setAdapter(wordAdapter);

        // load dictionary file
        try {
            InputStream in = getResources().openRawResource(R.raw.dictionary);
            dictionary.createDictionary(in);
        } catch (Exception e) { }

        // generate and solve board
        board = BoardGenerate.createNewBoard();
        allValidWords = BoggleSolver.solver(board, dictionary);

        resetButtonStatus();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                final int ButtonNum = i*4 + j;
                final int row = i;
                final int col = j;
                BoardButton[ButtonNum].setTextColor(Color.WHITE);
                BoardButton[ButtonNum].setText(String.valueOf(board[i][j]));
                BoardButton[ButtonNum].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean validClick = checkOnClick(row, col);

                        if (validClick == false) {
                            resetBoardButtons(BoardButton);
                        }
                        else {
                            BoardButton[ButtonNum].setBackgroundColor(Color.RED);
                            //PlayerActivity.this.inputWord += Log.v("EditText", BoardButton[ButtonNum].getText().toString());

                        }
                    }

                });
            }
        }


        for (String word: allValidWords)
            System.out.println(word);


        button_submit_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Check submit button");
                System.out.println(inputWord);
                // check inputWord is in list
                if (allValidWords.contains(inputWord)) {
                    System.out.println("Found word: " + inputWord);

                    player.updateInfor(inputWord);
                    p1_score.setText(Integer.toString(player.getScore()));

                    foundWords = player.getFoundWordsCurrentRound().toArray(new String[0]);

                    ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(PlayerActivity.this, android.R.layout.simple_list_item_1, foundWords);
                    ListView wordList = (ListView) findViewById(R.id.list_foundWords);
                    wordList.setAdapter(wordAdapter);

                    resetBoardButtons(BoardButton);
                    resetButtonStatus();

                }
            }
        });

        // timer
        timer = new CountDownTimer(totalTime, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                minute = millisUntilFinished/60000;
                second = millisUntilFinished/1000 % (millisUntilFinished/60000*60);
                totalTime = minute*60000 + second*1000;
                timer_text.setText(minute + ":" + second);
            }

            @Override
            public void onFinish() {
                isCounterRunning = false;
            }
        };

        if (!isCounterRunning) {
            isCounterRunning = true;
            timer.start();
        }
        else {
            //timer.cancel();
            //totalTime = minute*60000 + second*1000 + player.getScoreForCurrentRound()*1000;
            totalTime += player.getScoreForCurrentRound()*1000;
            timer.start();
        }


        button_next_round.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // BoggleBoard();
                if (player.getNumWordLastRound() < passingwords)
                    return;

                newRound(BoardButton, dictionary);
                player.moveToNextRound();
                text_round_count.setText(String.valueOf(player.getRound()));

                foundWords = new String[0];

                ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(PlayerActivity.this, android.R.layout.simple_list_item_1, foundWords);
                ListView wordList = (ListView) findViewById(R.id.list_foundWords);
                wordList.setAdapter(wordAdapter);

                // reset timer

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
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(getBaseContext(), ChooseModeMainActivity.class));
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();

                return true;

            case R.id.item_high_score:
                new AlertDialog.Builder(this)
                        .setTitle("")
                        .setMessage("Do you want to quit current game?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(getBaseContext(), ScoresActivity.class));
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
                return true;

            case R.id.item_instruction:
                new AlertDialog.Builder(this)
                        .setTitle("")
                        .setMessage("Do you want to quit current game?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                startActivity(new Intent(getBaseContext(), RulesActivity.class));
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
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

    public void resetButtonStatus() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                buttonStatus[i][j] = false;
                inputWord = "";
                lastCol = 0;
                lastRow = 0;
                pressCount = 0;
            }
        }
    }

    public void resetBoardButtons(Button[] buttons) {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setTextColor(Color.WHITE);
            //buttons[i].setBackgroundColor(Color.BLUE);
            buttons[i].setBackgroundResource(R.drawable.background_neural);
        }
    }

    public void newRound(Button[] boardButton, Dictionary dictionary) {
        board = BoardGenerate.createNewBoard();
        allValidWords = BoggleSolver.solver(board, dictionary);

        resetButtonStatus();
        resetBoardButtons(boardButton);
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++)
                boardButton[4*i+j].setText(String.valueOf(board[i][j]));
        }
    }


    public boolean checkOnClick(int row, int col) {
        if (buttonStatus[row][col] == true) {
            resetButtonStatus();
            return false;
        }

        if (pressCount == 0) {
            pressCount += 1;
            lastRow = row;
            lastCol = col;

            buttonStatus[row][col] = true;

            String str = Character.toString(board[row][col]);
            if (str == "q")
                str = "qu";

            inputWord = inputWord + str;

            return true;
        }

        boolean isNextToLastButton = false;
        int[] rowIdx = {0, 0, 1, 1, 1, -1, -1, -1};
        int[] colIdx = {1, -1, 0, 1, -1, 0, 1, -1};
        int tempRow, tempCol;

        for (int i = 0; i < 8; i++) {
            tempRow = lastRow + rowIdx[i];
            tempCol = lastCol + colIdx[i];
            if (tempRow < 0 || tempCol < 0 || tempRow > 3 || tempCol > 3)
                continue;
            if ((tempRow == row) && (tempCol == col)) {
                isNextToLastButton = true;
                break;
            }
        }

        if (isNextToLastButton == true) {
            pressCount += 1;
            lastRow = row;
            lastCol = col;
            buttonStatus[row][col] = true;

            String str = Character.toString(board[row][col]);
            if (str == "q")
                str = "qu";

            inputWord = inputWord + str;

            return true;
        } else {
            resetButtonStatus();
            return false;
        }

    }
}
