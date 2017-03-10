package com.pdx.kstn.kstn_boggle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;

import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.graphics.Color;
import android.widget.Toast;
import android.graphics.Point;



import java.lang.Runnable;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Timer;


/**
 * Created by Sharmistha on 1/27/2017.
 */
public class PlayerActivity extends Activity implements View.OnTouchListener, SensorEventListener {

    // variables for innit game
    public String[][] board;
    boolean isCounterRunning = false;
    public CountDownTimer timer = null;
    public Player player;
    public Dictionary dictionary = new Dictionary();
    public ArrayList<String> allValidWords = new ArrayList<String>();
    // load dictionary file


    // variables for on-going game
//    public int pressCount = 0;
//    public int lastRow = 0;
//    public int lastCol = 0;
//    boolean[][] buttonStatus = new boolean[4][4];
//    String inputWord = "";

    String[] foundWords = {};
    public long totalTime = 180000;

    // variables for sensor
    private boolean init;
    private Sensor mAccelerometer;
    private SensorManager mSensorManager;
    private float x1, x2, x3;
    private static final float ERROR = (float) 7.0;
    private boolean isGameOn;

    // variables for handling sliding + locations
    Point[][] locationMatrix = new Point[4][4];  //new Coordinate[4][4];
    int bttHeight, bttWidth, offset;         // offset should be 1/6 of width or height
    public boolean[][] touchVisited = new boolean[4][4];
    public int tlRow = 0, tlCol = 0, tPressCount = 0;
    public String tInputWord = "";


    // layout variables
    public Button BoardButton[] = new Button[16];
    public Button btt_cancel;
    public Button button_submit_word;

    public TextView text_timer;
    public TextView text_display;       // display system messages, word
    public TextView p1_score;

