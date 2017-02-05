package com.pdx.kstn.kstn_boggle;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    //I am replacing the previous comment now.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        final Button single_player_mode = (Button) findViewById(R.id.button_single_player_mode);
        final Button double_player_mode = (Button) findViewById(R.id.button_double_player_mode);

        System.out.print("Hello hello\n");

        single_player_mode.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // Start NewActivity.class
//                Intent myIntent = new Intent(getApplicationContext(),
//                        SinglePlayerActivity.class);
//                startActivity(myIntent);
                //Source of the data in the DIalog
                CharSequence[] difficultylevel = {"Easy","Medium","Hard"};
                // 1. Instantiate an AlertDialog.Builder with its constructor
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                // 2. Chain together various setter methods to set the dialog characteristics
                                builder.setSingleChoiceItems(difficultylevel, 0, new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub

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
                                AlertDialog dialog = builder.create();
                                dialog.show();

            }
        });

    }

}
