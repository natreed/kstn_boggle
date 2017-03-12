package com.pdx.kstn.kstn_boggle;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by thanhhoang on 2/24/17.
 */

public class MultiPlayerActivity extends AppCompatActivity implements View.OnTouchListener {

    // variables for handling sliding + locations
    Point[][] locationMatrix = new Point[4][4];  //new Coordinate[4][4];
    int bttHeight, bttWidth, offset;         // offset should be 1/6 of width or height
    public boolean[][] touchVisited = new boolean[4][4];
    public int tlRow = 0, tlCol = 0, tPressCount = 0;
    public String tInputWord = "";
    public boolean isTouchAble = false;

    // layout variables
    public Button BoardButton[] = new Button[16];
    public Button btt_cancel;
    public Button button_submit_word;
    public TextView text_timer;
    public TextView text_display;       // display system messages, word
    public TextView player_score;
    public ListView wordList;
    public RelativeLayout mainLayout;

    // Message types sent from the game Handler
    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE = 1;

    // Local Bluetooth adapter, service
    public  BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothConnectionService mBluetoothService = null;

    // Intent request codes
    private static final int REQUEST_CONNECT_DEVICE_SECURE = 1;
    private static final int REQUEST_CONNECT_DEVICE_INSECURE = 2;
    private static final int REQUEST_ENABLE_BT = 3;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;

    // double player variables

    public static final int NEW_ROUND_COND = 5;
    public boolean stateChangeMsgRead = false;
    public Player player = null;
    private boolean isMaster = false;
    private boolean isGameOn = false;
    private int roundNum = 0;
    private boolean newGameFlag = false;
    private boolean player1Stopped = false;
    private boolean player2Stopped = false;
    private boolean isCutthroat;
    private boolean isGameOver = false;
    private boolean isWinner = false;
    private int opponentScore = 0;
    private boolean opponentIsMaster;
    private int p1NumFound = 0, p2NumFound = 0;
    private ArrayList<String> foundWordsList = new ArrayList<String>();


    // variables for innit game
    public String[][] board = new String[4][4];
    public Dictionary dictionary = new Dictionary();
    public ArrayList<String> allValidWords = new ArrayList<String>();
    // load dictionary file

    String[] foundWords = {};

