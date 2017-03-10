package com.pdx.kstn.kstn_boggle;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import static android.R.id.input;

/**
 * Created by krishcline on 3/9/17.
 */


//public class GameOverMultipayer extends Activity {
//    Intent intent=getIntent();
//    boolean isWinner=Boolean.valueOf(intent.getStringExtra("WINNER"));
//    int p1score = intent.getIntExtra("MY_SCORE", 0);
//    int p2score = intent.getIntExtra("OP_SCORE", 0);
//
//
//private String msg;
//
//@Override
//protected void onCreate(Bundle savedInstanceState){
//    super.onCreate(savedInstanceState);
//        //setContentView(R.layout.gameover);
//    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//    if (isWinner)
//        builder.setTitle("YOU ARE THE WINNER!!" + "\nYour Score: " + p1score + "\nOpponent Score: " + p2score);
//    else
//        builder.setTitle("SORRY BUDDY YOU LOSE"+ "\nYour Score: " + p1score + "\nOpponent Score: " + p2score);
//    builder.setView(input);
//    //setup Buttons
//    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            System.out.println("YOU ARE THE WINNER");
//        }
//    });
//    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//        @Override
//        public void onClick(DialogInterface dialog, int which) {
//            dialog.cancel();
//        }
//    });
//
//    builder.show();
//
//    // setListAdapter(adapter);
//
//        }
//}

public class GameOverMultiplayer extends Activity {
    private String name = "";
    private HighScore highScore;
    private int score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover);

        Intent intent = getIntent();
        int p1score = intent.getIntExtra("MY_SCORE", 0);
        int p2score = intent.getIntExtra("OP_SCORE", 0);


        TextView p1 = (TextView) findViewById(R.id.textView3);
        TextView p2 = (TextView) findViewById(R.id.textView4);

        p1.setText(p1score);
        p2.setText(p2score);




    }


}

