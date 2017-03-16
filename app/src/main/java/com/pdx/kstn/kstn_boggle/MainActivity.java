package com.pdx.kstn.kstn_boggle;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
                // Perform action on click
                CharSequence[] difficultylevel = {"Easy","Medium","Hard"};
                //CharSequence[] difficultylevel = {"Easy","Medium","Hard","Basic","Cutthroat"};
                AlertDialog.Builder builder_difficulty_level = new AlertDialog.Builder(MainActivity.this);

                // 2. Chain together various setter methods to set the dialog characteristics
                builder_difficulty_level.setSingleChoiceItems(difficultylevel, selectedDifficultyLevel, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
///                     Auto-generated method stub
                        selectedDifficultyLevel=which;}
                    })
                        .setTitle(R.string.dialog_title)
                        // Set the action buttons
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK, so save the mSelectedItems results somewhere
                                // or return them to the component that opened the dialog
                                Intent myIntent = new Intent(getApplicationContext(),
                                        ScoresActivity.class);
                                myIntent.putExtra("PLAYER_LEVEL", Integer.toString(selectedDifficultyLevel));
                                startActivity(myIntent);
                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }

                        });

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder_difficulty_level.create();
                dialog.show();

//                // Start NewActivity.class
//                Intent myIntent = new Intent(getApplicationContext(),
//                        ScoresActivity.class);
//                startActivity(myIntent);
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

        /*button_quit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // Start NewActivity.class
                //finish();
                System.exit(0);
            }
        });*/


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
