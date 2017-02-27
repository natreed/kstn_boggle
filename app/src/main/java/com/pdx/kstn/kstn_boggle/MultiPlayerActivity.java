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

    // double player variables
    public Player player1 = null;
    public Player player2 = null;
    public Player player = null;
    private boolean isMaster = false;



    // variables for innit game
    public String[][] board;
    boolean isCounterRunning = false;
    public CountDownTimer timer = null;
    public Dictionary dictionary = new Dictionary();
    public ArrayList<String> allValidWords = new ArrayList<String>();
    // load dictionary file

    String[] foundWords = {};
    public long totalTime = 180000;

    // variables for handling sliding + locations
    Point[][] locationMatrix = new Point[4][4];  //new Coordinate[4][4];
    int bttHeight, bttWidth, offset;         // offset should be 1/4 of width or height
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
    public TextView p1_score;
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

    /**
     * Array adapter for the conversation thread
     */
    private ArrayAdapter<String> mConversationArrayAdapter;

    /**
     * String buffer for outgoing messages
     */
    private StringBuffer mOutStringBuffer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.double_player_activity_layout);

        // load dictionary file
        try {
            InputStream in = getResources().openRawResource(R.raw.dictionary);
            dictionary.createDictionary(in);
        } catch (Exception e) {
        }

        // call init layout
        initLayoutVariables();

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

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mBluetoothService != null) {
            if (mBluetoothService.getState() == BlueToothService.STATE_NONE) {
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

    // ======================== Send + Receive data from bluetooth

    private void sendMessage(String message) {

        // Check that we're actually connected before trying anything
        if (mBluetoothService.getState() != BlueToothService.STATE_CONNECTED) {
            Toast.makeText(this, "Not Connected", Toast.LENGTH_SHORT).show();
            return;
        }
        // Check that there's actually something to send
        if (message.length() > 0) {
            // Get the message bytes and tell the Bluetooth service  to write
            byte[] send = message.getBytes();
            mBluetoothService.write(send);

            // may add something to show that some stuff is sending out
        }
    }



    // The Handler that gets information back from the BluetoothService
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_WRITE:
                    byte[] writeBuf = (byte[]) msg.obj;
                    // construct a string from the buffer
                    String writeMessage = new String(writeBuf);
//                    mBluetoothAdapter.notifyDataSetChanged();
//                    messageList.add(new androidRecyclerView.Message(counter++, writeMessage, "Me"));
                    break;
                case MESSAGE_READ:
                    byte[] readBuf = (byte[]) msg.obj;
                    // construct a string from the valid bytes in the buffer
                    String readMessage = new String(readBuf, 0, msg.arg1);
//                    mBluetoothAdapter.notifyDataSetChanged();
//                    messageList.add(new androidRecyclerView.Message(counter++, readMessage, mConnectedDeviceName));
                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
//                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
//                    Toast.makeText(getApplicationContext(), "Connected to "
//                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST),
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    
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
                    text_display.setText("im mad");

                } else {
                    // User did not enable Bluetooth or an error occured
                   // Toast.makeText(this, R.string.bt_not_enabled_leaving, Toast.LENGTH_SHORT).show();
                    finish();
                }
        }
    }





