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




public class GameOverMultiplayer extends Activity {
    private String name = "";
    private HighScore highScore;
    private int score;
    String difficulty = null;
    String mode = null;
    String fileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gameover_multi);

        Intent intent = getIntent();
        int myScore = intent.getIntExtra("MY_SCORE", 0);
        int theirScore =  intent.getIntExtra("OP_SCORE", 0);

        this.difficulty = intent.getStringExtra("PLAYER_LEVEL");
        this.mode = intent.getStringExtra("PLAYER_MODE");
        fileName = mode + difficulty;

        highScore = new HighScore(getApplicationContext(), fileName);

        final String p1score = Integer.toString(intent.getIntExtra("MY_SCORE", 0));
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
        /*
        if (myScore >= highScore.lowestScore() || highScore.scores.size() < 5) {
              getName();
         */
        new AlertDialog.Builder(this)
                .setTitle("GAME OVER")

                .setMessage(msg)

                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
//                       Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                        startActivity(intent);

                        if (Integer.valueOf(p1score) >= highScore.lowestScore() || highScore.scores.size() < 5) {
                            getName();
                            try {
                                highScore.updateScore(name, score);
                            } catch (Exception e) { e.printStackTrace(); }

                        }
                        else {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                        }


                    }})
                .setNegativeButton(android.R.string.no, null).show();


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
                name  = input.getText().toString();
                try {
                    highScore.updateScore(name, score);
                } catch (Exception e) {e.printStackTrace();}
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                dialog.cancel();
            }
        });

        builder.show();

    }


}