    public ListView wordList;
    public RelativeLayout mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.double_player_activity_layout);
        Intent intent = getIntent();
		int level = Integer.parseInt(intent.getStringExtra("LEVEL"));
        String level_selected;
        if(level ==0){
            level_selected ="Easy";
        }
        else if (level==1)
        {
            level_slected ="Meidum";
        }
        else
            level_slected ="Hard";

        // load dictionary file
        try {
            InputStream in = getResources().openRawResource(R.raw.dictionary);
            dictionary.createDictionary(in);
        } catch (Exception e) {
        }

        // call init layout

        initLayoutVariables();
        setupNewGame();

        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        mainLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // Layout has happened here.
                        setLayoutLocation();
                        // Don't forget to remove your listener when you are done with it.
                        if (Build.VERSION.SDK_INT < 16) {
                            removeLayoutListenerPre16(mainLayout.getViewTreeObserver(), this);
                        } else {
                            removeLayoutListenerPost16(mainLayout.getViewTreeObserver(), this);
                        }
                    }
                });

        btt_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetBoardButtons();
                resetPressedStatus();
                text_display.setText(tInputWord);
            }
        });


        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        isGameOn = false;
        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {

            Toast.makeText(this, "ACCELEROMETER sensor is available on device", Toast.LENGTH_SHORT).show();


            init = false;

            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        } else {

            Toast.makeText(this, "ACCELEROMETER sensor is NOT available on device", Toast.LENGTH_SHORT).show();
        }


    }

    @SuppressWarnings("deprecation")
    private void removeLayoutListenerPre16(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        observer.removeGlobalOnLayoutListener(listener);
    }

    @TargetApi(16)
    private void removeLayoutListenerPost16(ViewTreeObserver observer, ViewTreeObserver.OnGlobalLayoutListener listener) {
        observer.removeOnGlobalLayoutListener(listener);

    }


    // get location of layout, init variables for handling
    // sliding/grad to select,
    // this functions has to be called on create activity
    private void setLayoutLocation() {
        // sliding
        int[] location = new int[2];

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        bttHeight = BoardButton[0].getHeight();
        bttWidth = BoardButton[0].getWidth();

        System.out.println("height " + bttHeight + ", width = " + bttWidth);

        offset = bttWidth / 6;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                BoardButton[4 * i + j].getLocationInWindow(location);
                locationMatrix[i][j] = new Point(location[0], location[1]);
                System.out.println("(" + i + ", " + j + ")  =  " + "(" + locationMatrix[i][j].x + ", " + locationMatrix[i][j].y + ")");
            }
        }

    }

    private void initLayoutVariables() {

        // init board buttons
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

        for (int i = 0; i < 16; i++)
            BoardButton[i].setOnTouchListener(this);


        btt_cancel = (Button) findViewById(R.id.button_cancel);
        button_submit_word = (Button) findViewById(R.id.button_submitWord);
        p1_score = (TextView) findViewById(R.id.text_player_score);

        btt_cancel = (Button) findViewById(R.id.button_cancel);
        button_submit_word = (Button) findViewById(R.id.button_submitWord);
        p1_score = (TextView) findViewById(R.id.text_player_score);
        text_display = (TextView) findViewById(R.id.text_display_screen);

        ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(PlayerActivity.this, R.layout.mywhite_listview, foundWords);
        wordList = (ListView) findViewById(R.id.list_foundWords);
        wordList.setAdapter(wordAdapter);

        text_timer = (TextView) findViewById(R.id.time_remaining);

    }


    private void setupNewGame() {

//        System.out.println("DICTIONARY LOADED");
//        dictionary = null;
//        try {
//            InputStream in = getResources().openRawResource(R.raw.dictionary);
//            dictionary = new dictionary();
//            dictionary.createDictionary(in);
//        } catch (Exception e) { }

        // generate and solve board

        board = BoardGenerate.createNewBoard();
        player = new Player(text_timer, getApplicationContext());
        allValidWords = BoggleSolver.solver(board, dictionary);
        player.setAllVallidWords(allValidWords);

        resetPressedStatus();
        initBoard();
        p1_score.setText("Score: 0");

        player.initiateTimer();
    }

    private void setupTimer() {
        // set up timer
        timer = new Timer(totalTime, 1000);
        timer.start();

    }

    private void submitAction(final Activity activity) {
        button_submit_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check inputWord is in list
                int ret = player.updateInfor(tInputWord, player.allValidWords);

                if (ret == -1) {
                    text_display.setText("Invalid word!");
                    MediaPlayer mperr = MediaPlayer.create(getApplicationContext(), R.raw.error);
                    mperr.start();
                }
                else if (ret == 0) {
                    text_display.setText("Invalid, \"" + tInputWord + "\" found!");
                    MediaPlayer mpallreadyfound = MediaPlayer.create(getApplicationContext(), R.raw.bip);
                    mpallreadyfound.start();

                }
                else if (ret == 1) {
                    text_display.setText("Valid Word!");
                    MediaPlayer mp = MediaPlayer.create(getApplicationContext(), R.raw.game_sound_correct);
                    mp.start();

                    p1_score.setText("Score: " + Integer.toString(player.getScore()));

                    foundWords = player.getFoundWords().toArray(new String[0]);
                    ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(PlayerActivity.this, R.layout.mywhite_listview, foundWords);
                    wordList.setAdapter(wordAdapter);
                }

                resetBoardButtons();
                resetPressedStatus();

            }
        });
    }


    @Override
    public void onSensorChanged(SensorEvent e) {
        //Get x,y and z values
        float x, y, z;
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
                player.resetPlayer();
                Thread thread_boardSolver = new Thread(new Runnable() {
                    public void run()
                    {
                        allValidWords = BoggleSolver.solver(board, dictionary);
                        player.setAllVallidWords(allValidWords);
                    }
                });

                thread_boardSolver.start();

                ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(PlayerActivity.this, android.R.layout.simple_list_item_1, foundWords);
                ListView wordList = (ListView) findViewById(R.id.list_foundWords);
                wordList.setAdapter(wordAdapter);

                resetPressedStatus();
                resetBoardButtons();
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
                player.resetTimer();
            }
        }

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Noting to do!!
    }



    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }

    public class Timer extends CountDownTimer {
        public Timer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onFinish() {
            text_timer.setText("TIME'S UP!");
            gameOver();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            text_timer.setText("Timer: " + millisUntilFinished / 1000);
            //text_timer.setText(millisUntilFinished/60000 + ":" + millisUntilFinished/1000 % (millisUntilFinished/60000*60));

        }
    }

    private void gameOver() {
        Intent intend = new Intent(getApplicationContext(), GameOver.class);
        intend.putExtra("PLAYER_SCORE", Integer.toString(player.getScore()));
        intend.putExtra("FOUND_WORDS", player.getFoundWords());
        intend.putExtra("POSSIBLE_WORDS", allValidWords);

        startActivity(intend);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    //just a prototype
    private int isWord(String word) {
        return 2;
    }

    public void resetPressedStatus() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {

                // for touch
                touchVisited[i][j] = false;
                tInputWord = "";
                tlRow = 0;
                tlCol = 0;
                tPressCount = 0;
            }
        }
    }

    public void initBoard() {
        // board init and handler
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                final int ButtonNum = i * 4 + j;
                final int row = i;
                final int col = j;
                String str = String.valueOf(board[i][j]);
                BoardButton[ButtonNum].setTextColor(Color.BLACK);
                BoardButton[ButtonNum].setText(str);
            }
        }
    }

    // reset text color and background of grid to unpressed
    private void resetBoardButtons() {
        for (int i = 0; i < BoardButton.length; i++) {
            BoardButton[i].setTextColor(Color.BLACK);
            BoardButton[i].setBackgroundResource(R.drawable.background1);
        }
    }

    // check and update inputWord
    private boolean checkOnTouch(int tRol, int tCol) {

        if (tPressCount == 0) {
            tPressCount++;
            tlRow = tRol;
            tlCol = tCol;
            touchVisited[tRol][tCol] = true;
            tInputWord = tInputWord + board[tRol][tCol];
            return true;
        }

        // in case if touch button that is the same as previous one
        // dont update or change anything, return true, so caller change
        // the color of button to pressed
        if (tlRow == tRol && tlCol == tCol)
            return true;

        if (touchVisited[tRol][tCol] == true) {
            resetPressedStatus();
            return false;
        }


        // check if touch button is next to previous one or not
        boolean isNextToLastButton = false;
        int[] rowIdx = {0, 0, 1, 1, 1, -1, -1, -1};
        int[] colIdx = {1, -1, 0, 1, -1, 0, 1, -1};
        int tempRow, tempCol;

        for (int i = 0; i < 8; i++) {
            tempRow = tlRow + rowIdx[i];
            tempCol = tlCol + colIdx[i];
            if (tempRow < 0 || tempCol < 0 || tempRow > 3 || tempCol > 3)
                continue;
            if ((tempRow == tRol) && (tempCol == tCol)) {
                isNextToLastButton = true;
                break;
            }
        }


        if (isNextToLastButton == true) {
            tPressCount++;
            tlRow = tRol;
            tlCol = tCol;
            touchVisited[tRol][tCol] = true;
            tInputWord = tInputWord + board[tRol][tCol];

            return true;

        } else {
            resetPressedStatus();
            return false;
        }

    }


    // ======================= HANDLING SLIDING =================================

    /**
     * Listen to finger touch and get the path
     */
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        // get coord
        int X = (int) motionEvent.getRawX();
        int Y = (int) motionEvent.getRawY();

        int eventAction = motionEvent.getAction();
        switch (eventAction) {

            case MotionEvent.ACTION_DOWN:
                trackCoordinate(X, Y);
                break;

            case MotionEvent.ACTION_MOVE:
                trackCoordinate(X, Y);
                break;

            case MotionEvent.ACTION_UP:
                submitAction(this);
                break;

        }

        return true;

    }

    /**
     * take X, Y positions from touch event and check if location is
     * inside grid or not, and adds letter to inputWord
     */
    private void trackCoordinate(int x, int y) {

        int bttX, bttY, index;

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                index = 4 * i + j;

                bttX = locationMatrix[i][j].x;
                bttY = locationMatrix[i][j].y;

                // check if x,y location of finger touch is in boundary of
                // button
                if ((bttX + offset) < x && x < (bttX + 5 * offset)) {
                    if ((bttY + offset) < y && y < (bttY + 5 * offset)) {

                        // handling if touch inside a button
                        boolean ret = checkOnTouch(i, j);

                        if (ret) {
//                            BoardButton[index].setBackgroundColor(Color.RED);
                            BoardButton[index].setBackgroundResource(R.drawable.background3);
                            BoardButton[index].setTextColor(Color.BLACK);
                            text_display.setText(tInputWord);
                        } else {
                            resetBoardButtons();
                            text_display.setText(tInputWord);
                        }

                    }
                }
            }
        }

    }

    // make start and end menu invisible
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem start = menu.findItem(R.id.item_start);
        MenuItem end = menu.findItem(R.id.item_end);
        start.setVisible(false);
        end.setVisible(false);
        return true;
    }


}



