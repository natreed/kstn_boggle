package com.pdx.kstn.kstn_boggle;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by thanhhoang on 1/28/17.
 */

public class Player {

    private int score;
    private int scoreForCurrentRound;
    private int round;
    private ArrayList<String> foundWords;
    private ArrayList<String> foundWordsCurrentRound;
    public CountDownTimer timer = null;
    public TextView text_timer;
    public Context activity_context;
    final long START_TIME = 30000;
    public long totalTime = START_TIME;

    public ArrayList<String> allValidWords = new ArrayList<String>();

    Player(TextView timerText, Context context) {
        this.score = 0;
        this.scoreForCurrentRound = 0;
        this.round = 1;
        this.foundWords = new ArrayList<String>();
        this.foundWordsCurrentRound = new ArrayList<String>();
        this.text_timer = timerText;
        this.activity_context = context;
        timer = new PlayerTimer(totalTime, 1000);
    }

    public int getScore() { return this.score; }
    public void setScore(int s) {this.score = s;}

    public int getScoreForCurrentRound() { return this.scoreForCurrentRound; }

    public int getRound() { return this.round; }

    public ArrayList<String> getFoundWords() { return this.foundWords; }

    public ArrayList<String> getFoundWordsCurrentRound() { return foundWordsCurrentRound; }

    public void setAllVallidWords (ArrayList<String> vallidWords) {
        this.allValidWords = vallidWords;
    }

    public ArrayList<String> getAllValidWords () {
        return this.allValidWords;
    }


//    static public int getNewTimer(int timeLeft, int score) {
//        return timeLeft + score;
//    }
    public int getNumWordLastRound() {
        return foundWordsCurrentRound.size();
    }

    public void moveToNextRound() {
        this.scoreForCurrentRound = 0;
        round += 1;
        this.foundWordsCurrentRound = new ArrayList<String>();
    }



    public boolean updateInfor(String word) {
        if (foundWordsCurrentRound.contains(word) == true)
            return false;

        foundWordsCurrentRound.add(word);
        foundWords.add(word);

        switch (word.length()) {
            case 3:
                this.score += 1;
                this.scoreForCurrentRound += 1;
                break;
            case 4:
                this.score += 1;
                this.scoreForCurrentRound += 1;
                break;
            case 5:
                this.score += 2;
                this.scoreForCurrentRound += 2;
                break;
            case 6:
                this.score += 3;
                this.scoreForCurrentRound += 3;
                break;
            case 7:
                this.score += 5;
                this.scoreForCurrentRound += 5;
                break;
            default:
                this.score += 10;
                this.scoreForCurrentRound += 10;
                break;
        }

        return true;
    }

    // -1 :     invalid word
    // 0  :     input word is in found list already
    // 1  :     valid word, added to found words list and update score
    public int updateInfor(String word, ArrayList<String> validList) {
        if (validList.contains(word) == false)
            return -1;

        if (foundWordsCurrentRound.contains(word))
            return 0;

        foundWordsCurrentRound.add(word);
        foundWords.add(word);

        switch (word.length()) {
            case 3:
                this.score += 1;
                this.scoreForCurrentRound += 1;
                break;
            case 4:
                this.score += 1;
                this.scoreForCurrentRound += 1;
                break;
            case 5:
                this.score += 2;
                this.scoreForCurrentRound += 2;
                break;
            case 6:
                this.score += 3;
                this.scoreForCurrentRound += 3;
                break;
            case 7:
                this.score += 5;
                this.scoreForCurrentRound += 5;
                break;
            default:
                this.score += 10;
                this.scoreForCurrentRound += 10;
                break;
        }

        return 1;
    }

    public class PlayerTimer extends CountDownTimer {
        public PlayerTimer(long startTime, long interval) {
            super(startTime, interval);
        }
        @Override
        public void onFinish() {
            text_timer.setText("TIME'S UP!");
            gameOver(activity_context);
        }
        @Override
        public void onTick(long millisUntilFinished) {
            text_timer.setText("Timer: " + millisUntilFinished / 1000);
            totalTime = millisUntilFinished;
            //text_timer.setText(millisUntilFinished/60000 + ":" + millisUntilFinished/1000 % (millisUntilFinished/60000*60));
        }
    }


    public void initiateTimer () {
        this.timer = new PlayerTimer(START_TIME, 1000);
        timer.start();
    }

    public void updateTimer (int millisToAdd) {
        this.totalTime = this.totalTime + millisToAdd*1000;
        timer.cancel();
        timer.start();
    }

    public void resetTimer () {
        this.totalTime = this.START_TIME;
        timer.cancel();
        timer.start();
    }

    public void gameOver(Context context) {
        Intent intend = new Intent(context, GameOver.class);
        intend.putExtra("PLAYER_SCORE", Integer.toString(getScore()));
        intend.putExtra("FOUND_WORDS", getFoundWords());
        intend.putExtra("POSSIBLE_WORDS", allValidWords);
        intend.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        System.out.println("reach 1");
        context.startActivity(intend);
        System.out.println("reach 2");
    }
}