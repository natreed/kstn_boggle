package com.pdx.kstn.kstn_boggle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

// Just shows two buttons(single Player,multiplayer) , when user clicks on the buttons it lets users choose a difficulty level and mode (basic play, cutthroat)
// implemented via using dialog boxes
public class ChooseModeMainActivity extends AppCompatActivity {
    //I am replacing the previous comment now.
    public static int selectedDifficultyLevel=0; //global variable to store state
    public int selectedDifficultyLevelDoublePlayer=0;
    public int selectedDoublePlayerMode=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.choose_mode);
        // hiding action bar
        //getActionBar().hide();

        final Button single_player_mode = (Button) findViewById(R.id.button_single_player_mode);
        final Button double_player_mode = (Button) findViewById(R.id.button_double_player_mode);

        System.out.print("Hello hello\n");

        single_player_mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // Start NewActivity.class
               //startActivity(myIntent);
                //Source of the data in the DIalog
                CharSequence[] difficultylevel = {"Easy","Medium","Hard"};

                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder_difficulty_level = new AlertDialog.Builder(ChooseModeMainActivity.this);


                // 2. Chain together various setter methods to set the dialog characteristics
                       builder_difficulty_level.setSingleChoiceItems(difficultylevel, selectedDifficultyLevel, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                            selectedDifficultyLevel=which;
                                            }
                                        })
                                        .setTitle(R.string.dialog_title)
                                         // Set the action buttons
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User clicked OK, so save the mSelectedItems results somewhere
                                                // or return them to the component that opened the dialog
                                                Intent myIntent = new Intent(getApplicationContext(),
                                                PlayerActivity.class);
//                                                myIntent.putExtra("difficultylevel", String.valueOf(selectedDifficultyLevel));
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

            }
        });

        // double player mode
        double_player_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence[] mode = {"Basic Mode","Cutthroat Mode"};
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(ChooseModeMainActivity.this);
                // 2. Chain together various setter methods to set the dialog characteristics
                builder.setSingleChoiceItems(mode, selectedDoublePlayerMode, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // TODO Auto-generated method stub
                        selectedDoublePlayerMode = which;
                    }
                })
                        .setTitle("Which Mode do you want to Play??")
                        // Set the action buttons
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK
                                CharSequence[] difficultylevel = {"Easy","Medium","Hard"};
                                // 1. Instantiate an AlertDialog.Builder with its constructor
                                AlertDialog.Builder builder_difficulty_level = new AlertDialog.Builder(ChooseModeMainActivity.this);

                                // 2. Chain together various setter methods to set the dialog characteristics
                                builder_difficulty_level.setSingleChoiceItems(difficultylevel, selectedDifficultyLevelDoublePlayer, new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        selectedDifficultyLevelDoublePlayer =which;
                                    }
                                })
                                        .setTitle(R.string.dialog_title)
                                        // Set the action buttons
                                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {
                                                // User clicked OK, so save the mSelectedItems results somewhere
                                                // or return them to the component that opened the dialog

                                            }
                                        })
                                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id) {

                                            }
                                        });

                                // 3. Get the AlertDialog from create()
                                AlertDialog dialog_difficulty_level_multi = builder_difficulty_level.create();
                                dialog_difficulty_level_multi.show();

                            }
                        })
                        .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                            //User clicked cancel
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });

                // 3. Get the AlertDialog from create()
                AlertDialog dialog = builder.create();
                dialog.show();

            }


            });


    }

}
