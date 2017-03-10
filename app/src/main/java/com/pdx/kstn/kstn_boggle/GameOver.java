package com.pdx.kstn.kstn_boggle;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.inputmethodservice.ExtractEditText;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by thanhhoang on 2/18/17.
 */

public class GameOver extends Activity {
    private String name = "";
    private HighScore highScore;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);

        // setListAdapter(adapter);

        highScore = new HighScore(getApplicationContext());

        Intent intent = getIntent();
        score = Integer.parseInt(intent.getStringExtra("PLAYER_SCORE"));
        Toast.makeText(this,"score is " +Integer.toString(score),Toast.LENGTH_LONG).show();
        ArrayList<String> foundWords = intent.getStringArrayListExtra("FOUND_WORDS");
        ArrayList<String> possibleWords = intent.getStringArrayListExtra("POSSIBLE_WORDS");

        ListView listView = (ListView) findViewById(R.id.list);
        ResultAdapter adapter = new ResultAdapter(this, score, possibleWords, foundWords);
        listView.setAdapter(adapter);

        System.out.println("Total possible words: " + possibleWords.size());
        //HighScore highScore = new HighScore(getApplicationContext());
        if (score >= highScore.lowestScore() || highScore.scores.size() < 5) {
            getName();
            System.out.println("Name: " + name);
            /*try {
                highScore.updateScore(name, score);
            } catch (Exception e) {e.printStackTrace();}*/

        }

//        final Button bttReplay = (Button) findViewById(R.id.button_replay);
//        final Button bttHome = (Button) findViewById(R.id.button_home);
//
//        bttReplay.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//                // Start NewActivity.class
//                Intent myIntent = new Intent(getApplicationContext(),
//                        PlayerActivity.class);
//                startActivity(myIntent);
//            }
//        });
//
//        bttHome.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                // Perform action on click
//                // Start NewActivity.class
//                Intent myIntent = new Intent(getApplicationContext(),
//                        MainActivity.class);
//                startActivity(myIntent);
//            }
//        });

    }

    private void getName() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("New High Score! Enter Name:");


        final EditText input = new EditText(this); //(EditText) promptView.findViewById(R.id.player_name);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Player name");
        builder.setView(input);
        //setup Buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.out.println("Hey, here is name");
                name  = input.getText().toString();
                try {
                    highScore.updateScore(name, score);
                } catch (Exception e) {e.printStackTrace();}
                System.out.println(name);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();

    }
}
