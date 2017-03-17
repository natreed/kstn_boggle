package com.pdx.kstn.kstn_boggle;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

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

    public String difficulty = null;
    public String mode = null;
    public String fileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ArrayList<String> levels = new ArrayList<String>();
        levels.add("Easy");
        levels.add("Medium");
        levels.add("Hard");
        /*
        levels.add("Basic");
        levels.add("Cutthroat");
         */
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);
        Intent intent = getIntent();
        difficulty = intent.getStringExtra("LEVEL");
        mode = intent.getStringExtra("MODE");
        fileName = mode + difficulty;

        System.out.println(fileName);
        // load and display high scores
        ArrayList<String> scores = new ArrayList<String>(); //, mediumScores = null, hardScores = null;

        try {
            //InputStream in = getResources().openRawResource(R.raw.scores);
            /*
            // Add this block of code at the end of the game.
             */
            HighScore highScore = new HighScore(ScoresActivity.this, fileName);
            //HighScore mediumHighScore = new HighScore(ScoresActivity.this, "medium");
            //HighScore hardHighScore = new HighScore(ScoresActivity.this, "hard");

            //highScore.resetScores();
            //highScore.updateScore("New_Player_two", 2);
            scores = new ArrayList<String>(highScore.scores);
            //mediumScores = new ArrayList<String>(easyHighScore.scores);
            //hardScores = new ArrayList<String>(easyHighScore.scores);
        } catch (Exception e) { e.printStackTrace();}

        ArrayAdapter<String> scoreAdapter = new ArrayAdapter<String>(ScoresActivity.this, R.layout.mywhite_listview, scores);
        ListView scoreList = (ListView) findViewById(R.id.Score_List);
        scoreList.setAdapter(scoreAdapter);
        TextView titleView = (TextView) findViewById(R.id.difficulty_text);
//        String title = levels.get(Integer.parseInt(difficulty)) + " Scores";
//        titleView.setText(title);

        final Button resetButton = (Button) findViewById(R.id.reset_button);

        resetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Perform action on click
                // Start NewActivity.class
                HighScore highScore = new HighScore(ScoresActivity.this, difficulty);
                try {
                    highScore.resetScores();
                } catch (Exception e) {e.printStackTrace();}
//                highScore = new HighScore(ScoresActivity.this, "medium");
//                try {
//                    highScore.resetScores();
//                } catch (Exception e) {e.printStackTrace();}
//                highScore = new HighScore(ScoresActivity.this, "hard");
//                try {
//                    highScore.resetScores();
//                } catch (Exception e) {e.printStackTrace();}
            }
        });

    }



}