    // type of message string
    public static final int MESSAGE_TYPE_BOGGLE_BOARD = 1;
    public static final int MESSAGE_TYPE_PLAYER_FOUND_WORD = 2;
    public static final int MESSAGE_TYPE_START_GAME = 4;
    public static final int MESSAGE_TYPE_END_GAME = 5;
    public static final int MESSAGE_TYPE_SIGNAL_NEW_ROUND = 6;
    public static final int MESSAGE_TYPE_OPPONENT_MASTER = 7;
    public static final int MESSAGE_TYPE_HOST_SELECTED = 8;
    public boolean gameoverMsgSent = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.double_player_activity_layout);

        // extract game mode, and difficulty from previous activity
		Intent intent = getIntent();
        String mode = intent.getStringExtra("MODE");
        if (mode.equals("cutthroat")) {
            isCutthroat = true;
            Toast.makeText(this, "Playing in cutthroat mode",Toast.LENGTH_LONG).show();
        }

        // call init layout, players variables, dictionary
        initVariables();

        // init location on screen for all components
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

        // set up game to not playing state
        setWaitingGame();

        // bluetooth does not support when it null
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "BLUETOOTH is NOT AVAILABLE", Toast.LENGTH_LONG).show();
            finish();
            return;
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        } else {
            if (mBluetoothService == null)
                // Initialize the BluetoothService to perform bluetooth connections
                mBluetoothService = new BluetoothConnectionService(this, mHandler);
        }

        // constantly running to check gameover condition
        gameOver();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothService != null) {
            if (mBluetoothService.getState() == BluetoothConnectionService.STATE_NONE) {
                mBluetoothService.start();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the Bluetooth  services
        if (mBluetoothService != null) mBluetoothService.stop();
    }

    // ======================== Bluetooth handling ==============================

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 30);
            startActivity(discoverableIntent);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CONNECT_DEVICE:
                // When DeviceListActivity returns with a device to connect
                if (resultCode == Activity.RESULT_OK) {
                    // Get the device MAC address
                    String address = data.getExtras().getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    // Get the BLuetoothDevice object
                    BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
                    // Attempt to connect to the device
                    mBluetoothService.connect(device);
                }
                break;
            case REQUEST_ENABLE_BT:
                // When the request to enable Bluetooth returns
                if (resultCode == Activity.RESULT_OK) {
                    // Bluetooth is now enabled, so set up a chat session
                    mBluetoothService = new BluetoothConnectionService(this, mHandler);
                    if (mBluetoothService.getState() == BluetoothConnectionService.STATE_CONNECTING) {
                        text_display.setText("State connecting");
                    }

                } else {
                    // User did not enable Bluetooth or an error occured
                   // Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }

    public void connect(View v) {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }

    public void sendMessage(String message, int messageType) {

        // Check that we're actually connected before trying anything
        if (mBluetoothService.getState() != BluetoothConnectionService.STATE_CONNECTED) {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
            return;
        }

        String mes = messageType + message;

        // Get the message bytes and tell the Bluetooth service  to write
        byte[] send = mes.getBytes();
        mBluetoothService.write(send);
    }

    private void sendImMaster () {
        sendMessage("", MESSAGE_TYPE_OPPONENT_MASTER);
    }
    // The Handler that gets information back from the BluetoothService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    break;

                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMsg = new String(readBuf, 0, msg.arg1);

                    String realMsg = readMsg.substring(1, readMsg.length());

                    System.out.println(readMsg);
                    System.out.println(realMsg);
                    System.out.println(Character.getNumericValue(readMsg.charAt(0)));

                    switch (Character.getNumericValue(readMsg.charAt(0))) {

                        case MESSAGE_TYPE_BOGGLE_BOARD:
                            //Toast.makeText(getApplicationContext(), "Received a board", Toast.LENGTH_SHORT).show();

                            if (roundNum == 0) {
                                isGameOn = true;
                                board = MessageConverter.messageToBoard(realMsg);
                                Toast.makeText(getApplicationContext(), "Press Start to Ready", Toast.LENGTH_LONG).show();
                            } else {
                                // new round:
                                if (p1NumFound >= NEW_ROUND_COND && p2NumFound >= NEW_ROUND_COND) {
                                    board = MessageConverter.messageToBoard(realMsg);
                                    Toast.makeText(getApplicationContext(), "New Round Start!", Toast.LENGTH_LONG).show();
                                    player2StartGame();
                                }
                            }

                            break;

                        case MESSAGE_TYPE_HOST_SELECTED:
                            if (isMaster == true)
                                isMaster = false;

                            break;

                        case MESSAGE_TYPE_START_GAME:
                            // player 2 is ready (has board + possible words) -> start game
                            // if player 1
                            player1StartGame();
                            Toast.makeText(getApplicationContext(), "Game Start", Toast.LENGTH_LONG).show();
                            break;

                        case MESSAGE_TYPE_PLAYER_FOUND_WORD:
                            if (!isMaster) {
                                p1NumFound++;
                                if (isCutthroat) {
                                    foundWordsList.add(realMsg);

                                    foundWords = foundWordsList.toArray(new String[0]);

                                    //send found words in message to other player
                                    ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(MultiPlayerActivity.this, R.layout.mywhite_listview, foundWords);
                                    wordList.setAdapter(wordAdapter);
                                }
//                                Toast.makeText(getApplicationContext(), "Player 1 found 1 more word", Toast.LENGTH_LONG).show();
                            } else {
                                p2NumFound++;
                                if (isCutthroat) {
                                    foundWordsList.add(realMsg);

                                    foundWords = foundWordsList.toArray(new String[0]);

                                    //send found words in message to other player
                                    ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(MultiPlayerActivity.this, R.layout.mywhite_listview, foundWords);
                                    wordList.setAdapter(wordAdapter);
                                }
//                                Toast.makeText(getApplicationContext(), "Player 2 found 1 more word", Toast.LENGTH_LONG).show();
                            }

                            break;

                        case MESSAGE_TYPE_SIGNAL_NEW_ROUND:
                            if (isMaster) player2Stopped = true;
                            else player1Stopped = true;

                            if (player1Stopped && player2Stopped && isMaster) {
                                // master setup new round + send board
                                newRound();
                            }
                            break;

                        case MESSAGE_TYPE_END_GAME:
                            String msg1 = realMsg.split(";")[0];
                            if (gameoverMsgSent == false) {
                                gameoverSignal();  // send back game score;
                                isWinner = true;
                                gameoverMsgSent = true;

                                opponentScore  = Integer.parseInt(msg1);
                                isGameOver = true;
                                break;
                            }
                            else {
                                opponentScore = Integer.parseInt(msg1);
                                isGameOver = true;
                                break;
                            }

                    }

                    break;

                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;

                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_LONG).show();

                    break;
            }
        }
    };


    // ================================ Boggle game setup + game management

    private void setWaitingGame() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                BoardButton[i*4+j].setText(" ");
            }
        }

        text_display.setText("Waiting for Game to Start!");
        player_score.setText(" ");

        isTouchAble = false;
    }

    // call when bluetooth is connected, this is for master player to call only
    private void setupNewGame() {
        if (!checkBluetoothConnection()) return;

        if (isMaster) {
            // generate board + valid words, then send to slave player
            Toast.makeText(this, "Waiting for other player to get ready.", Toast.LENGTH_LONG).show();
            board = BoardGenerate.createNewBoard();

            // send board + valid words
            sendMessage(MessageConverter.boardToMessage(board), MESSAGE_TYPE_BOGGLE_BOARD );
            Toast.makeText(getApplicationContext(), "Sending a Board to 2nd Player", Toast.LENGTH_SHORT).show();

            isGameOn = true;
        }

    }

    // after receive signal start game from player 2, do start game
    private void player1StartGame() {
        if (!checkBluetoothConnection()) return;

        Thread thread_boardSolver = new Thread(new Runnable() {
            public void run()
            {
                allValidWords = BoggleSolver.solver(board, dictionary);
                //player1.setAllVallidWords(allValidWords);
                player.setAllVallidWords(allValidWords);
            }
        });
        thread_boardSolver.start();

        initBoard();
        resetPressedStatus();


        isTouchAble = true;
        if (roundNum == 0)
            //  player1.initiateTimer();
            player.initiateTimer();
        else {
            // new round
            p1NumFound = 0;
            p2NumFound = 0;
            player1Stopped = false;
            player2Stopped = false;
            System.out.println("round: " + roundNum);
            //player1.moveToNextRound();
            player.moveToNextRound();
        }
        roundNum++;
    }

    // start game for player2, and signel player 1 to start game
    private void player2StartGame() {
        if (!checkBluetoothConnection()) return;

        Thread thread_boardSolver = new Thread(new Runnable() {
            public void run()
            {
                allValidWords = BoggleSolver.solver(board, dictionary);
                //player2.setAllVallidWords(allValidWords);
                player.setAllVallidWords(allValidWords);
            }
        });
        thread_boardSolver.start();

        initBoard();
        resetPressedStatus();

        isTouchAble = true;
        if (roundNum == 0)
            //player2.initiateTimer();
            player.initiateTimer();
        else {
            // move to new round
            p1NumFound = 0;
            p2NumFound = 0;
            player1Stopped = false;
            player2Stopped = false;
            System.out.println("round: " + roundNum);
            player.moveToNextRound();
        }
        roundNum++;
    }

    private boolean checkBluetoothConnection() {
        if (mBluetoothService.getState() != BluetoothConnectionService.STATE_CONNECTED) {
            Toast.makeText(getApplicationContext(), "Please Connect to other Device", Toast.LENGTH_SHORT).show();
            return false ;
        } else {
            return true;
        }
    }

    // ============================= Menu handling ===========================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.findItem(R.id.item_end);
        if (isCutthroat)
            item.setVisible(false);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_connection: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