//    /**
//     * Establish connection with other device
//     *
//     * @param data   An {@link Intent} with {@link DeviceListActivity#EXTRA_DEVICE_ADDRESS} extra.
//     * @param secure Socket Security type - Secure (true) , Insecure (false)
//     */
//    private void connectDevice(Intent data, boolean secure) {
//        // Get the device MAC address
//        String address = data.getExtras()
//                .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
//        // Get the BluetoothDevice object
//        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
//        // Attempt to connect to the device
//        mBluetoothService.connect(device, secure);
//    }


    public void connect(View v) {
        Intent serverIntent = new Intent(this, DeviceListActivity.class);
        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
    }




    // ================================ Boggle game setup + game management

    private void setWaitingGame() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                BoardButton[i*4+j].setText(" ");
            }
        }

        text_display.setText("Waiting for Game to Start!");
        p1_score.setText(" ");

        isTouchAble = false;
    }

    private void setupNewGame() {

        board = BoardGenerate.createNewBoard();
        allValidWords = BoggleSolver.solver(board, dictionary);

        resetPressedStatus();
        initBoard();

        // init or reset player, score, time
        player = new Player(text_timer, getApplicationContext());
        p1_score.setText("Score: 0");

        setupTimer();
    }

    private void setupTimer() {
        // set up timer
        timer = new Timer(totalTime, 1000);
        timer.start();

    }
























    //============================== stuff from single player activity ==========================

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

        offset = bttWidth / 4;

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

        ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(MultiPlayerActivity.this, android.R.layout.simple_list_item_1, foundWords);
        wordList = (ListView) findViewById(R.id.list_foundWords);
        wordList.setAdapter(wordAdapter);

        text_timer = (TextView) findViewById(R.id.time_remaining);

        // handling cancel button
        btt_cancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resetBoardButtons();
                resetPressedStatus();
                text_display.setText(tInputWord);
            }
        });

    }


    private void submitAction() {
        button_submit_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // check inputWord is in list
                int ret = player.updateInfor(tInputWord, allValidWords);

                if (ret == -1)
                    text_display.setText("Invalid word!");
                else if (ret == 0)
                    text_display.setText("Invalid, \"" + tInputWord + "\" found!");
                else if (ret == 1) {
                    text_display.setText("Valid Word!");

                    p1_score.setText("Score: " + Integer.toString(player.getScore()));

                    foundWords = player.getFoundWords().toArray(new String[0]);

                    ArrayAdapter<String> wordAdapter = new ArrayAdapter<String>(MultiPlayerActivity.this, android.R.layout.simple_list_item_1, foundWords);
                    wordList.setAdapter(wordAdapter);
                }

                resetBoardButtons();
                resetPressedStatus();

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
            gameOver();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            text_timer.setText("Timer: " + millisUntilFinished / 1000);
            //text_timer.setText(millisUntilFinished/60000 + ":" + millisUntilFinished/1000 % (millisUntilFinished/60000*60));

        }
    }

    // need to edit this for double player
    private void gameOver() {
        Intent intend = new Intent(getApplicationContext(), GameOver.class);
        intend.putExtra("PLAYER_SCORE", Integer.toString(player.getScore()));
        intend.putExtra("FOUND_WORDS", player.getFoundWords());
        intend.putExtra("POSSIBLE_WORDS", allValidWords);

        startActivity(intend);
    }


    // ============================= Menu handling ===========================

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_connection: {
                // Launch the DeviceListActivity to see devices and do scan
                Intent serverIntent = new Intent(this, DeviceListActivity.class);
                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_SECURE);
                if (mBluetoothService.getState() == BluetoothConnectionService.STATE_LISTEN) {
                    isMaster = true;
                    text_display.setText("Listening!");
                }
                return true;
            }
//            case R.id.insecure_connect_scan: {
//                // Launch the DeviceListActivity to see devices and do scan
//                Intent serverIntent = new Intent(getActivity(), DeviceListActivity.class);
//                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE_INSECURE);
//                return true;
//            }
            case R.id.item_discoverable: {
                // Ensure this device is discoverable by others
                ensureDiscoverable();
                return true;
            }
        }
        return false;
    }




    // ========================= Bluetooth handling

    /**
     * Makes this device discoverable for 300 seconds (5 minutes).
     */
    private void ensureDiscoverable() {
        if (mBluetoothAdapter.getScanMode() !=
                BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE) {
            Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
            discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);
            startActivity(discoverableIntent);
        }
    }

    public void discoverable(View v) {
        ensureDiscoverable();
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
                if ((bttX + offset) < x && x < (bttX + 3 * offset)) {
                    if ((bttY + offset) < y && y < (bttY + 3 * offset)) {

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





