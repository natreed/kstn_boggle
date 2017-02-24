package com.pdx.kstn.kstn_boggle;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;

import java.io.InputStream;
import java.util.ArrayList;


/**
 * Created by Sharmistha on 1/27/2017.
 */
public class PlayerActivity extends AppCompatActivity implements SensorEventListener {
    String[][] board;
    boolean isCounterRunning = false;
    public CountDownTimer timer = null;
    public Dictionary dictionary = null;
    public ArrayList<String> allValidWords = new ArrayList<String>();


    public int pressCount = 0;
    public int lastRow = 0;
    public int lastCol = 0;
    boolean[][] buttonStatus = new boolean[4][4];
    String inputWord = "";
    String[] foundWords = {};
    public long totalTime = 30000;

    public TextView text_timer;

    private boolean init;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;
    private float x1, x2, x3;
    private static final float ERROR = (float) 7.0;
    private boolean isGameOn;
    final  Button BoardButton[] = new Button[16];
    TextView text_display;

    public Player player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.player_activity);

        // set up timer
        text_timer =  (TextView) findViewById(R.id.time_remaining);
        //timer = new Timer(totalTime, 1000);
        player = new Player(text_timer, getApplicationContext());

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
         text_display = (TextView) findViewById(R.id.text_display_screen);

        ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(PlayerActivity.this, android.R.layout.simple_list_item_1, foundWords);
        ListView wordList = (ListView) findViewById(R.id.list_foundWords);
        wordList.setAdapter(wordAdapter);


            System.out.println("DICTIONARY LOADED");
            dictionary = null;
            try {
                InputStream in = getResources().openRawResource(R.raw.dictionary);
                dictionary = new Dictionary();
                dictionary.createDictionary(in);
            } catch (Exception e) { }

        // generate and solve board
        board = BoardGenerate.createNewBoard();
        //allValidWords = BoggleSolver.solver(board, dictionary);

        player.setAllVallidWords(BoggleSolver.solver(board, dictionary));

        resetButtonStatus();
        initBoard();

        for (String word: player.allValidWords)
            System.out.println(word);

        button_submit_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Check submit button");
                System.out.println(inputWord);
                // check inputWord is in list
                int ret = player.updateInfor(inputWord, player.allValidWords);

                if (ret == -1)
                    text_display.setText("Invalid word!");
                else if (ret == 0)
                    text_display.setText("Invalid, \"" +inputWord + "\" found!");
                else if (ret == 1) {
                    text_display.setText("Valid Word!");
                    p1_score.setText("Score: " + Integer.toString(player.getScore()));

                    foundWords = player.getFoundWords().toArray(new String[0]);

                    ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(PlayerActivity.this, android.R.layout.simple_list_item_1, foundWords);
                    ListView wordList = (ListView) findViewById(R.id.list_foundWords);
                    wordList.setAdapter(wordAdapter);
                }

                resetBoardButtons(BoardButton);
                resetButtonStatus();

            }
        });

        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        isGameOn =false;
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

            //Toast.makeText(this, "ACCELEROMETER sensor is available on device", Toast.LENGTH_SHORT).show();
            init = false;

            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        } else {

            Toast.makeText(this, "ACCELEROMETER sensor is NOT available on device", Toast.LENGTH_SHORT).show();
        }

        System.out.println("TIMER STARTS HERE");
        player.initiateTimer();

        btt_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetBoardButtons(BoardButton);
                resetButtonStatus();
                text_display.setText(inputWord);
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent e) {
        //Get x,y and z values
        float x,y,z;
        x = e.values[0];
        y = e.values[1];
        z = e.values[2];

        if (!init) {
            x1 = x;
            x2 = y;
            x3 = z;
            init = true;
        } else {

            float diffX = Math.abs(x1 - x);
            float diffY = Math.abs(x2 - y);
            float diffZ = Math.abs(x3 - z);

            //Handling ACCELEROMETER Noise
            if (diffX < ERROR) {
                diffX = (float) 0.0;
            }
            if (diffY < ERROR) {
                diffY = (float) 0.0;
            }
            if (diffZ < ERROR) {
                diffZ = (float) 0.0;
            }

            x1 = x;
            x2 = y;
            x3 = z;

            if (diffX > diffY) {
                Toast.makeText(PlayerActivity.this, "Shake Detected!", Toast.LENGTH_SHORT).show();
                board = BoardGenerate.createNewBoard();
                resetButtonStatus();
                resetBoardButtons(BoardButton);
                for (int i = 0; i < 4; i++) {
                    for (int j = 0; j < 4; j++) {
                        final int ButtonNum = i * 4 + j;
                        final int row = i;
                        final int col = j;
                        String str = String.valueOf(board[i][j]);

                        BoardButton[ButtonNum].setTextColor(Color.WHITE);
                        BoardButton[ButtonNum].setText(str);
                    }
                }
//                timer.cancel();
//                timer.start();
                player.resetTimer();
            }
        }
    }



    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Noting to do!!
    }

    public void gameOver() {
        Intent intend = new Intent(getApplicationContext(), GameOver.class);
        intend.putExtra("PLAYER_SCORE", Integer.toString(player.getScore()));
        intend.putExtra("FOUND_WORDS", player.getFoundWords());
        intend.putExtra("POSSIBLE_WORDS", player.allValidWords);

        System.out.println("reach 1");
        startActivity(intend);
        System.out.println("reach 2");
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

    public void initBoard(){
        // board init and handler
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                final int ButtonNum = i * 4 + j;
                final int row = i;
                final int col = j;
                String str = String.valueOf(board[i][j]);
//                if (str.equals("q")) str = "qu";

                BoardButton[ButtonNum].setTextColor(Color.WHITE);
                BoardButton[ButtonNum].setText(str);
                //BoardButton[ButtonNum].setText("");
                BoardButton[ButtonNum].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        boolean validClick = checkOnClick(row, col);

                        if (validClick == false) {
                            resetBoardButtons(BoardButton);
                        } else {
                            BoardButton[ButtonNum].setBackgroundColor(Color.RED);
                            text_display.setText(inputWord);
                            //PlayerActivity.this.inputWord += Log.v("EditText", BoardButton[ButtonNum].getText().toString());
                        }
                    }

                });
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

//    public void newRound(Button[] boardButton, Dictionary dictionary) {
//        board = BoardGenerate.createNewBoard();
//        allValidWords = BoggleSolver.solver(board, dictionary);
//
//        resetButtonStatus();
//        resetBoardButtons(boardButton);
//        for (int i = 0; i < 4; i++) {
//            for (int j = 0; j < 4; j++)
//                boardButton[4*i+j].setText(String.valueOf(board[i][j]));
//        }
//    }

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

            inputWord = inputWord + board[row][col];

            return true;
        } else {
            resetButtonStatus();
            return false;
        }

    }

}