//                if (mBluetoothService.getState() == BluetoothConnectionService.STATE_LISTEN) {
//                    isMaster = true;
//                    text_display.setText("Listening!");
//                }
                return true;
            }

            case R.id.item_discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }

            case R.id.item_ashost: {
                isMaster = true;
                sendMessage("host selected", MESSAGE_TYPE_HOST_SELECTED);

                return true;
            }


            case R.id.item_start: {
                if (mBluetoothService.getState() == BluetoothConnectionService.STATE_CONNECTED) {

                    if (isMaster) {

                        if (newGameFlag == false) {
                            setupNewGame();
                            item.setVisible(false);
                            newGameFlag = true;
                        }

                    } else {

                        if (isGameOn) {
                            sendMessage("Start Game", MESSAGE_TYPE_START_GAME);
                            player2StartGame();
                            item.setVisible(false);
                        }
                    }

                }

                return true;
            }


            case R.id.item_end: {

                if (isMaster) {

                    if (p1NumFound >= NEW_ROUND_COND) {
                        player.pauseTimer();
                        player1Stopped = true;
                        sendMessage("Player 1 stopped the timer!", MESSAGE_TYPE_SIGNAL_NEW_ROUND);
                    }

                    if (player1Stopped == true && player2Stopped == true)
                        newRound();

                }
                else {
                    if (p2NumFound >= NEW_ROUND_COND) {
                        player.pauseTimer();
                        player2Stopped = true;
                        sendMessage("Player 2 stopped the timer!", MESSAGE_TYPE_SIGNAL_NEW_ROUND);
                    }
                }

                return true;
            }
        }

        return false;
    }



    //============================== stuff from single player activity ==========================
    // ========= setup layout, drag to select, set onClickListener,

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

    private void initVariables() {

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
        player_score = (TextView) findViewById(R.id.text_player_score);

        btt_cancel = (Button) findViewById(R.id.button_cancel);
        button_submit_word = (Button) findViewById(R.id.button_submitWord);
        player_score = (TextView) findViewById(R.id.text_player_score);
        text_display = (TextView) findViewById(R.id.text_display_screen);

        ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(MultiPlayerActivity.this, R.layout.mywhite_listview, foundWords);
        wordList = (ListView) findViewById(R.id.list_foundWords);
        wordList.setAdapter(wordAdapter);

        text_timer = (TextView) findViewById(R.id.time_remaining);

        // load dictionary file
        try {
            InputStream in = getResources().openRawResource(R.raw.dictionary);
            dictionary.createDictionary(in);
        } catch (Exception e) {
        }

        player = new Player(text_timer, getApplicationContext());
        player.isMultiplay = true;
        // handling cancel button, need to move to somewhere else
        btt_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetBoardButtons();
                resetPressedStatus();
            }
        });


    }

    private void newRound() {

        if (p1NumFound >= NEW_ROUND_COND && p2NumFound >= NEW_ROUND_COND) {
            if (isMaster) {
                setupNewGame();
                player1StartGame();
            }
        }
    }

    private void submitAction() {
        button_submit_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check inputWord is in list
                if (isCutthroat) {
                    if (foundWordsList.contains(tInputWord) == true) {
                        text_display.setText("Invalid, \"" + tInputWord + "\" found!");
                        resetBoardButtons();
                        resetPressedStatus();
                        return;
                    }
                }

                int ret = player.updateInfor(tInputWord, allValidWords);

                if (ret == -1)
                    text_display.setText("Invalid word!");
                else if (ret == 0)
                    text_display.setText("Invalid, \"" + tInputWord + "\" found!");
                else if (ret == 1) {
                    text_display.setText("Valid Word!");

                    player_score.setText("Score: " + Integer.toString(player.getScore()));
                    foundWordsList.add(tInputWord);

                    foundWords = foundWordsList.toArray(new String[0]);

                    //send found words in message to other player
                    ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(MultiPlayerActivity.this, R.layout.mywhite_listview, foundWords);
                    wordList.setAdapter(wordAdapter);

                    if (isMaster)
                        p1NumFound++;
                    else
                        p2NumFound++;

                    sendMessage(tInputWord, MESSAGE_TYPE_PLAYER_FOUND_WORD);

                }

                resetBoardButtons();
                resetPressedStatus();

            }
        });
    }

    private void gameoverSignal() {
        String msg = "" + player.getScore() + ";";
        if (isMaster)
            System.out.println("Score sent from Master " + msg);
        System.out.println("Score sent from Slave " + msg);
        sendMessage(msg, MESSAGE_TYPE_END_GAME);
    }


    // need to edit this for double player
    private void gameOver() {

        Thread thread_gameOver = new Thread(new Runnable() {
            public void run()
            {
                boolean flag = false;
                while (isGameOver == false) {

                    if (player.isTimeUp && flag == false) {
                        if (isCutthroat && isMaster) {
                            sendMessage("" + player.getScore() + ";", MESSAGE_TYPE_END_GAME);
                            flag = true;
                            System.out.println("HELLOMSG 1");
                            gameoverMsgSent = true;
                        } else if (!isCutthroat && flag == false) {
                            sendMessage("" + player.getScore() + ";", MESSAGE_TYPE_END_GAME);
                            System.out.println("HELLOMSG 2");
                            flag = true;
                            gameoverMsgSent = true;
                        }
                    }
                }

                System.out.println("myscore" + player.getScore());
                System.out.println("opponent score " + opponentScore);
                mBluetoothService = null;
                player.gameOverMultiplayer(getApplicationContext(), isWinner, isCutthroat, opponentScore);

            }
        });
        thread_gameOver.start();

    }



    //just a prototype
    private int isWord(String word) {
        return 2;
    }


    // ========================== Board handling ===============================


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

    // int board to board layout, set color of text to black,
    public void initBoard() {
        // board init and handler
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                final int ButtonNum = i * 4 + j;
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
        if (isTouchAble == false)
            return false;


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
                submitAction();
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

}





