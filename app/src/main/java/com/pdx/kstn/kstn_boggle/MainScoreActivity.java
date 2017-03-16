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
import android.widget.TextView;
import android.widget.Toast;

public class MainScoreActivity extends Activity {


        public Button btt_startgame;
        public RadioGroup radioGroup_mode, radioGroup_difficulty;
        public RadioButton rbtt_mode, rbtt_difficulty;
        public TextView title;


        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.choose_double_playe_mode);

            initLayout();
            addListenerOnButtons();
        }


        private void initLayout() {
            btt_startgame = (Button) findViewById(R.id.button_start_game);

            radioGroup_mode = (RadioGroup) findViewById(R.id.group_mode);
            radioGroup_difficulty = (RadioGroup) findViewById(R.id.group_difficulty);

            title = (TextView) findViewById(R.id.screen_title);
            btt_startgame.setText("Get Scores");
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

                    Intent intend = new Intent(getApplicationContext(), ScoresActivity.class);
                    intend.putExtra("MODE", str_mode);
                    intend.putExtra("LEVEL", str_difficulty);
                    startActivity(intend);

                }
            });

        }



    }


