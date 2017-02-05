package com.pdx.kstn.kstn_boggle;

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

    Player() {
        this.score = 0;
        this.scoreForCurrentRound = 0;
        this.round = 1;
        this.foundWords = new ArrayList<String>();
        this.foundWordsCurrentRound = new ArrayList<String>();
    }

    public int getScore() { return this.score; }

    public int getRound() { return this.round; }

    /**
     * New timer depend how many score player get from last round
     * @param timeLeft
     * @param score
     * @return
     */
    static public int getNewTimer(int timeLeft, int score) {
        return timeLeft + score;
    }


    public void moveToNextRound() {
        this.scoreForCurrentRound = 0;
        this.foundWordsCurrentRound = new ArrayList<String>();
    }

    /**
     * Update scores and word lists when found a new word
     * @param word
     * @param score
     */
    public boolean updateInfor(String word, int score) {
        if (foundWordsCurrentRound.contains(word) == true)
            return false;

        foundWordsCurrentRound.add(word);
        foundWords.add(word);
        this.score += score;
        this.scoreForCurrentRound += score;

        return true;
    }



}
