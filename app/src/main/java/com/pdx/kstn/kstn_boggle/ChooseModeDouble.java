package com.pdx.kstn.kstn_boggle;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

/**
 * Created by thanhhoang on 3/11/17.
 */

public class ChooseModeDouble extends Activity{

    public Button btt_startgame, btt_discoverable, btt_connection;
    public RadioGroup radioGroup_mode, radioGroup_difficulty;
    public RadioButton rbtt_mode, rbtt_difficulty;
//    public RadioButton rbtt_basic, rbtt_cutthroat;
//    public RadioButton rbtt_easy, rbtt_medium, rbtt_hard;

    /**
     * Name of the connected device
     */
    private String mConnectedDeviceName = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_double_playe_mode);

        initLayout();
        addListenerOnButtons();
    }


    private void initLayout() {
        btt_startgame = (Button) findViewById(R.id.button_start_game);
        btt_discoverable = (Button) findViewById(R.id.button_discoverable);
        btt_connection = (Button) findViewById(R.id.button_connection);

        radioGroup_mode = (RadioGroup) findViewById(R.id.group_mode);
        radioGroup_difficulty = (RadioGroup) findViewById(R.id.group_difficulty);

    }

    private void addListenerOnButtons() {

        btt_startgame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 // get selected radio buttons from groupds (mode, difficulty)
                int selectedMode = radioGroup_mode.getCheckedRadioButtonId();
                int selectedDifficulty = radioGroup_difficulty.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rbtt_mode = (RadioButton) findViewById(selectedMode);
                rbtt_difficulty = (RadioButton) findViewById(selectedDifficulty);
                String str_mode, str_difficulty;
                str_mode = rbtt_mode.getText().toString().toLowerCase();
                str_difficulty = rbtt_difficulty.getText().toString().toLowerCase();

                if (str_mode.length() < 1 || str_difficulty.length() < 1)
                    return;

                // check bluetooth connection
                    Intent intend = new Intent(getApplicationContext(), MultiPlayerActivity.class);
                    intend.putExtra("MODE", str_mode);
                    intend.putExtra("DIFFICULTY", str_difficulty);
                    startActivity(intend);



            }
        });

    }



}
