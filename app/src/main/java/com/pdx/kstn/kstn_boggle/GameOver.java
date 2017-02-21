package com.pdx.kstn.kstn_boggle;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by thanhhoang on 2/18/17.
 */

public class GameOver extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);

        // setListAdapter(adapter);

        Intent intent = getIntent();
        int score = Integer.parseInt(intent.getStringExtra("PLAYER_SCORE"));
        ArrayList<String> foundWords = intent.getStringArrayListExtra("FOUND_WORDS");
        ArrayList<String> possibleWords = intent.getStringArrayListExtra("POSSIBLE_WORDS");

        ListView listView = (ListView) findViewById(R.id.list);
        ResultAdapter adapter = new ResultAdapter(this, score, possibleWords, foundWords);
//        setListAdapter(adapter);
        listView.setAdapter(adapter);

        System.out.println("Total possible words: " + possibleWords.size());

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
}
