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
        setContentView(R.layout.gameover_multi);

        Intent intent = getIntent();
        int myScore = intent.getIntExtra("MY_SCORE", 0);
        int theirScore =  intent.getIntExtra("OP_SCORE", 0);
        String p1score = Integer.toString(intent.getIntExtra("MY_SCORE", 0));
        String p2score = Integer.toString(intent.getIntExtra("OP_SCORE", 0));


        TextView p1 = (TextView) findViewById(R.id.Score1);
        TextView p2 = (TextView) findViewById(R.id.Score2);

        p1.setText(p1score);
        p2.setText(p2score);

        String msg = "";

        if (myScore > theirScore) {
            msg = "YOU WIN!!!" + "\nYour Score:" + myScore + "\nTheir Score: " + theirScore;
        }
        else if (myScore < theirScore){
            msg = "YOU LOSE!!!" + "\nYour Score:" + myScore + "\nTheir Score: " + theirScore;
        }
        else {
            msg = "ITS A TIE!!!" + "\nYour Score:" + myScore + "\nTheir Score: " + theirScore;
        }

        new AlertDialog.Builder(this)
                .setTitle("Title")

                .setMessage(msg)

                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                       Intent intent = new Intent(getApplicationContext(), ChooseModeMainActivity.class);
                        startActivity(intent);
                    }})
                .setNegativeButton(android.R.string.no, null).show();


    }


}

