package com.pdx.kstn.kstn_boggle;

import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by nathanreed on 1/26/17.
 */

public class GameBoard extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_board);
        String [] myStringArray = new String [] {"A", "B", "C", "D"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, myStringArray);

        ListView listView = (ListView) findViewById(R.id.foundWords);
        listView.setAdapter(adapter);
    }

}
