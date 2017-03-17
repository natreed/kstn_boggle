package com.pdx.kstn.kstn_boggle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //I am replacing the previous comment now.
    public static int selectedDifficultyLevel=0; //global variable to store state

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mainscreen);

        final Button button_play_game = (Button) findViewById(R.id.button_play);
        final Button button_scores = (Button) findViewById(R.id.button_scores);
        final Button button_rules = (Button) findViewById(R.id.button_rules);
        //final Button button_quit = (Button) findViewById(R.id.button_quit);

        button_play_game.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // Start NewActivity.class
                Intent myIntent = new Intent(getApplicationContext(),
                        ChooseModeMainActivity.class);
                startActivity(myIntent);
            }
        });

        button_scores.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                 Intent myIntent = new Intent(getApplicationContext(),
                                        MainScoreActivity.class);
                startActivity(myIntent);

            }
        });

        button_rules.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // Start NewActivity.class
                Intent myIntent = new Intent(getApplicationContext(),
                        RulesActivity.class);
                startActivity(myIntent);
            }
        });


    }

}
