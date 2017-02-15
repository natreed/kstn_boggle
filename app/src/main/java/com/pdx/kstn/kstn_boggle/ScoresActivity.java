package com.pdx.kstn.kstn_boggle;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.InputStream;


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
            InputStream in = getResources().openRawResource(R.raw.scores);
            HighScore highScore = new HighScore();
            highScore.loadScores(in);
            scores = highScore.getScores().toArray(new String[0]);
        } catch (Exception e) { System.out.print("HERE.\n");}



        ArrayAdapter<String> scoreAdapter = new ArrayAdapter<String>(ScoresActivity.this, android.R.layout.simple_list_item_1, scores);
        ListView scoreList = (ListView) findViewById(R.id.Score_List);
        scoreList.setAdapter(scoreAdapter);

    }
}
