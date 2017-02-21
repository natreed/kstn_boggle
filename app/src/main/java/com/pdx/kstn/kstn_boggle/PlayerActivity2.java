package com.pdx.kstn.kstn_boggle;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.graphics.Color;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import static android.R.attr.fingerprintAuthDrawable;
import static android.R.attr.startX;

/**
 * Created by Sharmistha on 1/27/2017.
 */
public class PlayerActivity2 extends MainActivity {
    String[][] board;
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
    public long totalTime = 180000;

    public TextView text_timer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);

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

        final Button btt_cancel = (Button) findViewById(R.id.button_cancel);
        final Button button_submit_word = (Button) findViewById(R.id.button_submitWord);
        final TextView p1_score = (TextView) findViewById(R.id.text_player_score);
        final TextView text_display = (TextView) findViewById(R.id.text_display_screen);
        //final TableLayout boardLayout = (TableLayout) findViewById(R.id.buttonBoard);
        final LinearLayout boardLayout = (LinearLayout) findViewById(R.id.mainLayout);

        ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(PlayerActivity2.this, android.R.layout.simple_list_item_1, foundWords);
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

        // board init and handler
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                final int ButtonNum = i*4 + j;
                final int row = i;
                final int col = j;
                String str = String.valueOf(board[i][j]);
//                if (str.equals("q")) str = "qu";

                BoardButton[ButtonNum].setTextColor(Color.WHITE);
                BoardButton[ButtonNum].setText(str);

                BoardButton[ButtonNum].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {}
                });


            }
        }
        Log.d("BUTTONFINDERTEST", "RUNNING buttonFinder");
        boardLayout.setOnTouchListener(new View.OnTouchListener() {
            private float startX;
            private float startY;
            SlideSelect slideSelect = new SlideSelect(boardLayout, BoardButton);

            @Override
            public boolean onTouch(View v, MotionEvent event) {
//                try {
//                    Thread.sleep(50);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        startX = event.getRawX();
                        startY = event.getRawY();

                        Log.d("BUTTONFINDERTEST", "RUNNING buttonFinder");
                        int isButton = slideSelect.buttonFinder(BoardButton, boardLayout, (int)startX, (int)startY);

                        if (isButton == -1) {
                            Log.d("NOT BUTTON", "NOT A BUTTON");
                        }
                        else {
                            int row = isButton/4;
                            int col = isButton%4;
                            boolean vallidClick = checkOnClick(row, col);
                            if (vallidClick == false) {
                                resetBoardButtons(BoardButton);
                            }
                            else {
                                BoardButton[isButton].setBackgroundColor(Color.RED);
                                text_display.setText(inputWord);
                            }
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        startX = event.getX();
                        startY = event.getY();

                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        startX = event.getX();
                        startY = event.getY();

                        break;
                    }

                }
                return true;
            }
        });



        for (String word: allValidWords)
            System.out.println(word);


        button_submit_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Check submit button");
                System.out.println(inputWord);
                // check inputWord is in list
                int ret = player.updateInfor(inputWord, allValidWords);

                if (ret == -1)
                    text_display.setText("Invalid word!");
                else if (ret == 0)
                    text_display.setText("Invalid, \"" +inputWord + "\" found!");
                else if (ret == 1) {
                    text_display.setText("Valid Word!");
                    p1_score.setText("Score: " + Integer.toString(player.getScore()));

                    foundWords = player.getFoundWords().toArray(new String[0]);

                    ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(PlayerActivity2.this, android.R.layout.simple_list_item_1, foundWords);
                    ListView wordList = (ListView) findViewById(R.id.list_foundWords);
                    wordList.setAdapter(wordAdapter);
                }

                resetBoardButtons(BoardButton);
                resetButtonStatus();

            }
        });


        // set up timer
        text_timer =  (TextView) findViewById(R.id.time_remaining);
        timer = new Timer(totalTime, 1000);
        timer.start();

        btt_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetBoardButtons(BoardButton);
                resetButtonStatus();
                text_display.setText(inputWord);
            }
        });

    }



    public class Timer extends CountDownTimer {
        public Timer(long startTime, long interval) {
            super(startTime, interval);
        }
        @Override
        public void onFinish() {
            text_timer.setText("TIME'S UP!");
        }
        @Override
        public void onTick(long millisUntilFinished) {
            text_timer.setText("Timer: " + millisUntilFinished / 1000);
            //text_timer.setText(millisUntilFinished/60000 + ":" + millisUntilFinished/1000 % (millisUntilFinished/60000*60));

        }
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

//            String str = Character.toString(board[row][col]);
//            if (str == "q")
//                str = "qu";

            inputWord = inputWord + board[row][col];

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

//            String str = Character.toString(board[row][col]);
//            if (str == "q")
//                str = "qu";

            inputWord = inputWord + board[row][col];

            return true;
        } else {
            resetButtonStatus();
            return false;
        }

    }
}