// ============================= comment out stuffs
//    private boolean checkOnClick(int row, int col) {
//        if (buttonStatus[row][col] == true) {
//            resetPressedStatus();
//            return false;
//        }
//
//        if (pressCount == 0) {
//            pressCount += 1;
//            lastRow = row;
//            lastCol = col;
//
//            buttonStatus[row][col] = true;
//
//            inputWord = inputWord + board[row][col];
//
//            return true;
//        }
//
//        boolean isNextToLastButton = false;
//        int[] rowIdx = {0, 0, 1, 1, 1, -1, -1, -1};
//        int[] colIdx = {1, -1, 0, 1, -1, 0, 1, -1};
//        int tempRow, tempCol;
//
//        for (int i = 0; i < 8; i++) {
//            tempRow = lastRow + rowIdx[i];
//            tempCol = lastCol + colIdx[i];
//            if (tempRow < 0 || tempCol < 0 || tempRow > 3 || tempCol > 3)
//                continue;
//            if ((tempRow == row) && (tempCol == col)) {
//                isNextToLastButton = true;
//                break;
//            }
//        }
//
//        if (isNextToLastButton == true) {
//            pressCount += 1;
//            lastRow = row;
//            lastCol = col;
//            buttonStatus[row][col] = true;
//
//            inputWord = inputWord + board[row][col];
//
//            return true;
//        } else {
//            resetPressedStatus();
//            return false;
//        }
//
//    }


