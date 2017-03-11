package com.pdx.kstn.kstn_boggle;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    //I am replacing the previous comment now.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.mainscreen);

        final Button button_play_game = (Button) findViewById(R.id.button_play);
        final Button button_scores = (Button) findViewById(R.id.button_scores);
        final Button button_rules = (Button) findViewById(R.id.button_rules);
        final Button button_quit = (Button) findViewById(R.id.button_quit);

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
                // Perform action on click
                // Start NewActivity.class
                Intent myIntent = new Intent(getApplicationContext(),
                        ScoresActivity.class);
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

        button_quit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // Start NewActivity.class
                //finish();
                System.exit(0);
            }
        });


        //setContentView(R.layout.activity_main);


//        final Button single_player_mode = (Button) findViewById(R.id.button_single_player_mode);
//        final Button double_player_mode = (Button) findViewById(R.id.button_double_player_mode);
//
//        System.out.print("Hello hello\n");
//
//

//        single_player_mode.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//                // Start NewActivity.class
//                Intent myIntent = new Intent(getApplicationContext(),
//                        SinglePlayerActivity.class);
//                startActivity(myIntent);
//            }
//        });

    }

}
