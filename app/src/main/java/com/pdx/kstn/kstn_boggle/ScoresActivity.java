package com.pdx.kstn.kstn_boggle;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by thanhhoang on 1/28/17.
 */

public class ScoresActivity extends MainActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.high_scores);

        // load and display high scores
        String[] scores = {};

        try {
            //InputStream in = getResources().openRawResource(R.raw.scores);
            /*
            // Add this block of code at the end of the game.
             */
            FileInputStream fis = openFileInput("scores.txt");
            HighScore highScore = new HighScore();
            highScore.loadScores(fis);
            scores = highScore.getScores().toArray(new String[0]);
            highScore.updateScore("STEVE", 12);
            FileOutputStream fos = openFileOutput("scores.txt", Context.MODE_PRIVATE);
            highScore.writeBack(fos);
        } catch (Exception e) { System.out.println("Scores Activity fail\n");}

        ArrayAdapter<String> scoreAdapter = new ArrayAdapter<String>(ScoresActivity.this, android.R.layout.simple_list_item_1, scores);
        ListView scoreList = (ListView) findViewById(R.id.Score_List);
        scoreList.setAdapter(scoreAdapter);


    }

}