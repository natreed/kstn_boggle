package com.pdx.kstn.kstn_boggle;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;


/**
 * Created by thanhhoang on 1/28/17.
 */

public class ScoresActivity extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);
        // load and display high scores
        ArrayList<String> scores = null;

        try {
            //InputStream in = getResources().openRawResource(R.raw.scores);
            /*
            // Add this block of code at the end of the game.
             */
            HighScore highScore = new HighScore(ScoresActivity.this);
            //highScore.resetScores();
            //highScore.updateScore("New_Player_two", 2);
            scores = new ArrayList<String>(highScore.scores);
        } catch (Exception e) { e.printStackTrace();}

        ArrayAdapter<String> scoreAdapter = new ArrayAdapter<String>(ScoresActivity.this, R.layout.mywhite_listview, scores);
        ListView scoreList = (ListView) findViewById(R.id.Score_List);
        scoreList.setAdapter(scoreAdapter);

        final Button resetButton = (Button) findViewById(R.id.reset_button);

        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // Start NewActivity.class
                HighScore highScore = new HighScore(ScoresActivity.this);
                try {
                    highScore.resetScores();
                } catch (Exception e) {e.printStackTrace();}
            }
        });

    }



}